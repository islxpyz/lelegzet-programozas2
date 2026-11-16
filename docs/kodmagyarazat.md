# A kód részletes magyarázata

Ez az útmutató a Java előzetes ismerete nélkül is végigkövethető. A Java
kulcsszavai és néhány szabványos könyvtári név angol, de az alkalmazás saját
változónevei, osztályai és a képernyőn megjelenő szövegek magyarok.

## 1. Hogyan indul el a program?

Belépési pont: src/main/java/hu/pte/prog2/App.java, main metódus.

Az App először megadja a naplófájl helyét: az alkalmazás munkamappáján belüli
adatok/legzesnaplo.tsv fájlt. Ezután létrehoz egy FajlMunkamenetTarolo objektumot,
majd ezt a tárolót átadja a MunkamenetSzolgaltatas példányának. A szolgáltatás
betölti a korábbi bejegyzéseket. Végül az App létrehozza a
KonzolosFeluletet, amely megkapja a szolgáltatást és a billentyűzetről olvasó
Scannert.

Az objektum átadását függőségátadásnak nevezzük: a konzolos felület nem maga
hozza létre a fájltárolót, csak a szolgáltatáson keresztül kéri annak
műveleteit. Az App zárja le a Scanner objektumot a try-with-resources
szerkezettel. Ha a napló nem olvasható vagy hibás, a program egy rövid hibát ír
ki.

## 2. Maven beállítás: pom.xml

pom.xml a projekt gyökerében található. Ez a fájl leírja, hogyan kell
lefordítani a programot.

- A groupId és artifactId a projekt azonosítói.
- A version a beadandó verziója, most 1.0.0.
- A maven.compiler.release értéke 21, vagyis a kód Java 21-hez fordul.
- A JUnit csak a tesztfuttatáshoz kell; az elkészült program futásához nincs
  külső könyvtárra szükség.
- A Maven Compiler Plugin fordítja a Java forrásokat.
- A Maven Surefire Plugin futtatja a JUnit teszteket.
- A Maven Jar Plugin végrehajtható JAR fájlt készít, és az App osztályt
  beállítja belépési pontnak.

## 3. Légzési minta: interfész és két megvalósítás

Fájlok:

- domain/LegzesiStrategia.java
- domain/NyugtatoStrategia.java
- domain/EgyenletesStrategia.java
- domain/LegzesiStrategiak.java

A LegzesiStrategia interfész szerződés. Felsorolja azokat a metódusokat, amelyet
minden légzési minta megad: egy fájlazonosítót, egy képernyőn használható nevet,
a be- és kilégzés hosszát, valamint rövid leírást.

A NyugtatoStrategia a 4 másodperc be és 6 másodperc ki mintát adja. Az
EgyenletesStrategia 4 másodperc be és 4 másodperc ki értéket ad. Mindkét osztály
ugyanazokat a metódusokat írja felül, de eltérő értékeket ad vissza. Ez a
futásidejű polimorfizmus: a hívó kód az interfészt használja, a konkrét
viselkedést pedig az objektum típusa határozza meg.

A LegzesiStrategiak osztály egy egyszerű gyár. A betöltött fájl csak a minta
szöveges azonosítóját tartalmazza; az azonositoAlapjan metódus ebből új,
megfelelő stratégiaobjektumot készít. Ismeretlen azonosító esetén
IllegalArgumentException kivételt dob, amit a fájltároló kezel.

## 4. Egy naplóbejegyzés: LegzesiMunkamenet

Fájl: domain/LegzesiMunkamenet.java.

Az osztály egyetlen feljegyzett alkalmat modellez. Privát, final mezői:

- azonosito: a naplóban egyedi egész szám;
- idopont: az alkalom rögzítésének dátuma és ideje;
- percek: a felhasználó által megadott időtartam;
- strategia: az adott alkalomhoz választott LegzesiStrategia;
- megjegyzes: opcionális, rövid szöveg.

A final azt jelenti, hogy a mező értékét a konstruktor beállítja, és a későbbi
kód nem cserélheti le. A mezők private láthatósága megakadályozza, hogy más
osztályok érvénytelen állapotot írjanak beléjük.

A konstruktor ellenőrzi, hogy az azonosító pozitív, az időpont megadott, a
hossz 1–120 perc közötti, és a stratégiát kiválasztották. A megjegyzést
levágja a széleiről, az elválasztó tabulátort és sortörést szóközzé alakítja,
majd ellenőrzi a 100 karakteres korlátot. Bármely szabály megsértésekor a saját
ErvenytelenMunkamenetException kivételt dobja. Így hibás objektum nem jön létre.

A get kezdetű metódusok ellenőrzött hozzáférést adnak a mezőkhöz. A
getCiklusokSzama metódus kiszámítja a percet másodpercre váltó időt, majd
elosztja egy légzési ciklus hosszával. Például a 4–6-os mintánál egy ciklus
10 másodperc. A számítás egész osztást használ, ezért csak a teljes ciklusokat
számolja.

## 5. Saját kivétel: ErvenytelenMunkamenetException

Fájl: domain/ErvenytelenMunkamenetException.java.

Ez egy ellenőrzött kivétel, mert az Exception osztályból származik. A
LegzesiMunkamenet hozza létre, amikor a kapott adatok nem felelnek meg a domain
szabályainak. A hibaüzenetet a konzolos felület jeleníti meg. A hiba
visszajelzéssé alakul; nem hoz létre hibás naplóbejegyzést.

## 6. A mentés absztrakciója: MunkamenetTarolo

Fájl: adattar/MunkamenetTarolo.java.

Az interfész két műveletet ír elő:

- betolt: munkamenetek listáját adja vissza a tárolóból;
- mentes: a kapott listát elmenti.

Mindkettő IOException kivételt jelezhet. A szolgáltatás ezt az interfészt
ismeri, nem a konkrét fájlkezelő osztályt. Később más tároló is készülhet
ugyanezen műveletekkel anélkül, hogy a szolgáltatást át kellene írni.

## 7. Fájlműveletek: FajlMunkamenetTarolo

Fájl: adattar/FajlMunkamenetTarolo.java.

A konstruktor eltárolja a fájl elérési útját.

### Betöltés

1. Létrehoz egy üres ArrayList listát.
2. Ha a fájl még nincs meg, az üres listát adja vissza.
3. A BufferedReader UTF-8 kódolással, try-with-resources szerkezetben olvas.
4. Minden sor tabulátor mentén legfeljebb öt mezőre válik: azonosító,
   időpont, percek száma, stratégiaazonosító, megjegyzés.
5. A szöveges számot egész számmá, a dátumot LocalDateTime értékké alakítja.
   A stratégia gyár a stratégiaazonosítóból visszaállítja a megfelelő típust.
6. A konstruktor ellenőrzi a beolvasott értékeket is, így a hibás fájladat sem
   kerülhet érvényes objektumba.
7. Hibás adatnál IOException keletkezik a sor számával. A try-with-resources
   automatikusan bezárja a fájlt akkor is, ha olvasás közben hiba történik.

### Mentés

A mentes metódus létrehozza a célmappát, ha még hiányzik. Utána
BufferedWriter segítségével soronként kiírja a mezőket UTF-8 kódolással.
Minden bejegyzés végére új sort tesz. A fájl lezárását itt is a
try-with-resources intézi.

Példa egy fájlsorra:

    7    2026-11-16T18:30    5    nyugtato    Tanulás után

A mezőket a fájl ténylegesen tabulátor választja el.

## 8. Alkalmak kezelése: MunkamenetSzolgaltatas

Fájl: szolgaltatas/MunkamenetSzolgaltatas.java.

Ez az osztály köti össze a felületet és a tárolót. Mezői a MunkamenetTarolo,
az aktuális ArrayList lista, és a következő azonosító.

- A konstruktor betölti a listát, majd megkeresi a legnagyobb azonosítót. A
  következő azonosító ennek az értéke plusz egy.
- Az ujMunkamenet létrehozza az objektumot. A lista módosítása után azonnal
  ment. Ha a fájlírás nem sikerül, eltávolítja az új elemet a memóriabeli
  listából, és továbbadja az IOException kivételt. Ez visszaállítja a korábbi
  állapotot.
- A getMunkamenetekIdorendben egy másolatot készít, és Comparator segítségével
  a legújabb időpontot teszi előre. Az eredeti lista sorrendje nem változik.
- A torol megkeresi az azonosítót. Ha nincs találat, false értékkel tér vissza.
  Ha van, törli és menti. Mentési hiba esetén az elemet visszahelyezi.
- A getMunkamenetekSzama a menüben megjelenő elemszámot adja vissza.
- A kovetkezoAzonositoKiszamitasa segédmetódus csak a konstruktorból használható.

## 9. A menü és a felhasználói műveletek

Fájl: felulet/KonzolosFelulet.java.

A konstruktor a szolgáltatást és a billentyűzetet olvasó Scannert kapja meg.
Az indit metódus egy while ciklusban kiírja a fejléces menüt, egy sort olvas,
majd switch szerkezettel kiválasztja a kért műveletet. A 0 érték állítja le a
ciklust.

- ujMunkamenet előbb légzési mintát kér, utána időtartamot és megjegyzést.
  Ezután meghívja a szolgáltatás mentő műveletét. A saját domain kivételt és a
  fájl hibáját külön, érthető üzenetként írja ki.
- strategiaValasztasa újra kérdez, amíg érvényes menüpontot nem kap. A 0
  választás visszatér a főmenübe.
- egeszSzamBeolvasasa addig próbálkozik, amíg egész számot nem kap.
- naploMegjelenitese elkéri az időrendbe rendezett másolatot, majd soronként
  megjeleníti. Üres lista esetén külön üzenetet ír ki.
- munkamenetTorlese megjeleníti a listát, bekéri az azonosítót, majd a
  szolgáltatásra bízza a törlést és a mentést.
- osszegzesMegjelenitese összeadja a feljegyzett perceket, és megmutatja a
  darabszámot is.
- munkamenetSor egy naplóelem olvasható szövegét építi fel. A megjegyzést csak
  akkor teszi hozzá, ha az nem üres.
- kiirFejlec kirajzolja a menü tetejét és a bejegyzések számát.

A beolvasás mindenhol nextLine metódussal történik. Ez egyszerűbbé teszi a
számként kapott válaszok és a szöveges megjegyzések egymás utáni kezelését.

## 10. Tesztek

A tesztforrások a src/test/java mappában vannak. A JUnit @Test jelölése mondja
meg, mely metódust futtassa a tesztkeret.

- LegzesiMunkamenetTest ellenőrzi, hogy a nulla perces bejegyzés kivételt dob,
  és a két stratégia valóban eltérő ciklusszámot ad.
- FajlMunkamenetTaroloTest elment egy bejegyzést ideiglenes fájlba, visszaolvassa,
  és ellenőrzi a fontos mezőket. Azt is ellenőrzi, hogy hiányzó fájl üres listát
  jelent.
- MunkamenetSzolgaltatasTest az újraindítás utáni visszatöltést és a törlést
  vizsgálja.

Az ideiglenes fájlokat a JUnit TempDir kezeli; a valódi adatok mappát a tesztek
nem írják.

## 11. Fordítás, teszt és indítás

A projekt gyökérmappájában:

    mvn clean test
    mvn package
    java -jar target/lelegzet-naplo-1.0.0.jar

A program saját adatait az adatok/legzesnaplo.tsv fájlba menti. Ez a mappa
szerepel a .gitignore fájlban, ezért a személyes próbaadat nem kerül a
GitHubra.

