import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DVMStockTest {
    @Test
    void test_check_stock_all() {
        DVMStock stock = new DVMStock();
        int[] test_item = new int[20];
        test_item = stock.check_stock_all();
        for (int i = 0; i < test_item.length; i++) {
            if (i < 7) {
                assertEquals(test_item[i], 99);
            } else {
                assertEquals(test_item[i], 0);
            }
        }
    }

    @Test
    void test_check_stock() {
        DVMStock stock = new DVMStock();
        for (int i = 0; i < stock.item.length; i++) {
            if (i < 7 && i > 0) {
                int temp = stock.item[i];
                assertEquals(1, stock.check_stock(i + 1, 1));
                assertEquals(temp - 1, stock.item[i]);
            }
        }
    }

    @Test
    void test_add_item() {
        DVMStock stock = new DVMStock();

        for (int i = 0; i < stock.item.length; i++) {
            if (i < 7) {
                int temp = stock.item[i];
                assertTrue(stock.add_item(i + 1, 1));
                assertEquals(temp + 1, stock.item[i]);
            } else {
                assertFalse(stock.add_item(i + 1, 1));
            }
        }
    }
}
