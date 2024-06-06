public class Main {

    public static void main(String[] args) {
        System.out.println("team6 DVM start");

        DVMStock dvmStock = new DVMStock();

        //DVM UI main test
        //DVMUI dvmui = new DVMUI();

        //DVM Controller main test
        DVMController server_dvmController = new DVMController(dvmStock);
        DVMController cli_dvmController = new DVMController(dvmStock);
        DVMController cli2_dvmController = new DVMController(dvmStock);

        server_dvmController.res_stock_msg();
        cli_dvmController.request_stock_msg(20, 4);
        cli2_dvmController.request_stock_msg(3, 5);
////        cli_dvmController.request_stock_msg(3, 6);
////        cli_dvmController.request_stock_msg(3, 7);

        DVMUI dvmui = new DVMUI(dvmStock);
    }
}