public class Main {
    public static void main(String[] args) {
        System.out.println("team6 DVM start");

        //DVM UI main test
        //DVMUI dvmui = new DVMUI();


        //DVM Controller main test
        DVMController server_dvmController = new DVMController();
        DVMController cli_dvmController = new DVMController();
        DVMController cli2_dvmController = new DVMController();
//        dvmController.runServer();
//        dvmController.runClient();
//        dvmController.runClient2();
        server_dvmController.res_stock_msg();
        cli_dvmController.request_stock_msg(3, 4);
        cli2_dvmController.request_stock_msg(3, 5);
//        cli_dvmController.request_stock_msg(3, 6);
//        cli_dvmController.request_stock_msg(3, 7);

    }
}