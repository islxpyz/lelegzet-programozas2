package hu.pte.prog2.domain;

public final class LegzesiStrategiak {
    private LegzesiStrategiak() {
    }

    public static LegzesiStrategia azonositoAlapjan(String azonosito) {
        if ("nyugtato".equals(azonosito)) {
            return new NyugtatoStrategia();
        }
        if ("egyenletes".equals(azonosito)) {
            return new EgyenletesStrategia();
        }
        throw new IllegalArgumentException("Ismeretlen légzési minta: " + azonosito);
    }
}

