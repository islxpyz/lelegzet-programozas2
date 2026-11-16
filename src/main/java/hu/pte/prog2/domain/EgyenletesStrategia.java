package hu.pte.prog2.domain;

public class EgyenletesStrategia implements LegzesiStrategia {
    @Override
    public String getAzonosito() {
        return "egyenletes";
    }

    @Override
    public String getNev() {
        return "Egyenletes 4–4";
    }

    @Override
    public int getBellegzesMasodperc() {
        return 4;
    }

    @Override
    public int getKillegzesMasodperc() {
        return 4;
    }

    @Override
    public String getLeiras() {
        return "4 másodperc be, majd 4 másodperc ki";
    }
}

