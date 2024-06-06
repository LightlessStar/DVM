
public class DVMStock {
    int[] item;
    boolean[] Is_our_item;
    DVMController dvmController;

    public DVMStock() {
        item = new int[20];
        //item 개수 99개로 초기화
        for (int i = 0; i < item.length; i++) {
            item[i] = 99;
        }

        Is_our_item = new boolean[20];
        //우리가 관리하는 Item 정하기
        Is_our_item[0] = true;
        Is_our_item[1] = true;
        Is_our_item[2] = true;
        Is_our_item[3] = true;
        Is_our_item[4] = true;
        Is_our_item[5] = true;
        Is_our_item[6] = true;
        //우리가 관리하지 않는 Item 0 초기화
        for (int i = 0; i < item.length; i++) {
            if (!Is_our_item[i]) {
                item[i] = 0;
            }
        }
    }

    public int check_stock(int item_code, int count) {
        try {
            if (!Is_our_item[item_code - 1] || item[item_code - 1] < count) {
                //우리한테 없는 item || item 재고 없음
                return 1;
            } else if (count > 20 || count < 0) {
                System.out.println("EXP: 구매 가능 개수 20개를 초과했거나 count가 올바르지 않습니다");
                return -1;
            } else {
                //우리가 가진 item, 재고 충분
                item[item_code - 1] -= count;
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return -1;
        }
    }

    public int[] check_stock_all() {
        return item;
    }

    public boolean add_item(int item_code, int stock) {
        if (Is_our_item[item_code - 1]) {
            int temp = item[item_code - 1];
            item[item_code - 1] += stock;
            if (item[item_code - 1] - temp == stock) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
