public enum Status {
    error(-1), def(0), pay(1), prepay(2);
    private final int value;
    Status(int value) { this.value = value; }
    public int getValue() { return value; }
}
