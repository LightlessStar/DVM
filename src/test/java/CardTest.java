import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card card = new Card(11111112,10000);
    @Test
    void card_check() {
        assertFalse(card.card_check(11111111));
    }

    @Test
    void cash_check() {
        assertFalse(card.cash_check(11111111));
    }

    @Test
    void pay() {
        //assertAll(card.pay(3333));
    }
}