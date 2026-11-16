package hu.pte.prog2.felulet;

import hu.pte.prog2.domain.EgyenletesStrategia;
import hu.pte.prog2.domain.ErvenytelenMunkamenetException;
import hu.pte.prog2.domain.LegzesiMunkamenet;
import hu.pte.prog2.domain.LegzesiStrategia;
import hu.pte.prog2.domain.NyugtatoStrategia;
import hu.pte.prog2.szolgaltatas.MunkamenetSzolgaltatas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class KonzolosFelulet {
    private static final DateTimeFormatter IDOPONT_FORMATUM =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final MunkamenetSzolgaltatas szolgaltatas;
    private final Scanner beolvaso;

    public KonzolosFelulet(MunkamenetSzolgaltatas szolgaltatas, Scanner beolvaso) {
        this.szolgaltatas = szolgaltatas;
        this.beolvaso = beolvaso;
    }

    public void indit() {
        boolean fut = true;
        while (fut) {
            kiirFejlec();
            System.out.println("  1. Új bejegyzés");
            System.out.println("  2. Napló megtekintése");
            System.out.println("  3. Bejegyzés törlése");
            System.out.println("  4. Rövid összegzés");
            System.out.println("  0. Kilépés");
            System.out.print("\nVálasztás: ");

            String valasz = beolvaso.nextLine().trim();
            switch (valasz) {
                case "1":
                    ujMunkamenet();
                    break;
                case "2":
                    naploMegjelenitese();
                    break;
                case "3":
                    munkamenetTorlese();
                    break;
                case "4":
                    osszegzesMegjelenitese();
                    break;
                case "0":
                    fut = false;
                    System.out.println("\nA bejegyzések el vannak mentve. Viszlát!");
                    break;
                default:
                    System.out.println("\nEzt a menüpontot nem ismerem.");
            }
        }
    }

    private void ujMunkamenet() {
        LegzesiStrategia strategia = strategiaValasztasa();
        if (strategia == null) {
            return;
        }

        int percek = egeszSzamBeolvasasa("Időtartam percben (1–120): ");
        System.out.print("Megjegyzés (legfeljebb 100 karakter, lehet üres): ");
        String megjegyzes = beolvaso.nextLine();

        try {
            LegzesiMunkamenet munkamenet =
                    szolgaltatas.ujMunkamenet(percek, strategia, megjegyzes);
            System.out.println("\nA bejegyzés elmentve. Azonosító: "
                    + munkamenet.getAzonosito());
        } catch (ErvenytelenMunkamenetException hiba) {
            System.out.println("\nNem sikerült elmenteni: " + hiba.getMessage());
        } catch (IOException hiba) {
            System.out.println("\nA naplófájlt nem sikerült elmenteni: " + hiba.getMessage());
        }
    }

    private LegzesiStrategia strategiaValasztasa() {
        LegzesiStrategia nyugtatoStrategia = new NyugtatoStrategia();
        LegzesiStrategia egyenletesStrategia = new EgyenletesStrategia();

        while (true) {
            System.out.println("\nVálassz légzési mintát:");
            System.out.println("  1. " + nyugtatoStrategia.getNev()
                    + " — " + nyugtatoStrategia.getLeiras());
            System.out.println("  2. " + egyenletesStrategia.getNev()
                    + " — " + egyenletesStrategia.getLeiras());
            System.out.println("  0. Vissza");
            System.out.print("Választás: ");
            String valasz = beolvaso.nextLine().trim();

            if ("1".equals(valasz)) {
                return nyugtatoStrategia;
            }
            if ("2".equals(valasz)) {
                return egyenletesStrategia;
            }
            if ("0".equals(valasz)) {
                return null;
            }
            System.out.println("Kérlek, az 1, 2 vagy 0 lehetőséget válaszd.");
        }
    }

    private int egeszSzamBeolvasasa(String kerdes) {
        while (true) {
            System.out.print(kerdes);
            String szoveg = beolvaso.nextLine().trim();
            try {
                return Integer.parseInt(szoveg);
            } catch (NumberFormatException hiba) {
                System.out.println("Egész számot írj be, például 5-öt.");
            }
        }
    }

    private void naploMegjelenitese() {
        List<LegzesiMunkamenet> munkamenetek = szolgaltatas.getMunkamenetekIdorendben();
        if (munkamenetek.isEmpty()) {
            System.out.println("\nA napló még üres.");
            return;
        }

        System.out.println("\n--- Legutóbbi bejegyzések ---");
        for (LegzesiMunkamenet munkamenet : munkamenetek) {
            System.out.println(munkamenetSor(munkamenet));
        }
    }

    private void munkamenetTorlese() {
        naploMegjelenitese();
        if (szolgaltatas.getMunkamenetekSzama() == 0) {
            return;
        }

        int azonosito = egeszSzamBeolvasasa("\nA törlendő azonosító: ");
        try {
            if (szolgaltatas.torol(azonosito)) {
                System.out.println("A bejegyzés törölve lett.");
            } else {
                System.out.println("Nincs ilyen azonosítójú bejegyzés.");
            }
        } catch (IOException hiba) {
            System.out.println("Nem sikerült frissíteni a naplófájlt: " + hiba.getMessage());
        }
    }

    private void osszegzesMegjelenitese() {
        List<LegzesiMunkamenet> munkamenetek = szolgaltatas.getMunkamenetekIdorendben();
        int osszesPerc = 0;
        for (LegzesiMunkamenet munkamenet : munkamenetek) {
            osszesPerc += munkamenet.getPercek();
        }
        System.out.println("\n--- Összegzés ---");
        System.out.println("Bejegyzések száma: " + munkamenetek.size());
        System.out.println("Összes feljegyzett idő: " + osszesPerc + " perc");
    }

    private String munkamenetSor(LegzesiMunkamenet munkamenet) {
        String sor = String.format(
                "[%d] %s | %d perc | %s | kb. %d teljes ciklus",
                munkamenet.getAzonosito(),
                munkamenet.getIdopont().format(IDOPONT_FORMATUM),
                munkamenet.getPercek(),
                munkamenet.getStrategia().getNev(),
                munkamenet.getCiklusokSzama());

        if (!munkamenet.getMegjegyzes().isEmpty()) {
            sor += " | " + munkamenet.getMegjegyzes();
        }
        return sor;
    }

    private void kiirFejlec() {
        System.out.println("\n==================================");
        System.out.println("          LÉLEGZETNAPLÓ");
        System.out.println("==================================");
        System.out.println("Feljegyzett alkalmak: " + szolgaltatas.getMunkamenetekSzama());
        System.out.println("----------------------------------");
    }
}

