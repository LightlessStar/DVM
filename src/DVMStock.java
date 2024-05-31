public class DVMStock {
    int[] item;
    boolean[] Is_our_item;

    public DVMStock() {
        item = new int[20];
        for(int i=0;i<item.length;i++){
            if(i<=7){
                item[i]=i;
                Is_our_item[i]=true;
            }else{
                item[i]=-1;
                Is_our_item[i]=false;
            }
        }
    }
    public boolean check_stock(int item_code, int count){
        if(item[item_code-1]==-1 || item[item_code]==0){
            return DVMController.request_stock_msg(item_code, count);
        }else{
            return true;
        }
    };

    public int[] check_stock_all() {
        return item;
    }

    public boolean add_item(int item, int stock) {
        item[item]+=stock;
        return true;
    }
}
