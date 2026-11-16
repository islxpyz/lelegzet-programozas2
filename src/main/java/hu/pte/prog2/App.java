package hu.pte.prog2;

import hu.pte.prog2.adattar.FajlMunkamenetTarolo;
import hu.pte.prog2.adattar.MunkamenetTarolo;
import hu.pte.prog2.felulet.KonzolosFelulet;
import hu.pte.prog2.szolgaltatas.MunkamenetSzolgaltatas;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class App {
    private static final Path NAPLO_UTVONALA = Path.of("adatok", "legzesnaplo.tsv");

    public static void main(String[] parancssoriArgumentumok) {
        try {
            MunkamenetTarolo tarolo = new FajlMunkamenetTarolo(NAPLO_UTVONALA);
            MunkamenetSzolgaltatas szolgaltatas = new MunkamenetSzolgaltatas(tarolo);

            try (Scanner beolvaso = new Scanner(System.in)) {
                KonzolosFelulet felulet = new KonzolosFelulet(szolgaltatas, beolvaso);
                felulet.indit();
            }
        } catch (IOException hiba) {
            System.out.println("A naplófájlt nem sikerült megnyitni: " + hiba.getMessage());
        }
    }
}

