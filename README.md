# Lélegzetnapló

Egyszerű, magyar nyelvű Java konzolprogram a légzőgyakorlatok feljegyzésére. A
felhasználó kiválaszt egy légzési mintát, megadja az időtartamot, majd a napló
bejegyzéseit később visszanézheti vagy törölheti.

Ez egy Programozás 2 beadandó, és egyben gyakorló alkalmazás. Nem mér pulzust,
nem használ kamerát, és nem ad egészségügyi tanácsot.

## Mire jó?

- Új légzésgyakorlat rögzítése egy megjegyzéssel.
- Két egyszerű minta kiválasztása: nyugtató 4–6 és egyenletes 4–4.
- A napló megtekintése időrendben.
- Bejegyzés törlése az azonosítója alapján.
- Adatok elmentése az adatok/legzesnaplo.tsv fájlba, így a program újraindítása
  után is megmaradnak.

## Előkészületek

- Java Development Kit 21.
- Apache Maven 3.9 vagy újabb.

Az IntelliJ IDEA használható szerkesztésre és futtatásra. A projekt mappájának
megnyitásakor az IDE felismeri a Maven pom.xml fájlját.

## Fordítás és indítás

A projekt gyökérmappájában futtasd:

    mvn clean test
    mvn package
    java -jar target/lelegzet-naplo-1.0.0.jar

Windows alatt ugyanazokat a parancsokat a PowerShellben vagy a Windows
Terminálban lehet kiadni.

## Minta a program használatára

1. Válaszd az Új bejegyzés lehetőséget.
2. Válassz egy légzési mintát.
3. Add meg az időtartamot percben. Ez 1 és 120 közé eshet.
4. Írhatsz rövid megjegyzést, például: „tanulás után”.
5. A Napló menüpontban megjelenik a dátum, a választott minta és a becsült
   teljes légzési ciklusok száma.

## Objektumorientált megoldás

- **Encapsulation:** a LegzesiMunkamenet mezői privátak és nem módosíthatók
  létrehozás után.
- **Abstraction:** a LegzesiStrategia és MunkamenetTarolo interfészek rögzítik
  a használható műveleteket.
- **Inheritance és polymorphism:** a NyugtatoStrategia és EgyenletesStrategia
  ugyanazt az interfészt valósítja meg, a munkamenet pedig az interfészen
  keresztül használja a kiválasztott viselkedést.
- **Composition:** minden LegzesiMunkamenet pontosan egy légzési stratégiát
  tartalmaz. A szolgáltatás nulla vagy több munkamenetet kezel.
- **Collections, generics és sorting:** az alkalmazás ArrayList<LegzesiMunkamenet>
  listát használ, a megjelenítés pedig Comparatorral rendezi az elemeket.
- **Exceptions:** az ErvenytelenMunkamenetException jelzi a hibás időtartamot
  vagy megjegyzést. A fájlból származó hibákat az alkalmazás olvasható üzenetté
  alakítja.
- **Persistence:** a FajlMunkamenetTarolo UTF-8 TSV fájlt használ, és try-with-
  resources szerkezettel zárja a fájlokat.

## Tervezési minta: Strategy

A LegzesiStrategia az azonos műveletsort leíró szerződés. A
NyugtatoStrategia és az EgyenletesStrategia két külön megvalósítást ad. Egy
LegzesiMunkamenet az interfészt kapja meg, ezért a ciklusszámítás a tényleges
futáskor a kiválasztott stratégiához igazodik. Új légzési minta később új
megvalósításként hozzáadható a meglévő munkamenet-kódjának átírása nélkül.

## Ellenőrzés

A tesztek a domain szabályokat, a fájlba mentést és visszatöltést, valamint a
szolgáltatás létrehozási és törlési műveleteit ellenőrzik:

    mvn test

## Tervezési dokumentumok

- [Felhasználói történetek](docs/felhasznaloi_tortenetek.md)
- [UML osztálydiagram és mintamagyarázat](docs/uml.md)
- [Részletes kódmagyarázat kezdőknek](docs/kodmagyarazat.md)

## Fő korlátok

- A bejegyzés a felhasználó által megadott naplóadat; nincs érzékelő vagy
  automatikus mérés.
- A program egyetlen helyi TSV fájlt használ, több felhasználót nem kezel.
- A minták bemutató jellegűek, nem jelentenek személyre szabott útmutatást.

## Közreműködő

- islxpyz — szerző
