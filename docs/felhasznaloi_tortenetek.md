# Felhasználói történetek

## Probléma és célközönség

A program annak segít, aki szeretné egyszerűen feljegyezni, hogy mikor és
milyen légzési mintát gyakorolt. A feljegyzések nem vesznek el a program
bezárásakor, és később visszanézhetők. A program nem végez mérést, és nem ad
egészségügyi tanácsot.

## Történet 1: gyakorlat feljegyzése

Felhasználóként szeretnék választani a két légzési minta közül, megadni a
gyakorlás hosszát, és hozzáfűzni egy rövid megjegyzést, hogy később emlékezzek
az alkalomra.

**Elfogadási feltételek**

- A felhasználó választhat nyugtató 4–6 és egyenletes 4–4 minta közül.
- Az időtartam 1 és 120 perc közötti egész szám.
- A megjegyzés lehet üres, de legfeljebb 100 karakter.
- A program a mentés után azonnal visszajelzi az új azonosítót.

## Történet 2: korábbi alkalmak áttekintése

Felhasználóként szeretném látni a korábbi bejegyzéseimet, a hozzájuk tartozó
időponttal, időtartammal és légzési mintával.

**Elfogadási feltételek**

- A bejegyzések legújabbtól a legrégebbi felé jelennek meg.
- Üres napló esetén a program ezt külön jelzi.
- Az összegzés megmutatja a bejegyzések számát és az összes feljegyzett percet.
- A program újraindítása után a korábban elmentett bejegyzések megmaradnak.

## Történet 3: téves bejegyzés javítása

Felhasználóként szeretnék törölni egy tévesen rögzített bejegyzést az
azonosítója alapján.

**Elfogadási feltételek**

- A törlés előtt látható a napló és benne az azonosító.
- A program jelzi, ha az azonosító nem létezik.
- Sikeres törlés után a bejegyzés azonnal eltűnik, és a fájlból is törlődik.

