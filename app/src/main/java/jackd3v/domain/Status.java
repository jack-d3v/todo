package jackd3v.domain;

public enum Status {
    ACTIVE(" "),
    PARKED("-"),
    CLOSED("x");

    private final String symbol;

    Status(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
