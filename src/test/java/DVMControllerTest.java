import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DVMControllerTest {

    @Test
    void request_stock_msg() {

    }

    @Test
    void send_code() {

    }

    @Test
    void send_card_num() {
        DVMController dvmController = new DVMController();

    }

    @Test
    void res_stock_msg() {
        DVMController dvmController = new DVMController();

        assertEquals(dvmController.req_stock_msg(msg));
    }

    @Test
    void req_stock_msg() {
        DVMController dvmController = new DVMController();

        assertEquals(dvmController.req_stock_msg(msg));
    }
}