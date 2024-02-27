# Linkitetyt listat tehtävä 2

Tietorakenteet ja algoritmit

## Tavoite

Tämän tehtävän tavoitteena on testata linkitettyä listaa käyttämällä sitä johonkin käytännölliseen tehtävään. Toteutat linkitetyn listan rajapintaan ominaisuuden jolla listan järjestys saadaan käännettyä päinvastaiseksi. Tällaistahan sovelluksissa usein tehdään. Esimerkiksi erilaisissa käyttöliittymissä voidaan esittää tietoja lajitetuna tiettyyn järjestykseen ja muuttaa järjestys päinvastaiseksi. 

Toinen tavoite on verrata oman linkitetyn listasi suoritusnopeutta Javan `ArrayList` ja `LinkedList` -toteutuksiin. Kun teet vertailuja, kysy mielessäsi, **mistä erot johtuvat**? 

**TÄRKEÄÄ:** huomaa että tämän pohdintaan suoritusnopeuksista liittyviä asioita voidaan kysyä **kurssin tentissä**. Tee tehtävä siis ajatuksen kanssa ja tutustu ohjeiden mukaisesti eri toteutuksiin!

## Edeltävyydet

Olet tehnyt tämän harjoituksen ensimmäisen tehtävän jossa toteutit linkitetyn listan luokkaan `LinkedListImplementation` ja se toimii suorittamiesi testien `ListTests` perusteella.

## Ohjeet

Tässä harjoituksessa käytetään `LinkedListImplementation` -listaa joka käsittelee merkkijonoja (`String`). Testit lukevat pitkän listan jossa erilaisia rypäleitä, tiedostosta. Rypäleet on listattu tiedostossa aakkosjärjestykseen. Tehtäväsi on **toteuttaa** metodi `LinkedListImplementation.reverse()` siten että se kääntää listan järjestyksen päinvastaiseksi. Toteutuksen tulee olla "in place".

"In place" tarkoittaa että järjestyksen kääntämistä varten ei saa luoda uusia tietosäiliöitä kuten taulukoita, listoja tai pinoja tai muutakaan johon elementit laitetaan päinvastaiseen järjestykseen. Kääntäminen täytyy tehdä liikumalla listaa eteenpäin ja kääntämällä listan solmujen linkit toiseen suuntaan. 

Esimerkiksi, jos listan järjestys olisi: `A -> B -> C -> D`, se on järjestyksen kääntämisen jälkeen `A <- B <- C <- D`. Ja lopuksi vielä asetetaan listan ensimmäiseksi elementiksi (`head`) D, listasta tulee käytännössä `D -> C -> B -> A`.

**Avaa** testitiedosto `ReorderListTests.java`. Näet että se sisältää yksikkötestejä jotka lukevat testidatan tiedostosta ja tarkistavat että listan järjestys on päinvastainen `reverse()` -metodin kutsun jälkeen.

> Huom: on mahdollista toteuttaa järjestyksen kääntäminen esimerksi pinoa (Stack) hyödyntäen. Tämä toteutus kuitenkin kuluttaa tuplasti muistia elementtien määrää kohti. Jokainen listan elementti työnnetään pinoon ja sen jälkeen otetaan pinosta elementit ja lisätään ne listalle. Näin järjestys kääntyy. Älä tee tätä, koska emme halua tuplata tietorakenteen muistinkäyttöä n:n suhteen vaan toteutetaan tämä, kuten mainittu, "in place".

Jotta voit pohtia ja vertailla toteutuksesi nopeutta suhteessa Javan `ArrayList` ja `LinkedList` luokkien välillä, **tutustu** seuraaviin Javan lähdekooditiedostoihin:

* Java ArrayList: https://github.com/openjdk/jdk15u-dev/blob/master/src/java.base/share/classes/java/util/ArrayList.java
* Java LinkedList: https://github.com/openjdk/jdk15u-dev/blob/master/src/java.base/share/classes/java/util/LinkedList.java
* Java Collections: https://github.com/openjdk/jdk15u-dev/blob/master/src/java.base/share/classes/java/util/Collections.java

Javan `Collections.reverse()` -metodia käytetään testeissä kääntämään listojen / taulukoiden järjestys.

**Vertaa** miten Javan luokissa metodit `add()`, `get(int)` ja `indexOf(E)` suhteutuvat omaan toteutukseesi, ja miten nämä erot voisivat selittää oman toteutuksesi ja näiden Javan metodien nopeuseroja.

## Testaus

**Suorita ReorderListTests testit** ja varmista että toteutuksesi menevät läpi tästä yksikkötestistä. Komentoriviltä (hakemistossa jossa harjoituksen `pom.xml` on) :

```console
mvn -Dtest=ReorderListTests test
```

Jos testit eivät mene läpi, näet virheilmoituksia. 

Tässä vaiheessa voit myös suorittaa *kaikki* harjoituksen testit:

```
mvn test
```

Varsinkin jos joudut muuttamaan toteutusta luokassa `LinkedListImplementation`, on tarpeen tehdä *regressiotestausta* (eli suorittaa uudestaan jo suoritettuja testejä).

Kun teet tätä tehtävää, **älä**:

* muuta rajapintaa `LinkedListInterface` millään tavoin,
* muuta yksikkötestejä millään tavoin,
* muuta testiaineistotiedostoa `Grapes.txt` millään tavoin.

## Toimitus arvioitavaksi

Kun testit menevät läpi, toimita harjoitus arvioitavaksi kurssilla esitetyllä tavalla.

## Kysymyksiä tai ongelmia?

Osallistu luennoille, harjoituksiin ja hyödynnä kurssin keskustelufoorumia.

Jos on ongelmia työkalujen kanssa, tarkista että sinulla on oikea JDK asennettu ja käytössä, ympäristömuuttujat ovat kohdallaan ja Maven on asennettu ja toimii.

## Tietoja

* Tietorakenteet ja algoritmit 2021-2022.
* Tietojenkäsittelytieteet, Oulun yliopisto.
* Antti Juustila, INTERACT Research Group.