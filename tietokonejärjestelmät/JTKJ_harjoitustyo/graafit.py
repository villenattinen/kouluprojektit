"""
Piirtää graafeja sensortagin mpu-sensorin mittausarvoista, arvot mpu_data.csv-tiedostossa muotoa
time,ax,ay,az,gx,gy,gz

Nättinen Ville, 2021
"""
import matplotlib.pyplot as plt
import csv

ax_lista = []
ay_lista = []
az_lista = []
gx_lista = []
gy_lista = []
gz_lista = []
time_lista = []

def arvojen_luku():
    try:
        with open("mpu_data.csv") as luku:
            lukija = csv.reader(luku)
            tallennukset = list(lukija)

        for i in range(1,102): #otetaan 100 ensimmäistä mittaustulosta
            if tallennukset[i]:    
                    time_lista.append(tallennukset[i][0])
                    ax_lista.append(tallennukset[i][1])
                    ay_lista.append(tallennukset[i][2])
                    az_lista.append(tallennukset[i][3])
                    gx_lista.append(tallennukset[i][4])
                    gy_lista.append(tallennukset[i][5])
                    gz_lista.append(tallennukset[i][6])
    except IOError:
        print("\nTiedostoa ei voitu avata")

                
        tiedosto.close()

def tulosta_graafi():

    y1 = list(map(float, ax_lista)) 
    y2 = list(map(float, ay_lista))
    y3 = list(map(float, az_lista))
    y4 = list(map(float, gx_lista))
    y5 = list(map(float, gy_lista))
    y6 = list(map(float, gz_lista))
    x = list(map(float,time_lista))

    fig, axs = plt.subplots(2)  
    
    plt.ylim([-1.5, 1.5])
    axs[0].plot(x, y1, label='ax')  
    axs[0].plot(x, y2, label='ay') 
    axs[0].plot(x, y3, label='az') 
    axs[0].set_xlabel('time')
    axs[0].set_ylabel('acceleration')  
    axs[0].set_title("MPU acc data") 
    plt.ylim([-250, 250])
    axs[1].plot(x, y4, label='gx')  
    axs[1].plot(x, y5, label='gy') 
    axs[1].plot(x, y6, label='gz') 
    axs[1].set_xlabel('time') 
    axs[1].set_ylabel('gyro') 
    axs[1].set_title("MPU gyro data") 
    plt.show()  


if __name__ == "__main__":
    arvojen_luku()
    tulosta_graafi()