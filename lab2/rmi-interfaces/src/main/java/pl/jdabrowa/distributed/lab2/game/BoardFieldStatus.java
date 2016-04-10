package pl.jdabrowa.distributed.lab2.game;

public enum BoardFieldStatus {

    UNKNOWN(" "),
    HIT("x"),
    MISS("."),
    SHIP("*");

    BoardFieldStatus(String indicator) {
        this.indicator = indicator;
    }

    private String indicator;

    public String getMark() {
        return indicator;
    }

    public static BoardFieldStatus fromString(String s) {
        switch (s) {
            case " ":
                return UNKNOWN;
            case "x":
                return HIT;
            case ".":
                return MISS;
            case "*":
                return SHIP;
            default:
                return null;
        }
    }
}
