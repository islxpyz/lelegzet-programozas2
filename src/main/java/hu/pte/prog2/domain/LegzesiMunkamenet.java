package hu.pte.prog2.domain;

import java.time.LocalDateTime;

public class LegzesiMunkamenet {
    private static final int MINIMALIS_PERCEK = 1;
    private static final int MAXIMALIS_PERCEK = 120;
    private static final int MAXIMALIS_MEGJEGYZES_HOSSZ = 100;

    private final int azonosito;
    private final LocalDateTime idopont;
    private final int percek;
    private final LegzesiStrategia strategia;
    private final String megjegyzes;

    public LegzesiMunkamenet(
            int azonosito,
            LocalDateTime idopont,
            int percek,
            LegzesiStrategia strategia,
            String megjegyzes) throws ErvenytelenMunkamenetException {
        if (azonosito < 1) {
            throw new ErvenytelenMunkamenetException("Az azonosítónak pozitív számnak kell lennie.");
        }
        if (idopont == null) {
            throw new ErvenytelenMunkamenetException("Az időpont nem lehet üres.");
        }
        if (percek < MINIMALIS_PERCEK || percek > MAXIMALIS_PERCEK) {
            throw new ErvenytelenMunkamenetException("Az időtartam 1 és 120 perc közé essen.");
        }
        if (strategia == null) {
            throw new ErvenytelenMunkamenetException("Válassz egy légzési mintát.");
        }

        String megtisztitottMegjegyzes = megjegyzes == null ? "" : megjegyzes.trim();
        megtisztitottMegjegyzes = megtisztitottMegjegyzes
                .replace('\t', ' ')
                .replace('\n', ' ')
                .replace('\r', ' ');
        if (megtisztitottMegjegyzes.length() > MAXIMALIS_MEGJEGYZES_HOSSZ) {
            throw new ErvenytelenMunkamenetException("A megjegyzés legfeljebb 100 karakter lehet.");
        }

        this.azonosito = azonosito;
        this.idopont = idopont;
        this.percek = percek;
        this.strategia = strategia;
        this.megjegyzes = megtisztitottMegjegyzes;
    }

    public int getAzonosito() {
        return azonosito;
    }

    public LocalDateTime getIdopont() {
        return idopont;
    }

    public int getPercek() {
        return percek;
    }

    public LegzesiStrategia getStrategia() {
        return strategia;
    }

    public String getMegjegyzes() {
        return megjegyzes;
    }

    public int getCiklusokSzama() {
        int ciklusMasodperc = strategia.getBellegzesMasodperc()
                + strategia.getKillegzesMasodperc();
        return percek * 60 / ciklusMasodperc;
    }
}

