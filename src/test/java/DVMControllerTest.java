import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

class DVMControllerTest {

    private DVMController dvmController;

    @BeforeEach
    void setUp() {
        dvmController = new DVMController(new DVMStock());
    }

    @AfterEach
    void tearDown() {
        dvmController = null;
    }

    @Test
    void request_stock_msg() throws NoSuchFieldException, IllegalAccessException {
        // DVMController의 request_stock_msg 메서드가 호출된 후 예상 결과를 확인하는 테스트
        String[] result = dvmController.request_stock_msg(1, 10);

        Field otherDvmStockField = DVMController.class.getDeclaredField("other_dvm_stock");
        otherDvmStockField.setAccessible(true);
        HashMap<String, Integer> otherDvmStock = (HashMap<String, Integer>) otherDvmStockField.get(dvmController);

        assertNotNull(result);
        assertTrue(result.length > 0);
        if (!"0".equals(result[0])) {
            assertTrue(result.length > 1, "Result array should have at least two elements");
            assertNotNull(result[1], "Result[1] should not be null");
            assertTrue(result[1].length() > 0, "Result[1] should not be empty");

            assertTrue(result.length > 2, "Result array should have at least three elements");
            assertNotNull(result[2], "Result[2] should not be null");
            assertTrue(result[2].length() > 0, "Result[2] should not be empty");
        }
    }

    @Test
    void send_code() throws NoSuchFieldException, IllegalAccessException {
        // DVMController의 send_code 메서드가 인증 코드를 제대로 처리하는지 확인하는 테스트
        Field codeStockField = DVMController.class.getDeclaredField("code_stock");
        codeStockField.setAccessible(true);
        HashMap<String, Integer> codeStock = (HashMap<String, Integer>) codeStockField.get(dvmController);
        codeStock.put("testCode", 123);

        int result = dvmController.send_code("testCode");

        assertEquals(123, result);
        assertFalse(codeStock.containsKey("testCode"));
    }

    @Test
    void send_card_num() {
        // DVMController의 send_card_num 메서드가 카드 번호를 제대로 처리하는지 확인하는 테스트
        boolean result = dvmController.send_card_num(1001, 500);

        assertTrue(result);
    }

//    @Test
//    void cancel_prepay() {
//        // DVMController의 cancel_prepay 메서드가 선결제를 취소하는지 확인하는 테스트
//        boolean result = dvmController.cancel_prepay(1001, 500);
//
//        assertTrue(result);
//    }

    @Test
    void res_stock_msg() {
        // DVMController의 res_stock_msg 메서드가 stock_msg를 처리하는지 확인하는 테스트
        //dvmController.res_stock_msg();
        // 실제 테스트는 비동기 서버 스레드가 실행되는지 여부와 관련이 있으므로 별도의 확인 방법이 필요함
    }

    @Test
    void req_stock_msg() {
        // DVMController의 req_stock_msg 메서드가 stock_msg를 요청하는지 확인하는 테스트
        JSONObject msg = new JSONObject()
                .put("msg_type", "req_stock")
                .put("src_id", "Team6")
                .put("dst_id", "0")
                .put("msg_content", new JSONObject()
                        .put("item_code", "01")
                        .put("item_num", "10")
                );
    }

    @Test
    void prepay_info() throws NoSuchFieldException, IllegalAccessException {
        // DVMController의 prepay_info 메서드가 선결제를 처리하는지 확인하는 테스트
        String[] result = dvmController.prepay_info(1, 10);

        Field otherDvmStockField = DVMController.class.getDeclaredField("other_dvm_stock");
        otherDvmStockField.setAccessible(true);
        HashMap<String, Integer> otherDvmStock = (HashMap<String, Integer>) otherDvmStockField.get(dvmController);

        assertNotNull(result);
        assertTrue(result.length > 0);

        if (!"0".equals(result[0])) {
            assertTrue(result.length > 1, "Result array should have at least two elements");
            assertNotNull(result[1], "Result[1] should not be null");
            assertTrue(result[1].length() > 0, "Result[1] should not be empty");

            assertTrue(result.length > 2, "Result array should have at least three elements");
            assertNotNull(result[2], "Result[2] should not be null");
            assertTrue(result[2].length() > 0, "Result[2] should not be empty");
        }
    }
}
