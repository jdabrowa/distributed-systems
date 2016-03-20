package pl.jdabrowa.distributed.lab2.game;

public enum BoardFieldStatus {

    UNKNOWN(" "),
    HIT("x"),
    MISS("."),
    OWN_SHIP("*");

    BoardFieldStatus(String indicator) {
        this.indicator = indicator;
    }

    private String indicator;

    public String getMark() {
        return indicator;
    }
}
