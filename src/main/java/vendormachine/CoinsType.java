package vendormachine;

public enum CoinsType {
    PENNY(1),
    NICKLE(5),
    DIME(10),
    QUARTER(25);
    private int worth;

    CoinsType(int worth) {
        this.worth = worth;
    }

    public int getWorth() {
        return worth;
    }
}
