package hu.pte.prog2.domain;

public class ErvenytelenMunkamenetException extends Exception {
    private static final long serialVersionUID = 1L;

    public ErvenytelenMunkamenetException(String uzenet) {
        super(uzenet);
    }
}

