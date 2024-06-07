import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MainTest {

    public static Object genericInvokeMethod(Object obj, String methodName,
                                             Object... params) {
        int paramCount = params.length;
        Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            classArray[i] = params[i].getClass();
        }
        try {
            method = obj.getClass().getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            requiredObj = method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return requiredObj;
    }

    @org.junit.jupiter.api.Test
    void viewItemCorrect() {
        Main main = new Main();
    }

    @Test
    void viewItemNotCorrect() {


    }

    @Test
    void selectItemOfferUser() throws Exception {
//        DVMUI test = mock(DVMUI.class);
//        Method method = DVMUI.class.getDeclaredMethod("prepay_UI");
//        method.setAccessible(true);
//        method.invoke(test);
//        test.select_item(1, 1);
//
//        verify(test, times(1)).prepay_UI(anyInt());
    }

    @Test
    void selectItemOtherDVM() {

    }

    @Test
    void selectItemNotOffer() {
    }

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
    void resPrepayMsgReduceNotEnough() {
    }


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
