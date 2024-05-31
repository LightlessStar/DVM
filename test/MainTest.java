import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @org.junit.jupiter.api.Test
    void sum1() {
        Main main = new Main();
        assertEquals(main.sum(1, 2), 3);
    }

    @Test
    void sum2() {
        Main main = new Main();
        assertEquals(main.sum(1, 2), 4);
    }

    @Test
    void viewItemCorrect() {
        Main main = new Main();
    }

    @Test
    void viewItemNotCorrect() {

    }

    @Test
    void selectItemOfferUser() {}

    @Test
    void selectItemOtherDVM() {

    }

    @Test
    void selectItemNotOffer() {}

    @Test
    void payByVerificationCodeCorrect() {

    }

    @Test
    void payByVerificationCodeNotCorrect() {

    }

    @Test
    void insertCardNotCorrect() {

    }

    @Test
    void insertCardCorrect() {

    }

    @Test
    void insertCardPrepayNotCorrect() {

    }

    @Test
    void insertCardPrepayCorrect() {

    }

    //Test usecase 2.*
    @Test
    void reqStockMsgCorrect() {

    }

    @Test
    void reqStockMsgNotCorrect() {

    }

    @Test
    void resStockMsgInput() {

    }

    @Test
    void resStockMsgNotValidAPI() {

    }

    @Test
    void resStockMsgNotConnected() {

    }

    @Test
    void reqPrepayMsgInput() {

    }

    @Test
    void reqPrepayMsgNotValidAPI() {

    }

    @Test
    void reqPrepayMsgNotConnected() {

    }

    @Test
    void resPrepayMsgReduceEnough() {

    }

    @Test
    void resPrepayMsgReduceNotEnough() {}


    @Test
    void resPrepayMsgNotValidAPI() {

    }

    @Test
    void resPrepayMsgNotConnected() {

    }

    //test case use case 3-*
    @Test
    void checkStockCorrect() {

    }

    @Test
    void checkStockNotCorrect() {

    }

    @Test
    void addStockCorrect() {

    }

    @Test
    void addStockNotCorrect() {

    }

    //test case use case 4-*
    @Test
    void payEnoughMoney() {

    }

    @Test
    void payNotEnoughMoney() {

    }

    @Test
    void cancelPayCorrectCardNum() {

    }

    @Test
    void cancelPayNotCorrectCardNum() {

    }
}
