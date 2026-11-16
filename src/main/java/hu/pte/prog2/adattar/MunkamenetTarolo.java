package hu.pte.prog2.adattar;

import hu.pte.prog2.domain.LegzesiMunkamenet;

import java.io.IOException;
import java.util.List;

public interface MunkamenetTarolo {
    List<LegzesiMunkamenet> betolt() throws IOException;

    void mentes(List<LegzesiMunkamenet> munkamenetek) throws IOException;
}

