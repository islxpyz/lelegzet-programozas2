package hu.pte.prog2.adattar;

import hu.pte.prog2.domain.LegzesiMunkamenet;
import hu.pte.prog2.domain.NyugtatoStrategia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FajlMunkamenetTaroloTest {
    @TempDir
    Path ideiglenesMappa;

    @Test
    void elmentiEsVisszatoltiAMunkamenetet() throws Exception {
        Path naploFajl = ideiglenesMappa.resolve("naplo.tsv");
        FajlMunkamenetTarolo tarolo = new FajlMunkamenetTarolo(naploFajl);
        LegzesiMunkamenet eredetiMunkamenet = new LegzesiMunkamenet(
                7,
                LocalDateTime.of(2026, 11, 16, 18, 30),
                5,
                new NyugtatoStrategia(),
                "Tanulás után");

        tarolo.mentes(List.of(eredetiMunkamenet));
        List<LegzesiMunkamenet> betoltottMunkamenetek = tarolo.betolt();

        assertEquals(1, betoltottMunkamenetek.size());
        assertEquals(7, betoltottMunkamenetek.get(0).getAzonosito());
        assertEquals("Tanulás után", betoltottMunkamenetek.get(0).getMegjegyzes());
        assertEquals("nyugtato",
                betoltottMunkamenetek.get(0).getStrategia().getAzonosito());
    }

    @Test
    void hianyzoFajlEsEsetenUresListatAd() throws Exception {
        FajlMunkamenetTarolo tarolo =
                new FajlMunkamenetTarolo(ideiglenesMappa.resolve("meg-nincs.tsv"));

        assertTrue(tarolo.betolt().isEmpty());
    }
}

