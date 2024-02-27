"""
Miinantallaaja lopputyö
Ville Nättinen
Ohjelmoinnin alkeet, syksy 2021

Miinaharavaa imitoiva ohjelma
Hyödyntää haravasto-kirjastoa peligrafiikoihin
Pelissä on  tekstipohjainen alkuvalikko, josta voi valita uuden pelin,
lopettamisen tai tilastojen katselemisen
Pyytää käyttäjältä uuteen peliin peliruudukon koon ja miinojen lukumäärän
Pelin päättyessä tallentaa tiedot (pelin ajankohdan, keston minuuteissa,
keston vuoroissa, lopputuloksen, kentän koon ja miinojen lukumäärän)
Myös tilastojen katseleminen tapahtuu tekstipohjaisesti
Toiminnoissa pyritty kopioimaan alkuperäisen miinaharavan ominaisuuksia (mitat, liputus, jne.)
"""

import time
import csv
from random import randint
from math import ceil
import haravasto


# Sanakirja ruudukon määrittämiseen ja pelien tallentamiseen käytettäville muuttujille
muuttujat = {
    "pvm": None,
    "aloitusaika": None,
    "lopetusaika": None,
    "vuorojen_lkm": 0,
    "tulos": None,
    "leveys": 0,
    "korkeus": 0,
    "miinojen_lkm": 0
}

# Sanakirja sisältää miinoitetun listan ja
# käyttäjälle näkyvän tyhjän listan
# Listat määritetään alusta_listat-funktiossa
tila = {
    "kentta": [],
    "nakyva": []
}

def tallennusten_tarkastelu(toistot):
    """
    Tulostaa tiedostosta parametrin "toistot" mukaisen määrän rivejä per sivu,
    kysyy käyttäjältä syötteen joka sivun välissä
    Palaa aloitusvalikkoon, kun tallennukset on selattu läpi ja käyttäjä painaa enteriä
    """

    tallennukset = tallennusten_luku()
    indeksi = 0   # Tulostettavan rivin indeksi
    sivu = 0      # Tarkastelussa olevan sivun numero
    print(
        "\nTulostetaan korkeintaan {} tallennetta per sivu, uusimmasta vanhimpaan".format(toistot)
    )
    while True:
        for i in range(toistot):
            indeksi -= 2  # Pienennetään indeksiä kahdella csv-tiedoston muotoilun vuoksi
            tallennusten_tulostus(indeksi) # Tulostetaan rivi
        sivu += 1 # Päivitetään sivunumero
        print("\nSivu {}/{}".format(sivu, ceil(len(tallennukset)/2/toistot)))
        # Tarkistetaan ollaanko saavutettu viimeinen sivu
        if sivu < ceil(len(tallennukset)/2/toistot):
            syote = input(
                "\nPaina enter tulostaaksesi lisää tallennettuja "
                "pelejä tai syötä Q poistuaksesi tallenteista: "
            ).strip().lower()
            if syote == "q":
                break
        # Kehotetaan käyttäjää palaamaan päävalikkoon, jos tallennukset on käyty läpi
        else:
            input("\nPaina enter jatkaaksesi takaisin päävalikkoon")
            break

def tallennusten_tulostus(indeksi=-2):
    """
    Tulostaa indeksin määrittämän rivin tiedostosta
    """
    
    tallennukset = tallennusten_luku()
    # Tarkastetaan että indeksi on halutuissa rajoissa
    if len(tallennukset)+indeksi >= 0:
        # Tulostetaan rivi vain jos sillä on sisältöä
        if tallennukset[indeksi]:
            print("")
            for i in range(6):
                print(tallennukset[indeksi][i])

def tallennusten_luku():
    """
    Luetaan tiedosto "pelatut_pelit.csv"
    Palautetaan tiedosto listana
    """
    
    try:
        with open("pelatut_pelit.csv") as luku:
            lukija = csv.reader(luku)
            tallennukset = list(lukija)
            return tallennukset
    except IOError:
        print("\nTallennuksia ei voitu avata")

def pelin_tallennus():
    """
    Tallennetaan pelattu peli csv-tiedostoon haluttuun muotoon
    Tallennettaan pelin ajankohta, kesto, vuorojen määrä, tulos, mitat ja miinojen määrä
    Funktiota kutsutaan, kun päättymisehto täyttyy
    """
    
    # Huolehditaan kieliopista
    if int((muuttujat["lopetusaika"]-muuttujat["aloitusaika"])/60) == 1:
        minuutit = "minuutti"
    else:
        minuutit = "minuuttia"
    
    try:
        with open("pelatut_pelit.csv", "a") as tallennukset:
            kirjoittaja = csv.writer(tallennukset)
            kirjoittaja.writerow([
                "Peli pelattu: {}".format(muuttujat["pvm"]),
                "Pelin kesto: {} {}".format(
                    int((muuttujat["lopetusaika"]-muuttujat["aloitusaika"])/60),
                    minuutit
                ),
                "Vuorojen määrä: {}".format(muuttujat["vuorojen_lkm"]),
                "Lopputulos: {}".format(muuttujat["tulos"]),
                "Ruudukon koko: {}x{}".format(muuttujat["leveys"], muuttujat["korkeus"]),
                "Miinojen määrä: {}".format(muuttujat["miinojen_lkm"])
            ])
    except IOError:
        print("\nPelitilastojen tallentaminen ei onnistunut")

def lopetus(tulos):
    """
    Toteutetaan kun päättymisehto täyttyy
    Tallentaa sanakirjaan lopetusajan,
    tarkistaa lopptuloksen ja päivittää sen perusteella "tulos"-muuttujan
    Tulostaa pelaajalle tiedot pelatusta pelistä ja kehottaa sulkemaan peliruudun
    """
    
    tila["nakyva"] = tila["kentta"]           # Näytetään pelaajalle miinat
    muuttujat["lopetusaika"] = time.time()    # Poimitaan lopetusaika
    # Jos tulos == 0, peli päättyy häviöön, 1 voittoon, muussa tapauksessa virheilmoitukseen
    if tulos == 0:
        muuttujat["tulos"] = "Häviö"
        print("\nHävisit pelin!")
        pelin_tallennus() # Tallennetaan peli
        tallennusten_tulostus() # Tulostetaan tilastot pelaajalle näkyviin
        print("\nSulje peliruudukko jatkaaksesi\n")
    elif tulos == 1:
        muuttujat["tulos"] = "Voitto"
        print("\nVoitit pelin!")
        pelin_tallennus() # Tallennetaan peli
        tallennusten_tulostus() # Tulostetaan tilastot pelaajalle näkyviin
        print("\nSulje peliruudukko jatkaaksesi\n")
    else:
        haravasto.lopeta()
        print("\nJotakin erittäin kummallista tapahtui ja peli päättyi odottamattomalla tavalla\n")


def tulvataytto(x, y):
    """
    Syöttää laske_miinat-funktiolle lisää ruutuja tarkasteltavaksi, jos ne täyttävät ehdot
    """
    
    # Käy läpi viereiset indeksit
    for (dx, dy) in [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]:
        # Tarkistaa ovatko indeksit avaamattomia sekä vieressä myös ruudukossa
        # Kutsuu tarvittaessa laske_miinat-funktiota uusilla arvoilla
        if (
            x+dx >= 0 and x+dx < muuttujat["leveys"] and
            y+dy >= 0 and y+dy < muuttujat["korkeus"] and
            tila["kentta"][y+dy][x+dx] == " "
        ):
            laske_miinat(x+dx, y+dy)

def laske_miinat(x, y):
    """
    Laskee ruudun ympärillä olevat miinat ja päivittää niiden lukumäärän
    Kutsuu tulvataytto-funktiota, jos ruutu on tyhjä
    """
    
    miinoja = 0
    # Käy läpi viereiset indeksit
    for (dx, dy) in [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]:
        # Tarkistaa ovatko indeksit vieressä myös ruudukossa ja sisältävätkö ne miinaa
        # Kasvattaa "miinoja"-muuttujaa, jos näin on
        if (
            x+dx >= 0 and x+dx < muuttujat["leveys"] and
            y+dy >= 0 and y+dy < muuttujat["korkeus"] and
            tila["kentta"][y+dy][x+dx] == "x"
        ):
            miinoja += 1
    # Päivittää ruudun näkyvään listaan ja miinoja sisältävään listaan
    tila["kentta"][y][x] = miinoja
    tila["nakyva"][y][x] = miinoja
    # Jos miinoja ei löytynyt, kutsuu tulvatäyttö-funktiota
    if miinoja == 0:
        tulvataytto(x, y)

def kasittele_hiiri(x_pikseleina, y_pikseleina, nappi, muokkausnappi):
    """
    Tätä funktiota kutsutaan kun käyttäjä klikkaa sovellusikkunaa hiirellä
    Muuttaa x:n ja y:n pikseleistä indekseiksi
    Hiiren oikeaa nappia painettaessa asettaa tai poistaa lipun
    Lisää yhden vuoron, kun painetaan hiiren vasenta nappia
    Jos ruutua ei ole aiemmin avattu tarkistaa onko ruudussa miinaa ja joko 
    lopettaa pelin häviönä tai kutsuu laske_miinat-funktiota
    Tarkistaa onko avaamattomia ruutuja jäljellä ja tarvittaessa lopettaa pelin voittona
    Toimii vain, jos pelin päättymisehto ei ole täyttynyt
    """
    
    x = int(x_pikseleina/40)  # Muuttaa x:n ja y:n arvot pikseleistä listojen indekseiksi
    y = int(y_pikseleina/40)
    # Tarkistetaan onko päättymisehto täyttynyt
    if not muuttujat["tulos"]:
        # Jos painetaan hiiren oikeaa nappia ja ruutu on tyhjä, lisätään lippu
        if nappi == haravasto.HIIRI_OIKEA and tila["nakyva"][y][x] == " ":
            tila["nakyva"][y][x] = "f"
        # Jos painetaan hiiren oikeaa nappia ja ruudussa on lippu, tyhjätään ruutu
        elif nappi == haravasto.HIIRI_OIKEA and tila["nakyva"][y][x] == "f":    
            tila["nakyva"][y][x] = " "
        # Jos painetaan hiiren vasenta nappia ja ruutu on avaamaton
        elif nappi == haravasto.HIIRI_VASEN and tila["nakyva"][y][x] == " ":
            muuttujat["vuorojen_lkm"] += 1    # Lisätään yksi vuoro
            # Lopettaa pelin häviönä, jos ruudussa on miina
            if tila["kentta"][y][x] == "x":
                lopetus(0)
            else:
                # Kutsutaan laske_miinat-funktiota
                laske_miinat(x, y)
                # Lopettaa pelin voittona, jos tyhjiä ruutuja ei ole miinoitetussa listassa
                tyhja = 0
                for i in tila["kentta"]:
                    if " " in i:
                        tyhja += 1
                if tyhja == 0:
                    lopetus(1)

def piirra_kentta():
    """
    Käsittelijäfunktio, joka piirtää kaksiulotteisena listana kuvatun miinakentän
    ruudut näkyviin peli-ikkunaan. Funktiota kutsutaan aina kun pelimoottori pyytää
    ruudun näkymän päivitystä
    """
    
    haravasto.tyhjaa_ikkuna()
    haravasto.piirra_tausta()
    haravasto.aloita_ruutujen_piirto()
    for y, l in enumerate(tila["nakyva"]):
        for x, avain in enumerate(l):
            haravasto.lisaa_piirrettava_ruutu(avain, x*40, y*40)
    haravasto.piirra_ruudut()

def miinoita(vapaat, n):
    """
    Asettaa kentälle n kpl miinoja satunnaisiin paikkoihin
    """
    
    while n:
        # Arvotaan kokonaisluvut 0 =< y =< (korkeus - 1)  ja 0 =< x =< (leveys - 1)
        x = randint(0, muuttujat["leveys"]-1)
        y = randint(0, muuttujat["korkeus"]-1)
        # Sijoitetaan miinat arvottuihin listan indekseihin, jos ne ovat vapaina
        if (x, y) in vapaat:
            tila["kentta"][y][x] = "x"
            vapaat.remove((x, y))
            n -= 1

def alusta_listat():
    """
    Luodaan kolme listaa: 
    tyhjä lista pelaajalle näkyväksi pelikentäksi,
    miinoitettu lista funktioiden käyttöön ja 
    tyhjä lista, joka kuvaa miinoittamattomia ruutuja miinoita-funktiolle
    """
    
    # Luodaan käyttäjälle näkyvä tyhjä lista
    tila["nakyva"] = []
    for rivi in range(muuttujat["korkeus"]):
        tila["nakyva"].append([])
        for sarake in range(muuttujat["leveys"]):
            tila["nakyva"][-1].append(" ")
    # Luodaan piiloon jäävä lista, johon sijoitetaan miinat
    tila["kentta"] = []
    for rivi in range(muuttujat["korkeus"]):
        tila["kentta"].append([])
        for sarake in range(muuttujat["leveys"]):
            tila["kentta"][-1].append(" ")
    # Luodaan miinoittamista varten lista, joka sisältää miinoista vapaana olevat ruudut
    jaljella = []
    for x in range(muuttujat["leveys"]):
        for y in range(muuttujat["korkeus"]):
            jaljella.append((x, y))
    # Kutsutaan miinoita-funktiota
    miinoita(jaljella, muuttujat["miinojen_lkm"])
    
def alusta_kentta():
    """
    Kysyy käyttäjältä kentän mitat (väliltä 3x3 - 30x24) ja miinojen määrän (väliltä 1-719)
    Luo näkyvän tyhjän listan, miinoitettavan listan
    ja miinoittamista varten vapaat ruudut sisältävän listan
    Käynnistää miinoita-funktion
    """
    
    # Kysytään leveys
    while True:
        try:
            muuttujat["leveys"] = int(input("\nAnna leveys kokonaislukuna väliltä 3-30: "))
            # Varmistetaan, että listan mitat ovat halutunlaiset
            if muuttujat["leveys"] < 3 or muuttujat["leveys"] > 30:
                print("\nAnna kysytty luku")
            else:
                break
        except ValueError:
            print("\nAnna kysytty luku")
    # Kysytään korkeus
    while True:
        try:
            muuttujat["korkeus"] = int(input("\nAnna korkeus kokonaislukuna väliltä 3-24: "))
            # Varmistetaan, että listan mitat ovat halutunlaiset
            if muuttujat["korkeus"] < 3 or muuttujat["korkeus"] > 24:
                print("\nAnna kysytty luku")
            else:
                break
        except ValueError:
            print("\nAnna kysytty luku")
    # Kysytään miinat
    while True:
        try:
            # Määritetään miinojen maksimimäärä annettujen mittojen perusteella
            miinat_max = muuttujat["korkeus"]*muuttujat["leveys"]-1
            # Kysytään käyttäjältä miinojen lukumäärä
            muuttujat["miinojen_lkm"] = int(input(
                "\nAnna miinojen lukumäärä väliltä 1-{}: ".format(miinat_max))
            )
            # Varmistetaan, että miinoja on haluttu määrä
            if muuttujat["miinojen_lkm"] < 1 or muuttujat["miinojen_lkm"] > miinat_max:
                print("\nAnna kysytty luku")
            else:
                break
        except ValueError:
            print("\nAnna kysytty luku")
    alusta_listat() # Kutsutaan funktiota listojen luomiseksi

def main():
    """
    Lataa pelin grafiikat, luo peli-ikkunan ja asettaa siihen piirtokäsittelijän
    Asettaa/nollaa muuttujat (pl. mitat)
    """
    
    haravasto.lataa_kuvat("spritet")
    haravasto.luo_ikkuna(muuttujat["leveys"]*40, muuttujat["korkeus"]*40)
    haravasto.aseta_piirto_kasittelija(piirra_kentta)
    haravasto.aseta_hiiri_kasittelija(kasittele_hiiri)
    # Varmistetaan ettei edellisen pelin arvot jää kummittelemaan
    muuttujat["pvm"] = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    muuttujat["aloitusaika"] = time.time()
    muuttujat["lopetusaika"] = None
    muuttujat["vuorojen_lkm"] = 0
    muuttujat["tulos"] = None
    haravasto.aloita()

if __name__ == "__main__":
    """
    Avaa aloitusvalikon.
    Käyttäjän valitessa uuden pelin kutsuu alusta_kentta-funktiota,
    jonka jälkeen käynnistää pääohjelman.
    Käyttäjän valitessa tulostuksen tulostaa tiedostosta "pelatut_pelit.csv"
    tallennukset uusimmasta vanhimpaan, oletusarvona 5 peliä per tulostettu sivu
    Käyttäjän valitessa lopetuksen, lopettaa silmukan
    Jatkaa, kunnes saa halutunlaisen syötteen
    """
    
    # Pääsilmukka, johon palataan aina toimintojen jälkeen
    while True:
        print("\n============================================================")
        print("=               Miinantallaajan aloitusvalikko             =")
        print("============================================================\n")
        print("Valitse seuraavista toiminnoista:\n")
        print("Aloittaaksesi uuden pelin:                                 A")
        print("Tarkastellaksesi aiemmin pelattuja pelejä:                 T")
        print("Lopettaaksesi ohjelman:                                    Q\n")
        valinta = input("Valintasi: ").strip().lower()
        # Luo kentän ja aloittaa pääohjelman eli pelin
        if valinta == "a":
            alusta_kentta()
            main()
        # Kutsuu tallennusten_tarkastelu-funktiota 
        # Asettaa parametrin "tulostettavat tallennukset per sivu"
        elif valinta == "t":
            tallennusten_tarkastelu(5)
        # Lopettaa ohjelman
        elif valinta == "q":
            break
        else:
            print("\nValitsemaasi toimintoa ei ole olemassa")
