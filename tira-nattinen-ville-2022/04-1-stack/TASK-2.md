# Pino -harjoituksen osa 2

Tietorakenteet ja algoritmit 2022.

## Tavoite

Tämän harjoituksen tavoitteena on testata ja hyödyntää toteuttamaasi pinotietorakennetta käytännössä hyödylliseen tehtävään. Käytät pinoa tarkistamaan onko annetuissa rakenteellista tekstiä (structured text) sisältävien tiedostojen sulut (parentheses) tasapainossa.

Esimerkiksi, tämä on oikeellista tekstiä sulkuja tarkasteltaessa. Tämä JSON -teksti on haettu [Linnanmaan sääasemalta](http://weather.willab.fi/weather.json):

```JSON
{
  "tempnow": 1.7,
  "temphi": 7.4,
  "templo": -1.6,
  "dewpoint": 1.4,
  "humidity": 98,
  "airpressure": 1005.1,
  "windspeed": 3.3,
  "windspeedmax": 7.6,
  "winddir": 174,
  "precipitation1d": 0,
  "precipitation1h": 0,
  "timestamp": "2021-10-14 11:11 EEST"
}
```
Tässä aloitussulkua `{` vastaa yksi lopetussulku `}`. Jos jompia kumpia sulkuja olisi liikaa, teksti ei olisi siinä mielessä oikeellista.

Tässä Java -koodinpätkässä sen sijaan on ongelma. Koodissa on yksi ylimääräinen sulkeva sulku `}` ennen viimeistä riviä:

```Java
  if (currentIndex >= capacity - 1) {
    reallocateInternalArray();
    }
  }
  itemArray[++currentIndex] = element;         
```
Koodisi jonka toteutat tutkimaan onko tällainen teksti sulkujen mielessä oikeellista, pitää huomata tällaiset ongelmat annetusta tekstistä. Tähän käytetään pinotietorakennetta johon sulkumerkkejä laitetaan ja niitä otetaan pois.

## Edeltävät tehtävät

Olet toteuttanut pinotietorakenteen luokkaan `StackImplementation` ja testit testiluokassa `StackTests` menevät läpi.

## Ohjeet

Tässä harjoituksessa käytetään luokkaa `StackImplementation` joka sisältää (tehtävän 1 kokonaislukujen sijaan) merkkitietoa (`Character`). 

Samaan tapaan kuin `StackFactory.createIntegerStack()` luo pinon kokonaislukuja, **toteuta** `StackFactory` -luokkaan toinen metodi joka luo pinon johon voidaan laittaa `Character` tietoa.

**Avaa** testiluokka `ParenthesisTests.java`. Huomaat että se sisältää yksikkötestejä jotka testaavat ovatko sulut kohdallaan kahdessa testitiedostossa. Testit kutsuvat metodia `ParenthesisChecker.checkParentheses()` jossa varsinainen tarkistus tehdään.

**Sinun tehtäväsi** on **toteuttaa metodi `ParenthesisChecker.checkParentheses()`** siten että se hyödyntää pinon toteutusta `StackImplementation` jonka juuri viimeistelit.

Testiaineiston joukossa on kaksi tiedostoa hakemistossa `src/test/resources`. Tiedosto `SSN.java` on sulkujen suhteen oikeellinen, mutta `Person.json` tiedosto ei ole. Siksi testi joka testaa toista .json -tiedostoa odottaa että koodisi heittää tämän tiedoston kanssa poikkeuksen:

```Java
   assertThrows(ParenthesesException.class, () -> checkParentheses(toCheck), "Person.json is invalid JSON so must throw");
```
Eli `ParenthesisChecker.checkParentheses()` pitäisi huomata että sulut eivät ole tässä tiedostossa kunnossa. Jos koodisi *ei heitä* tätä poikkeusta testi epäonnistuu koska näin pitäisi käydä.

**Seuraa ohjeita** jotka löydät metodin `ParenthesisChecker.checkParentheses()` kommenteissa, ja toteuta metodi oikein, hyödyntäen toteuttamaasi pinotietorakennetta. 

## Testaus

**Suorita testit ParenthesisTests testiluokassa** ja varmista että toteutuksesi läpäisee testit. Voit suorittaa testit komentorivillä (hakemistossa jossa harjoituksen `pom.xml` tiedosto on):

```
mvn -Dtest=ParenthesisTests test
```

Jos toteutuksesi ei toimi oikein, testit eivät mene läpi. 

Voit tässä vaiheessa myös suorittaa kaikki harjoituksen testit:

```
mvn test
```

Erityisesti jos olet *korjaillut* `StackImplementation` -toteutustasi kun ehkä olet huomannut siellä virheitä tätä tehtävää tehdessäsi. On siis syytä suorittaa *regressiotestausta* (ajaa jo aiemmin tehtyjä testejä uudelleen).

Kun toteutat harjotuksen koodia, **älä**:

* muuta rajapintaluokkaa `StackInterface` millään tavoin,
* muuta yksikkötestikoodia millään tavoin,
* muuta tiedostoja `SSN.java` tai `Person.json` millään tavoin.
* yritä päästä vähällä huijaamalla testejä heittäen poikkeuksia kun niitä odotetaan tai olla heittämättä jos niitä ei odoteta. Koodia tarkastetaan arvioinnin yhteydessä myös käsin.

## Toimitus arvioitavaksi

Toimita toteutus etärepoosi git:n avulla kuten kurssilla on ohjeistettu.

## Kysymyksiä tai ongelmia?

Osallistu kurssin luennoille, harjoituksiin ja verkkofoorumeille, kysy apua ja ohjeita.

## Tietoja

* Kurssimateriaalia Tietorakenteet ja algoritmit -kurssille | Data structures and algorithms 2021-2022.
* Tietojenkäsittelytieteet, Tieto- ja sähkötekniikan tiedekunta, Oulun yliopisto.
* (c) Antti Juustila 2021-2022, INTERACT Research Group.