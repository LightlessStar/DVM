public class DVMUI {
    private int status;
    private DVMStock dvmStock;
    private DVMController dvmController;


    public DVMUI() {

    }

    /**
     * @param status - 0 : normal status, show all, 1, 2, 3...
     */
    private void item_list_UI(int status) {}

    /**
     * 1-b Select Item
     * @param status - 0 : normal
     */
    private void prepay_UI(int status) {

    }

    /**
     * 1-b Select Item
     * @param status : 0
     */
    private void pay_UI(int status) {}

    private void code_verify_UI(int status) {}

    private void item_UI(int status) {}

    private void complete_prepay_UI(int status) {}

    private void check_stock_UI(int status) {}

    private void admin_UI(int status) {}

    private void add_stock_UI(int status) {}

    /**
     * 1-a View Item
     * User Action
     */
    public void view_item_list() {
        item_list_UI(1);
    }

    /**
     * 1-b Select Item
     * @param item_code : Drink Code, 1 ~ 20, Matching one by one
     * @param count : Drink Amount, 1 ~ 20, or Error Occur
     */
    public void select_item(int item_code, int count) {
        int ret;

        if (count < 1 || count > 20)
        {
            this.item_list_UI(1);
        }
        else
        {
            //TODO : return type setting
            /**
             * return -1 : error
             * return 1 :
             * return 2 :
             * return 3 : count > 20, count < 0
             */
            ret = dvmStock.check_stock(item_code, count);
            if (ret == 0)
            {
                prepay_UI(status);
            }
            else{
                pay_UI(status);
            }
        }
    }

    /**
     * 1-c Pay by Verification Code
     */
    public void code_verify_menu() {
        code_verify_UI(0);
    }

    public void enter_code(String verify_code) {
        if (dvmController.enter_code(verify_code))
        {
            item_UI(0);
        }
        else
        {
            item_list_UI(0);
        }
    }

    /**
     * 1-d Insert Card, 1-e Insert Card(결제, 선결제)
     * @param card_id : card id in user input
     * TODO : require in 1-e sequence diagram
     */
    public void insert_card(int card_id){
        if (dvmController.send_card_num(card_id))
        {
            item_UI(0);
        }
        else
        {
            pay_UI(0);
        }
    }

    /**
     * 3-a Check Stock
     */
    public void check_stock() {
        int[] item;

        item = check_stock_all();
        //TODO : add check item amount logic
        check_stock_UI(this.status);
    }

    /**
     * 3-b Add Stock
     */
    public void show_menu() {
        admin_UI(this.status);
        //TODO : add_item_menu status shift logic
    }

    public void add_item_menu() {
        add_stock_UI(this.status);
        //TODO : add_item status shift logic
    }

    /**
     * Add item in Administer mode
     * @param item_code : add item code
     * @param stock : add item amount
     * if add_item_admin is true : show add
     * else : show fail add
     */
    public void add_item(int item_code, int stock) {
        if (dvmStock.add_item_admin(item_code, stock))
        {
            check_stock_UI(this.status);
        }
        else
        {
            check_stock_UI(this.status);
        }
    }
}
