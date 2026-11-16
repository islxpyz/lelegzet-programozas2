package hu.pte.prog2.szolgaltatas;

import hu.pte.prog2.adattar.MunkamenetTarolo;
import hu.pte.prog2.domain.ErvenytelenMunkamenetException;
import hu.pte.prog2.domain.LegzesiMunkamenet;
import hu.pte.prog2.domain.LegzesiStrategia;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MunkamenetSzolgaltatas {
    private final MunkamenetTarolo tarolo;
    private final List<LegzesiMunkamenet> munkamenetek;
    private int kovetkezoAzonosito;

    public MunkamenetSzolgaltatas(MunkamenetTarolo tarolo) throws IOException {
        this.tarolo = tarolo;
        this.munkamenetek = new ArrayList<>(tarolo.betolt());
        this.kovetkezoAzonosito = kovetkezoAzonositoKiszamitasa();
    }

    public LegzesiMunkamenet ujMunkamenet(
            int percek,
            LegzesiStrategia strategia,
            String megjegyzes) throws ErvenytelenMunkamenetException, IOException {
        LegzesiMunkamenet munkamenet = new LegzesiMunkamenet(
                kovetkezoAzonosito,
                LocalDateTime.now(),
                percek,
                strategia,
                megjegyzes);

        munkamenetek.add(munkamenet);
        try {
            tarolo.mentes(munkamenetek);
        } catch (IOException hiba) {
            munkamenetek.remove(munkamenet);
            throw hiba;
        }

        kovetkezoAzonosito++;
        return munkamenet;
    }

    public List<LegzesiMunkamenet> getMunkamenetekIdorendben() {
        List<LegzesiMunkamenet> rendezettLista = new ArrayList<>(munkamenetek);
        rendezettLista.sort(Comparator
                .comparing(LegzesiMunkamenet::getIdopont)
                .reversed());
        return rendezettLista;
    }

    public boolean torol(int azonosito) throws IOException {
        for (int listaIndex = 0; listaIndex < munkamenetek.size(); listaIndex++) {
            LegzesiMunkamenet munkamenet = munkamenetek.get(listaIndex);
            if (munkamenet.getAzonosito() == azonosito) {
                munkamenetek.remove(listaIndex);
                try {
                    tarolo.mentes(munkamenetek);
                } catch (IOException hiba) {
                    munkamenetek.add(listaIndex, munkamenet);
                    throw hiba;
                }
                return true;
            }
        }
        return false;
    }

    public int getMunkamenetekSzama() {
        return munkamenetek.size();
    }

    private int kovetkezoAzonositoKiszamitasa() {
        int legnagyobbAzonosito = 0;
        for (LegzesiMunkamenet munkamenet : munkamenetek) {
            legnagyobbAzonosito = Math.max(legnagyobbAzonosito, munkamenet.getAzonosito());
        }
        return legnagyobbAzonosito + 1;
    }
}

