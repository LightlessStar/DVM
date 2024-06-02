import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Bank bank = new Bank();
    @Test
    void certify_pay() {
        assertFalse(bank.certify_pay(11111112,3222));
    }

    @Test
    void cancel_pay() {
        assertFalse(bank.cancel_pay(11111112,3423));
    }
}