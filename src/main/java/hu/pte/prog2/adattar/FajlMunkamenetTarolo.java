package hu.pte.prog2.adattar;

import hu.pte.prog2.domain.ErvenytelenMunkamenetException;
import hu.pte.prog2.domain.LegzesiMunkamenet;
import hu.pte.prog2.domain.LegzesiStrategia;
import hu.pte.prog2.domain.LegzesiStrategiak;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FajlMunkamenetTarolo implements MunkamenetTarolo {
    private final Path fajlUtvonal;

    public FajlMunkamenetTarolo(Path fajlUtvonal) {
        this.fajlUtvonal = fajlUtvonal;
    }

    @Override
    public List<LegzesiMunkamenet> betolt() throws IOException {
        List<LegzesiMunkamenet> munkamenetek = new ArrayList<>();
        if (!Files.exists(fajlUtvonal)) {
            return munkamenetek;
        }

        try (BufferedReader olvaso = Files.newBufferedReader(fajlUtvonal, StandardCharsets.UTF_8)) {
            String sor;
            int sorszam = 0;
            while ((sor = olvaso.readLine()) != null) {
                sorszam++;
                if (sor.isBlank()) {
                    continue;
                }

                String[] adatok = sor.split("\t", 5);
                if (adatok.length != 5) {
                    throw new IOException("Hibás adat a napló " + sorszam + ". sorában.");
                }

                try {
                    int azonosito = Integer.parseInt(adatok[0]);
                    LocalDateTime idopont = LocalDateTime.parse(adatok[1]);
                    int percek = Integer.parseInt(adatok[2]);
                    LegzesiStrategia strategia = LegzesiStrategiak.azonositoAlapjan(adatok[3]);
                    LegzesiMunkamenet munkamenet =
                            new LegzesiMunkamenet(azonosito, idopont, percek, strategia, adatok[4]);
                    munkamenetek.add(munkamenet);
                } catch (IllegalArgumentException | DateTimeParseException
                         | ErvenytelenMunkamenetException hiba) {
                    throw new IOException("Hibás adat a napló " + sorszam + ". sorában.", hiba);
                }
            }
        }

        return munkamenetek;
    }

    @Override
    public void mentes(List<LegzesiMunkamenet> munkamenetek) throws IOException {
        Path mappa = fajlUtvonal.toAbsolutePath().getParent();
        if (mappa != null) {
            Files.createDirectories(mappa);
        }

        try (BufferedWriter iro = Files.newBufferedWriter(
                fajlUtvonal, StandardCharsets.UTF_8)) {
            for (LegzesiMunkamenet munkamenet : munkamenetek) {
                String sor = munkamenet.getAzonosito()
                        + "\t" + munkamenet.getIdopont()
                        + "\t" + munkamenet.getPercek()
                        + "\t" + munkamenet.getStrategia().getAzonosito()
                        + "\t" + munkamenet.getMegjegyzes();
                iro.write(sor);
                iro.newLine();
            }
        }
    }
}

