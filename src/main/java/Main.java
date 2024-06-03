public class Main {
    public static void main(String[] args) {
        System.out.println("team6 DVM start");

        //DVM UI main test
        //DVMUI dvmui = new DVMUI();


        //DVM Controller main test
        DVMController dvmController = new DVMController();
//        dvmController.runServer();
//        dvmController.runClient();
//        dvmController.runClient2();
        dvmController.res_stock_msg();
        dvmController.request_stock_msg(3, 4);
//        dvmController.request_stock_msg(3, 5);
//        dvmController.request_stock_msg(3, 6);
//        dvmController.request_stock_msg(3, 7);

    }
}