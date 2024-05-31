
public class DVMStock {
    int[] item;
    boolean[] Is_our_item;
    DVMController Controller;
    public DVMStock() {
        item = new int[20];
        Controller= new DVMController();
        Is_our_item=new boolean[20];
        for(int i=0;i<item.length;i++){
            if(i<=7){
                item[i]=i;
                Is_our_item[i]=true;
            }else{
                item[i]=0;
                Is_our_item[i]=false;
            }
        }
    }
    public int check_stock(int item_code, int count){
        if(item[item_code-1]==-1 || item[item_code-1]==0){
            if(Controller.request_stock_msg(item_code, count)){
                return 1;
            }else{
                return 2;
            }
        }else if(count>20 || count<0) {
            return 3;
        }
        return -1;
    }

    public int[] check_stock_all() {
        return item;
    }

    public boolean add_item(int item_code, int stock) {
        if(Is_our_item[item_code-1]) {
            int temp = item[item_code - 1];
            item[item_code - 1] += stock;
            if (item[item_code - 1] - temp == stock) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
}
