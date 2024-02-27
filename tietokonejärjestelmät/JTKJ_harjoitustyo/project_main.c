/*
JTKJ Harjoitustyö 2021 

Tekijät:
Ville Nättinen
Jani Nivalainen
Arimo Pukari

Ohjaa SensorTagin avulla taustajärjestelmässä pyörivää Tamagotchia
Mittaa sensoreilta tulevaa dataa sensorTaskissa, vastaanottaa ja tarpeen vaatiessa lähettää
6LoWPAN:in avulla viestejä taustajärjestelmän kanssa, joka päivittää Tamagotchin vointia.
commTask hoitaa yhteyden muodostamisen ja viestien vastaanottamisen.
buzzerTask pyörittää summeria loopissa, joka tilakoneen tilan mukaan soittaa erilaisia
ääniä.
Funktio buttonFxn käsittelee painonapin ja funktio powerButtonFxn käsittelee virtanapin.

Käyttöjärjestelmässä punainen ledi palaa, kun laite on päällä ja valmiustilassa, vihreä ledi palaa kun laite pyörii
(sensorit mittaavat dataa) ja ledit ovat pois, kun laite on "sammutettu".
Liikkeiden nostaessa arvoja soitetaan äänimerkki, BEEP-viestin vastaanotettua soitetaan eri äänimerkki

Liikkeet ovat: pöydällä kallistus oikealla, vasemmalla, eteen tai taakse ja paluu takaisin pöydälle,
"tuplahyppy", eli laitteen nosto pöydältä kahdesti peräkkäin nopeasti
Eteen kallistus liikunta, taakse kallistus syönti, oikealla ja vasemmalle kallistus hoito.
Tuplahyppäys nostaa kaikkia arvoja.
Laite lähettää Tamagotchissa näkyvän viestin 1 laitteen ollessa päällä ja viestin 2, kun jokin
arvoista nousee
Käynnistyessä SensorTagin on oltava tasaisella pinnalla paikoillaan

Työssä on käytetty apuna kurssin esimerkkikoodeja (paljon)
*/

/* C Standard library */
#include <stdio.h>
#include <string.h>
#include <math.h>

/* XDCtools files */
#include <xdc/std.h>
#include <xdc/runtime/System.h>

/* BIOS Header files */
#include <ti/sysbios/BIOS.h>
#include <ti/sysbios/knl/Clock.h>
#include <ti/sysbios/knl/Task.h>
#include <ti/drivers/PIN.h>
#include <ti/drivers/pin/PINCC26XX.h>
#include <ti/drivers/I2C.h>
#include <ti/drivers/i2c/I2CCC26XX.h>
#include <ti/drivers/Power.h>
#include <ti/drivers/power/PowerCC26XX.h>
#include <ti/drivers/UART.h>

/* Board Header files */
#include "Board.h"
#include "wireless/comm_lib.h"
#include "wireless/address.h"
#include "sensors/opt3001.h"
#include "sensors/mpu9250.h"
#include "sensors/tmp007.h"
#include "sensors/buzzer.h"

/* Tasks */
#define STACKSIZE 2048
Char sensorTaskStack[STACKSIZE];
Char buzzerTaskStack[STACKSIZE];
Char commTaskStack[STACKSIZE];

/* Tilakoneiden esittely */
// Päätilakone
enum mainState { WAITING=1, RUNNING };
enum mainState programState = WAITING;
// Summerin tilakone
enum buzzerState { SILENT=1, VALUE_UP, LOW_VALUE, POWER_UP };
enum buzzerState soundState = POWER_UP;

// Globaalit muuttujat
char payload[16];

// Painonapin RTOS-muuttujat ja alustus
static PIN_Handle buttonHandle;
static PIN_State buttonState;

PIN_Config buttonConfig[] = {
    Board_BUTTON0  | PIN_INPUT_EN | PIN_PULLUP | PIN_IRQ_NEGEDGE, 
    PIN_TERMINATE
};

// Virtanapin RTOS-muuttujat ja alustus
static PIN_Handle powerButtonHandle;
static PIN_State powerButtonState;

PIN_Config powerButtonConfig[] = {
    Board_BUTTON1 | PIN_INPUT_EN | PIN_PULLUP | PIN_IRQ_NEGEDGE,
    PIN_TERMINATE
};
PIN_Config powerButtonWakeConfig[] = {
    Board_BUTTON1 | PIN_INPUT_EN | PIN_PULLUP | PINCC26XX_WAKEUP_NEGEDGE,
    PIN_TERMINATE
};

// Ledien RTOS-muuttujat ja alustus
// Punainen ledi
static PIN_Handle ledRedHandle;
static PIN_State ledRedState;

PIN_Config ledRedConfig[] = {
    Board_LED1 | PIN_GPIO_OUTPUT_EN | PIN_GPIO_HIGH | PIN_PUSHPULL | PIN_DRVSTR_MAX, 
    PIN_TERMINATE
};

// Vihreä ledi
static PIN_Handle ledGreenHandle;
static PIN_State ledGreenState;

PIN_Config ledGreenConfig[] = {
    Board_LED0 | PIN_GPIO_OUTPUT_EN | PIN_GPIO_LOW | PIN_PUSHPULL | PIN_DRVSTR_MAX, 
    PIN_TERMINATE
};

// Summerin RTOS-muuttujat ja alustus
static PIN_Handle buzzerHandle;

PIN_Config buzzerConfig[] = {
  Board_BUZZER | PIN_GPIO_OUTPUT_EN | PIN_GPIO_LOW | PIN_PUSHPULL | PIN_DRVSTR_MAX,
  PIN_TERMINATE
};

// MPU power pin RTOS-muuttujat ja alustus
static PIN_Handle handleMpuPin;
static PIN_State  stateMpuPin;

static PIN_Config MpuPinConfig[] = {
    Board_MPU_POWER  | PIN_GPIO_OUTPUT_EN | PIN_GPIO_HIGH | PIN_PUSHPULL | PIN_DRVSTR_MAX,
    PIN_TERMINATE
};

// MPU:n oma I2C interface
static const I2CCC26XX_I2CPinCfg i2cMPUCfg = {
    .pinSDA = Board_I2C0_SDA1,
    .pinSCL = Board_I2C0_SCL1
};

/* Virtanapin käsittelijäfunktio */
Void powerButtonFxn(PIN_Handle handle, PIN_Id pinId) {
    // Odotetaan hetki ihan varalta..
    Task_sleep(100000 / Clock_tickPeriod);
    // Taikamenot
    PIN_close(powerButtonHandle);
    PINCC26XX_setWakeup(powerButtonWakeConfig);
    // Tulostetaan varmistukseksi
    System_printf("SHUTDOWN\n");
    System_flush();
    // Valot pois, kun virrat pois
    PIN_setOutputValue( ledGreenHandle, Board_LED0, 0 );
    PIN_setOutputValue( ledRedHandle, Board_LED1, 0 );
    // Virrat pois (melkein ainakin)
    Power_shutdown(NULL, 0);
} 

/* 
Painonapin käsittelijäfunktio 
Oletuksena ohjelma WAITING-tilassa, jolloin sensorit eivät mittaa ja punainen ledi palaa merkkinä
Napin painalluksella päästään RUNNING-tilaan, jolloin sensorit alkavat mittaamaan ja vihreä ledi
palaa
*/
void buttonFxn(PIN_Handle handle, PIN_Id pinId) {

    char *msg;

    // Vuorotellaan ledit päälle/pois napin painalluksella
    uint_t pinValue0 = PIN_getOutputValue( Board_LED0 );
    uint_t pinValue1 = PIN_getOutputValue( Board_LED1 );
    pinValue1 = !pinValue1;
    pinValue0 = !pinValue0;

    /* 
    Jos ohjelma ei pyöri, laitetaan se pyörimään (merkiksi vihreä ledi) ja toisin päin (merkiksi punainen ledi)
    ja lähetetään kiva viesti Tamagotchilla
    */
    if (programState == WAITING) {
        programState = RUNNING;
        PIN_setOutputValue( ledGreenHandle, Board_LED0, pinValue0 );
        PIN_setOutputValue( ledRedHandle, Board_LED1, pinValue1 );
        msg = "MSG1:Homma toimii";
    }
    else if (programState == RUNNING) {
        programState = WAITING;
        PIN_setOutputValue( ledGreenHandle, Board_LED0, pinValue0 );
        PIN_setOutputValue( ledRedHandle, Board_LED1, pinValue1 );
        msg = "MSG1:Lepotauko";    
    }
    sprintf(payload, "%c", msg);
    Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
    StartReceive6LoWPAN();
}

/* Task-funktiot */
/* Summerin task-funktio */
Void buzzerTaskFxn(UArg arg0, UArg arg1) {

    // Ikuinen looppi, soitetaan ääniä tilakoneen tilojen mukaan
    while (1) {
        /* Soitetaan tämä, kun SensorTag käynnistetään, siirrytään lopuksi hiljaiseen tilaan */
        if (soundState == POWER_UP) {
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(660);
            Task_sleep(100000/Clock_tickPeriod);
            buzzerClose();
            Task_sleep(150000/Clock_tickPeriod);
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(660);
            Task_sleep(100000/Clock_tickPeriod);
            buzzerClose();
            Task_sleep(300000/Clock_tickPeriod);
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(660);
            Task_sleep(100000/Clock_tickPeriod);
            buzzerClose();
            Task_sleep(300000/Clock_tickPeriod);
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(520);
            Task_sleep(100000/Clock_tickPeriod);
            buzzerClose();
            Task_sleep(150000/Clock_tickPeriod);
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(660);
            Task_sleep(100000/Clock_tickPeriod);
            buzzerClose();
            Task_sleep(300000/Clock_tickPeriod);
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(780);
            Task_sleep(100000/Clock_tickPeriod);
            buzzerClose();
            Task_sleep(750000/Clock_tickPeriod);
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(390);
            Task_sleep(100000/Clock_tickPeriod);
            buzzerClose();
            soundState = SILENT;
        }
        /* Soitetaan tämä ääni, kun Tamagotchin arvot nousevat ja summerin tilakoneen tila on VALUE_UP,
        palataan lopuksi hiljaiseen tilaan */
        else if (soundState == VALUE_UP) {
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(1200);
            Task_sleep(50000 / Clock_tickPeriod);
            buzzerClose();
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(1000);
            Task_sleep(50000 / Clock_tickPeriod);
            buzzerClose();
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(1500);
            Task_sleep(100000 / Clock_tickPeriod);
            buzzerClose();
            soundState = SILENT;
        }
        /* Soitetaan tämä ääni, kun summerin tilakoneen tila on LOW_VALUE, eli on vastaanotettu BEEP-viesti,
        palataan lopuksi hiljaiseen tilaan */
        else if (soundState == LOW_VALUE) {
            buzzerOpen(buzzerHandle);
            buzzerSetFrequency(2000);
            Task_sleep(100000 / Clock_tickPeriod);
            buzzerSetFrequency(2000);
            Task_sleep(100000 / Clock_tickPeriod);
            buzzerClose();
            soundState = SILENT;
        }
        // Sitten nukutaan hetki
        Task_sleep(500000 / Clock_tickPeriod);
    }
}

/* Sensorien task-funktio */
Void sensorTaskFxn(UArg arg0, UArg arg1) {
    
	/* muuttujat */
    float ax, ay, az, gx, gy, gz;
    char merkkijono[80]; // Vain debugausta varten
    double lux, temperature;
    int i, time=0;
    float mpuDataAx[10], mpuDataAy[10], mpuDataAz[10], mpuDataGx[10], mpuDataGy[10], mpuDataGz[10];

    // Alustetaan I2C-yhteys
    I2C_Handle      i2c;
    I2C_Params      i2cParams;
	I2C_Params_init(&i2cParams);
    i2cParams.bitRate = I2C_400kHz;

    // Alustetaan MPU:lle oma I2C-yhteys
    I2C_Handle i2cMPU; 
	I2C_Params i2cMPUParams;
    I2C_Params_init(&i2cMPUParams);
    i2cMPUParams.bitRate = I2C_400kHz;
    // Note the different configuration below
    i2cMPUParams.custom = (uintptr_t)&i2cMPUCfg;

    // Avataan MPU:n I2c-yhteys
    i2cMPU = I2C_open(Board_I2C, &i2cMPUParams);
    if (i2cMPU == NULL) {
        System_abort("Error Initializing I2CMPU\n");
    }

    // MPU:n virrat päälle
    PIN_setOutputValue(handleMpuPin, Board_MPU_POWER, Board_MPU_POWER_ON);
    // Odotellaan 100ms käynnistymistä
	Task_sleep(100000 / Clock_tickPeriod);
    System_printf("MPU9250: Power ON\n");
    System_flush();

    // MPU:n setup ja kalibrointi
    System_printf("MPU9250: Setup and calibration...\n");
    System_flush();
    mpu9250_setup(&i2cMPU);
    System_printf("MPU9250: Setup and calibration OK\n");
    System_flush();

    I2C_close(i2cMPU);

    // Avataan i2c yhteys
    i2c = I2C_open(Board_I2C, &i2cParams);
    if (i2c == NULL) {
        System_abort("Error Initializing I2C\n");
    }

    // Alustetaan sensori OPT3001 setup-funktiolla 100ms viiveellä
    Task_sleep(100000 / Clock_tickPeriod); // 100ms
    opt3001_setup(&i2c);
    // Alustetaan sensori TMP007 setup-funktiolla 100ms viiveellä
    Task_sleep(100000 / Clock_tickPeriod); // 100ms
    tmp007_setup(&i2c);  
    // Suljetaan I2C-yhteys
    I2C_close(i2c);
    
    while (1) {
        if (programState == RUNNING) {
            // Avataan i2c yhteys
            i2c = I2C_open(Board_I2C, &i2cParams);
            if (i2c == NULL) {
                System_abort("Error Initializing I2C\n");
            }

            // Luetaan valosensorilta dataa
            lux = opt3001_get_data(&i2c);

            // Luetaan lämpötilasensorilta dataa 
            temperature = tmp007_get_data(&i2c);
            
            // Suljetaan I2C-yhteys, jotta tehdään tilaa MPU:n omalle yhteydelle
            I2C_close(i2c);

            // Avataan MPU:n I2C-yhteys
            i2cMPU = I2C_open(Board_I2C, &i2cMPUParams);
            if (i2cMPU == NULL) {
                System_abort("Error Initializing I2CMPU\n");
            }

	        // Kerätään MPU:lta dataa
		    mpu9250_get_data(&i2cMPU, &ax, &ay, &az, &gx, &gy, &gz);
            I2C_close(i2cMPU);
            /*
            Tallennetaan MPU:lta dataa kuuteen eri taulukkoon siten, että liu'utetaan vanhin mittausarvo taulukon
            alkupäästä ulos ja lisätään viimeisin arvo taulukon loppuun. Taulukoiden pituus 10, oletuksena
            dataa tallennetaan sekunnin ajalta.
            */
            for (i=0; i<9; i++) {
                mpuDataAx[i] = mpuDataAx[i+1];
                mpuDataAy[i] = mpuDataAy[i+1];
                mpuDataAz[i] = mpuDataAz[i+1];
                mpuDataGx[i] = mpuDataGx[i+1];
                mpuDataGy[i] = mpuDataGy[i+1];
                mpuDataGz[i] = mpuDataGz[i+1];
            }
            mpuDataAx[9] = ax;
            mpuDataAy[9] = ay;
            mpuDataAz[9] = az;
            mpuDataGx[9] = gx;
            mpuDataGy[9] = gy;
            mpuDataGz[9] = gz;
            
            /*
            Tarkastellaan MPU:lta tulevaa dataa, jonka mukaan määritellään tapahtuuko haluttuja liikkeitä.
            Eri liikkeet ovat "tuplahyppy" (pöydältä nosto kahdesti nopeasti peräkkäin) sekä kallistukset
            oikea/vasen/eteen/taakse.
            Lähtetilanteessa SensorTagin tulee olla pöydällä paikoillaan
            */
            // Katsotaan onko SensorTag paikoillaan
            if ((fabs(mpuDataAx[3]) < 0.1 && fabs(mpuDataAy[3]) < 0.1 && mpuDataAz[3] < -0.9 && mpuDataAz[3] > -1.1) &&
                (fabs(mpuDataGx[3]) < 5.0 && fabs(mpuDataGy[3]) < 5.0 && fabs(mpuDataGz[3]) < 5.0)) {
                // Seuraavissa vaiheissa katsotaan tapahtuvatko halutut liikkeet (nosto, lasku, nosto, lasku)
                if ((fabs(mpuDataAz[4]) > 1.1 || fabs(mpuDataAz[4]) < 0.9) || (fabs(mpuDataAz[5]) > 1.1 || fabs(mpuDataAz[5]) < 0.9) && 
                    ((fabs(mpuDataGx[4]) || fabs(mpuDataGy[4]) || fabs(mpuDataGz[4])) > 10 || (fabs(mpuDataGx[5]) || fabs(mpuDataGy[5]) || fabs(mpuDataGz[5]))) > 10 && 
                    (fabs(mpuDataAx[5]) && fabs(mpuDataAy[5])) < 0.1) {
                    System_printf("1\n");
                    System_flush();              
                    if ((fabs(mpuDataAz[5]) > 1.1 || fabs(mpuDataAz[5]) < 0.9) || (fabs(mpuDataAz[6]) > 1.1 || fabs(mpuDataAz[6]) < 0.9) && 
                        ((fabs(mpuDataGx[5]) || fabs(mpuDataGy[5]) || fabs(mpuDataGz[5])) > 10 || (fabs(mpuDataGx[6]) || fabs(mpuDataGy[6]) || fabs(mpuDataGz[6]))) > 10 && 
                        (fabs(mpuDataAx[6]) && fabs(mpuDataAy[6])) < 0.1) {
                        System_printf("2\n");
                        System_flush();
                        if ((fabs(mpuDataAz[6]) > 1.1 || fabs(mpuDataAz[6]) < 0.9) || (fabs(mpuDataAz[7]) > 1.1 || fabs(mpuDataAz[7]) < 0.9) &&
                         (fabs(mpuDataGx[6]) || fabs(mpuDataGy[6]) || fabs(mpuDataGz[6])) > 10 || (fabs(mpuDataGx[7]) || fabs(mpuDataGy[7]) || fabs(mpuDataGz[7])) > 10 &&
                          (fabs(mpuDataAx[7]) && fabs(mpuDataAy[7])) < 0.1) {
                            System_printf("3\n");
                            System_flush();
                            if ((fabs(mpuDataAz[7]) > 1.1 || fabs(mpuDataAz[7]) < 0.9) || (fabs(mpuDataAz[8]) > 1.1 || fabs(mpuDataAz[8]) < 0.9) && 
                            (fabs(mpuDataGx[7]) || fabs(mpuDataGy[7]) || fabs(mpuDataGz[7])) > 10 || (fabs(mpuDataGx[8]) || fabs(mpuDataGy[8]) || fabs(mpuDataGz[8])) > 10  && 
                            (fabs(mpuDataAx[8]) && fabs(mpuDataAy[8])) < 0.1) {
                                System_printf("4\n");
                                System_flush();
                                // Liikkeet tapahtuneet ja SensorTag on takaisin aloitusasennossa
                                if ((fabs(mpuDataAz[9])-1) < 0.1 && fabs(mpuDataGx[9]) < 10 && fabs(mpuDataGy[9]) < 10 && fabs(mpuDataGz[9]) < 10 && 
                                (fabs(mpuDataAx[9]) && fabs(mpuDataAy[9]) < 0.1)) {
                                    System_printf("tuplahyppy\n");
                                    System_flush();  
                                    // Nostetaan tamagotchin joka arvoa kahdella
                                    sprintf(payload, "ACTIVATE:2;2;2");
                                    Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                                    // Lähetetään vielä viesti merkiksi
                                    sprintf(payload, "MSG2:Oijoi!");
                                    Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                                    StartReceive6LoWPAN();
                                    // Muutetaan summerin tilakoneen tilaa äänimerkin saamiseksi
                                    soundState = VALUE_UP;
                                    // Tyhjennetään listat tuplalyöntien välttämiseksi
                                    for (i=0; i<9; i++) {
                                        mpuDataAx[i] = 0;
                                        mpuDataAy[i] = 0;
                                        mpuDataAz[i] = 0;
                                        mpuDataGx[i] = 0;
                                        mpuDataGy[i] = 0;
                                        mpuDataGz[i] = 0;
                                    }
                                    // Viive liikkeen jälkeen
                                    Task_sleep(5000000 / Clock_tickPeriod); // 5 sec                         
                                }
                            } 
                        }
                    }
                }
                // Katsotaan tapahtuuko pöydällä kallistus taakse
                if (fabs(mpuDataAx[5]) < 0.1 && (mpuDataAy[5] < -0.5 || mpuDataAy[6] < -0.5 ) && (mpuDataAz[5] > -0.9 || mpuDataAz[6] > -0.9) &&
                    fabs(mpuDataGx[5]) > 100.0 && fabs(mpuDataGy[5]) < 25.0 && fabs(mpuDataGz[5]) < 25.0 ) {
                    System_printf("taakse kallistus\n");
                    System_flush();
                    // Katsotaan palataanko vielä alkuasentoon
                    if ((fabs(mpuDataAx[7]) < 0.1 && fabs(mpuDataAy[7]) < 0.1 && mpuDataAz[7] < -0.9) &&
                        (fabs(mpuDataGx[7]) < 5.0 && fabs(mpuDataGy[7]) < 5.0 && fabs(mpuDataGz[7]) < 5.0)) {
                        System_printf("taakse kallistus palaa lähtöasentoon\n");
                        System_flush();
                        // Jos on lämmin, ruoka-arvo nousee enemmän
                        if (temperature >= 30) {
                            sprintf(payload, "EAT:3");
                        }
                        // Jos kylmä, niin toisin päin
                        else if (temperature < 30) {
                            sprintf(payload, "EAT:2");
                        }    
                        // Lähetetään syöntikomento
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        sprintf(payload, "MSG2:Kyllä maistuu!");
                        // Lähetetään vielä kiva viesti
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        StartReceive6LoWPAN();
                        // Muutetaan summerin tilakoneen tilaa äänimerkin saamiseksi
                        soundState = VALUE_UP;
                        // Tyhjennetään listat tuplalyöntien välttämiseksi
                        for (i=0; i<9; i++) {
                            mpuDataAx[i] = 0;
                            mpuDataAy[i] = 0;
                            mpuDataAz[i] = 0;
                            mpuDataGx[i] = 0;
                            mpuDataGy[i] = 0;
                            mpuDataGz[i] = 0;
                        }   
                        // Viive liikkeen jälkeen
                        Task_sleep(3000000 / Clock_tickPeriod); // 3 sec     
                    }
                }
                // Katsotaan tapahtuuko pöydällä kallistus eteen
                else if (fabs(mpuDataAx[5]) < 0.1 && (mpuDataAy[5] > 0.5 || mpuDataAy[6] > 0.5 ) && (mpuDataAz[5] > -0.9 || mpuDataAz[6] > -0.9) &&
                    fabs(mpuDataGx[5]) > 100.0 && fabs(mpuDataGy[5]) < 25.0 && fabs(mpuDataGz[5]) < 25.0 ) {
                    System_printf("eteen kallistus\n");
                    System_flush();
                    // Katsotaan palataanko vielä alkuasentoon
                    if ((fabs(mpuDataAx[7]) < 0.1 && fabs(mpuDataAy[7]) < 0.1 && mpuDataAz[7] < -0.9 && mpuDataAz[7] > -1.1) &&
                        (fabs(mpuDataGx[7]) < 5.0 && fabs(mpuDataGy[7]) < 5.0 && fabs(mpuDataGz[7]) < 5.0)) {
                        System_printf("eteen kallistus palaa lähtöasentoon\n");
                        System_flush(); 
                        // Jos on lämmin, liike nousee enemmän
                        if (temperature >= 30){
                            sprintf(payload, "EXERCISE:3");
                        }
                        // Jos on kylmä, niin toisin päin
                        else if (temperature < 30){
                            sprintf(payload, "EXERCISE:2"); 
                        }
                        // Lähetetään liikekomento
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        StartReceive6LoWPAN();
                        // Lähetetään vielä kiva viesti
                        sprintf(payload, "MSG2:Jaksaa jaksaa!");
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        // Muutetaan summerin tilakoneen tilaa äänimerkin saamiseksi
                        soundState = VALUE_UP;
                        // Tyhjennetään listat tuplalyöntien välttämiseksi
                        for (i=0; i<9; i++) {
                            mpuDataAx[i] = 0;
                            mpuDataAy[i] = 0;
                            mpuDataAz[i] = 0;
                            mpuDataGx[i] = 0;
                            mpuDataGy[i] = 0;
                            mpuDataGz[i] = 0;
                        } 
                        // Viive liikkeen jälkeen
                        Task_sleep(3000000 / Clock_tickPeriod); // 3 sec          
                    }  
                }
                // Katsotaan tapahtuuko pöydällä kallistus oikealle
                // Nämä arvot ovat muita väljempiä, sillä ne ottavat huomioon SensorTagin kumisuojan
                else if ((mpuDataAx[7] > 0.5 || mpuDataAx[8] > 0.5 ) && fabs(mpuDataAy[7]) < 0.2 && (mpuDataAz[7] > -0.9 || mpuDataAz[8] > -0.9) &&
                    fabs(mpuDataGx[7]) < 50.0 && fabs(mpuDataGy[7]) > 80.0 && fabs(mpuDataGz[7]) < 25.0 ) {
                    System_printf("oikealle kallistus\n");
                    System_flush();
                    // Katsotaan palataanko vielä alkuasentoon
                    if ((fabs(mpuDataAx[9]) < 0.1 && fabs(mpuDataAy[9]) < 0.1 && mpuDataAz[9] < -0.9 && mpuDataAz[9] > -1.1) &&
                        (fabs(mpuDataGx[9]) < 5.0 && fabs(mpuDataGy[9]) < 5.0 && fabs(mpuDataGz[9]) < 5.0)) {
                        System_printf("oikealle kallistus palaa lähtöasentoon\n");
                        System_flush();
                        // Lähetetään hoitokomento
                        sprintf(payload, "PET:4");
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        // Lähetetään vielä kiva viesti
                        sprintf(payload, "MSG2:Tekkeepä eetvarttia!");
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        // Muutetaan summerin tilakoneen tilaa äänimerkin saamiseksi
                        StartReceive6LoWPAN();
                        soundState = VALUE_UP;                        
                        // Tyhjennetään listat tuplalyöntien välttämiseksi
                        for (i=0; i<9; i++) {
                            mpuDataAx[i] = 0;
                            mpuDataAy[i] = 0;
                            mpuDataAz[i] = 0;
                            mpuDataGx[i] = 0;
                            mpuDataGy[i] = 0;
                            mpuDataGz[i] = 0;
                        }  
                        // Viive liikkeen jälkeen
                        Task_sleep(3000000 / Clock_tickPeriod); // 3 sec          
                    }
                }
                // Katsotaan tapahtuuko pöydällä kallistus vasemmalle
                else if ((mpuDataAx[7] < -0.5 || mpuDataAx[8] < -0.5 ) && fabs(mpuDataAy[7]) < 0.1 && (mpuDataAz[7] > -0.9 || mpuDataAz[8] > -0.9) &&
                    fabs(mpuDataGx[7]) < 25.0 && mpuDataGy[7] < -80.0 && fabs(mpuDataGz[7]) < 25.0 ) {
                    System_printf("vasemmalle kallistus\n");
                    System_flush();
                    // Katsotaan palataanko vielä alkuasentoon
                    if ((fabs(mpuDataAx[9]) < 0.1 && fabs(mpuDataAy[9]) < 0.1 && mpuDataAz[9] < -0.9 && mpuDataAz[9] > -1.1) &&
                        (fabs(mpuDataGx[9]) < 5.0 && fabs(mpuDataGy[9]) < 5.0 && fabs(mpuDataGz[9]) < 5.0)) {
                        System_printf("vasemmalle kallistus palaa lähtöasentoon\n");
                        System_flush(); 
                        // Lähetetään hoitokomento
                        sprintf(payload, "PET:2");
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        // Lähetetään vielä kiva viesti
                        sprintf(payload, "MSG2:Tekkeepä eetvarttia!");
                        Send6LoWPAN(IEEE80154_SERVER_ADDR, payload, strlen(payload));
                        StartReceive6LoWPAN();
                        // Muutetaan summerin tilakoneen tilaa äänimerkin saamiseksi
                        soundState = VALUE_UP;    
                        // Tyhjennetään listat tuplalyöntien välttämiseksi
                        for (i=0; i<9; i++) {
                            mpuDataAx[i] = 0;
                            mpuDataAy[i] = 0;
                            mpuDataAz[i] = 0;
                            mpuDataGx[i] = 0;
                            mpuDataGy[i] = 0;
                            mpuDataGz[i] = 0;
                        }     
                        // Viive liikkeen jälkeen            
                        Task_sleep(5500000 / Clock_tickPeriod); // 5.5sec            
                    }
                }
            }

            // Debugaamista varten tulostusoptio
            
            char acc[80];
            sprintf(acc, "%hi,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", time, mpuDataAx[9], mpuDataAy[9], mpuDataAz[9], mpuDataGx[9], mpuDataGy[9], mpuDataGz[9]);
            System_printf("%s\n",acc);
            System_flush(); 
            
            // 100ms viive, sensorien mittausten välinen aika
            Task_sleep(100000 / Clock_tickPeriod);
            // Aikamuuttuja debuggaukseen, joka kasvaa yhdellä joka kierroksella
            time++;
        }
    }
}

/* 
Viestinnän task-funktio 
Tässä alustetaan vain yhteys, vastaanotetaan viestejä ja reagoidaan BEEP-viestiin
*/
Void commTaskFxn(UArg arg0, UArg arg1) {
    
    // Muuttujat
    uint16_t senderAddr;
    char *beep;

    // Radio alustetaan vastaanottotilaan
    int32_t result = StartReceive6LoWPAN();
    if(result != true) {
        System_abort("Wireless receive start failed");
    }
   
    // Vastaanotetaan viestejä loopissa
    while (1) {
        // HUOM! VIESTEJÄ EI SAA LÄHETTÄÄ TÄSSÄ SILMUKASSA
        // Viestejä lähtee niin usein, että se tukkii laitteen radion ja 
        // kanavan kaikilta muilta samassa harjoituksissa olevilta!!

        // jos true, viesti odottaa
        if (GetRXFlag()) {
            // Tyhjennetään puskuri (ettei sinne jäänyt edellisen viestin jämiä)
            memset(payload,0,16);
            // Luetaan viesti puskuriin payload
            Receive6LoWPAN(&senderAddr, payload, 16);
            /* Jos vastaanotetaan viesti, jossa on "BEEP", muutetaan summerin tilakoneen arvoa,
            jolla viestitetään käyttäjälle Tamagotchin tilasta */
            beep = strstr(payload, "BEEP");
            if (beep != NULL) {
                soundState = LOW_VALUE;
            }
            // Tulostetaan vastaanotettu viesti konsoli-ikkunaan
            System_printf(payload);
            System_flush();
        }          
    }
}

Int main(void) {

    // Initialize boards
    Board_initGeneral();
    Init6LoWPAN();
    Board_initI2C();

    // Task muuttujat
    Task_Handle commTaskHandle;
    Task_Params commTaskParams;
    Task_Handle buzzerTaskHandle;
    Task_Params buzzerTaskParams;
    Task_Handle sensorTaskHandle;
    Task_Params sensorTaskParams;

    // Sensori-taskin alustus
    Task_Params_init(&sensorTaskParams);
    sensorTaskParams.stackSize = STACKSIZE;
    sensorTaskParams.stack = &sensorTaskStack;
    sensorTaskParams.priority=2;
    sensorTaskHandle = Task_create(sensorTaskFxn, &sensorTaskParams, NULL);
    if (sensorTaskHandle == NULL) {
        System_abort("Task create failed!");
    }
    // Summeri-taskin alustus
    Task_Params_init(&buzzerTaskParams);
    buzzerTaskParams.stackSize = STACKSIZE;
    buzzerTaskParams.stack = &buzzerTaskStack;
    buzzerTaskParams.priority=2;
    buzzerTaskHandle = Task_create((Task_FuncPtr)buzzerTaskFxn, &buzzerTaskParams, NULL);
    if (buzzerTaskHandle == NULL) {
        System_abort("Task create failed!");
    }
    // Viestintä-taskin alustus
    Task_Params_init(&commTaskParams);
    commTaskParams.stackSize = STACKSIZE;
    commTaskParams.stack = &commTaskStack;
    commTaskParams.priority=1;
    commTaskHandle = Task_create(commTaskFxn, &commTaskParams, NULL);
    if (commTaskHandle == NULL) {
        System_abort("Task create failed!");
    }

    // Otetaan virtanappi ohjelman käyttöön
    powerButtonHandle = PIN_open(&powerButtonState, powerButtonConfig);
    if(!powerButtonHandle) {
        System_abort("Error initializing power button\n");
    }
    if (PIN_registerIntCb(powerButtonHandle, &powerButtonFxn) != 0) {
        System_abort("Error registering power button callback");
    }

    // Otetaan painonappi ja ledit ohjelman käyttöön
    buttonHandle = PIN_open(&buttonState, buttonConfig);
    if(!buttonHandle) {
        System_abort("Error initializing button pins\n");
    }
    ledGreenHandle = PIN_open(&ledGreenState, ledGreenConfig);
    if(!ledGreenHandle) {
        System_abort("Error initializing LED pins\n");
    }
    ledRedHandle = PIN_open(&ledRedState, ledRedConfig);
    if(!ledRedHandle) {
        System_abort("Error initializing LED pins\n");
    }

    // Asetetaan painonappi-pinnille keskeytyksen käsittelijäksi funktio buttonFxn
    if (PIN_registerIntCb(buttonHandle, &buttonFxn) != 0) {
        System_abort("Error registering button callback function");
    }

    // Avataan MPU:n virtapinni
    handleMpuPin = PIN_open(&stateMpuPin, MpuPinConfig);
    if (handleMpuPin == NULL) {
    	System_abort("Pin open failed!");
    }
    
    /* Sanity check */
    System_printf("Hello world!\n");
    System_flush();

    /* Start BIOS */
    BIOS_start();

    return (0);
}
