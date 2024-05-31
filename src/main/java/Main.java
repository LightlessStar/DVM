public class Main {
    public static void main(String[] args) {
        System.out.println("team6 DVM start");

        DVMController dvmController = new DVMController();
//        dvmController.runServer();
//        dvmController.runClient();
//        dvmController.runClient2();

        dvmController.request_stock_msg(3,4);

    }
}