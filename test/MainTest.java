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


}