package hu.pte.prog2.szolgaltatas;

import hu.pte.prog2.adattar.FajlMunkamenetTarolo;
import hu.pte.prog2.domain.EgyenletesStrategia;
import hu.pte.prog2.domain.LegzesiMunkamenet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MunkamenetSzolgaltatasTest {
    @TempDir
    Path ideiglenesMappa;

    @Test
    void letrehozottBejegyzesUjrainditasUtanIsBetoltheto() throws Exception {
        Path naploFajl = ideiglenesMappa.resolve("adatok/naplo.tsv");
        MunkamenetSzolgaltatas elsoSzolgaltatas =
                new MunkamenetSzolgaltatas(new FajlMunkamenetTarolo(naploFajl));

        elsoSzolgaltatas.ujMunkamenet(3, new EgyenletesStrategia(), "Pihenő");

        MunkamenetSzolgaltatas ujrainditottSzolgaltatas =
                new MunkamenetSzolgaltatas(new FajlMunkamenetTarolo(naploFajl));
        List<LegzesiMunkamenet> betoltottLista =
                ujrainditottSzolgaltatas.getMunkamenetekIdorendben();

        assertEquals(1, betoltottLista.size());
        assertEquals(1, betoltottLista.get(0).getAzonosito());
        assertEquals("Pihenő", betoltottLista.get(0).getMegjegyzes());
    }

    @Test
    void torlesUtanNemMaradBejegyzes() throws Exception {
        MunkamenetSzolgaltatas szolgaltatas =
                new MunkamenetSzolgaltatas(new FajlMunkamenetTarolo(
                        ideiglenesMappa.resolve("torles/naplo.tsv")));
        szolgaltatas.ujMunkamenet(3, new EgyenletesStrategia(), "");

        assertTrue(szolgaltatas.torol(1));
        assertFalse(szolgaltatas.torol(1));
        assertEquals(0, szolgaltatas.getMunkamenetekSzama());
    }
}

