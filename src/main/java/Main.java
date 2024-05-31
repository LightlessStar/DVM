public class Main {
    public static void main(String[] args) {
        System.out.println("team5 DVM start");

        DVMController dvmController = new DVMController();
            dvmController.runServer();
            dvmController.runClient();
            dvmController.runClient2();

    }

    public int sum(int a, int b) {
        return (a + b);
    }
}