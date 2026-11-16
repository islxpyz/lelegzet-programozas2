package hu.pte.prog2.domain;

public class NyugtatoStrategia implements LegzesiStrategia {
    @Override
    public String getAzonosito() {
        return "nyugtato";
    }

    @Override
    public String getNev() {
        return "Nyugtató 4–6";
    }

    @Override
    public int getBellegzesMasodperc() {
        return 4;
    }

    @Override
    public int getKillegzesMasodperc() {
        return 6;
    }

    @Override
    public String getLeiras() {
        return "4 másodperc be, majd 6 másodperc ki";
    }
}

