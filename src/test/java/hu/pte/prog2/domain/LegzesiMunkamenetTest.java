package hu.pte.prog2.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LegzesiMunkamenetTest {
    @Test
    void aNullaPercesMunkamenetetElutasitja() {
        assertThrows(ErvenytelenMunkamenetException.class,
                () -> new LegzesiMunkamenet(
                        1, LocalDateTime.now(), 0, new NyugtatoStrategia(), ""));
    }

    @Test
    void aValasztottStrategiaMegvaltoztatjaACiklusokSzamat() throws Exception {
        LegzesiMunkamenet nyugtatoMunkamenet = new LegzesiMunkamenet(
                1, LocalDateTime.now(), 2, new NyugtatoStrategia(), "");
        LegzesiMunkamenet egyenletesMunkamenet = new LegzesiMunkamenet(
                2, LocalDateTime.now(), 2, new EgyenletesStrategia(), "");

        assertEquals(12, nyugtatoMunkamenet.getCiklusokSzama());
        assertEquals(15, egyenletesMunkamenet.getCiklusokSzama());
    }
}

