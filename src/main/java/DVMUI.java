import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DVMUI extends JFrame {

  final int ERROR = -1;
  final int DEF = 0;
  final int PAY = 100;
  final int PREPAY = 200;
  final private String[] drink = {
      "없음", "콜라", "사이다",
      "녹차", "홍차", "밀크티", "탄산수",
      "보리차", "캔커피", "물", "에너지드링크",
      "유자차", "식혜", "아이스티", "딸기주스",
      "오렌지주스", "포도주스", "이온음료", "아메리카노",
      "핫초코", "카페라뗴"
  };

  private int status;
  private DVMStock dvmStock;
  private DVMController dvmController;

  private static int findStringIndex(String[] array, String target) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].equals(target)) {
        return i;
      }
    }
    return -1;
  }


  public DVMUI() {
    JFrame frame = new JFrame("DVM TEAM6");
    frame.setSize(600, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    item_list_UI(DEF);
    frame.setVisible(true); // 화면에 프레임 출력
  }

  private void item_list_UI(int status) {
    Container contentPane = getContentPane();
    //4 * 5 짜리 jpanel을 만들어 jframe에 부착하는 작업.
    JPanel buttonPanel1 = new JPanel();
    buttonPanel1.setLayout(new GridLayout(4, 5));
    for (int i = 1; i <= 20; i++) {
      JButton button = new JButton(drink[i]);
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JButton b = (JButton) e.getSource();
          String buttonText = b.getText();
          int status = findStringIndex(drink, buttonText);
//                    select_item(status);
        }
      });
      buttonPanel1.add(button);
    }
    JPanel buttonPanel2 = new JPanel();

    buttonPanel2.setLayout(new GridLayout(1, 2));
    JButton button = new JButton("선결제 코드 인증");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        code_verify_menu();
      }
    });
    JButton button2 = new JButton("관리자 모드");
    button2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        show_menu();
      }
    });
    buttonPanel2.add(button);
    buttonPanel2.add(button2);

    //패널 2개를 contentPane에 추가
    contentPane.add(buttonPanel1, BorderLayout.CENTER);
    contentPane.add(buttonPanel2, BorderLayout.SOUTH);
  }

  /**
   * 1-b Select Item
   *
   * @param status - 1 ~ 20 : drink status
   */
  private void prepay_UI(int status) {
    //TODO : set drink string using status
    Container contentPane = getContentPane();
    String drinkSomething = drink[status];

  }

  /**
   * 1-b Select Item
   *
   * @param status : 0
   */
  private void pay_UI(int status) {
  }

  private void code_verify_UI(int status) {

  }

  private void item_UI(int status) {
  }

  private void complete_prepay_UI(int status) {
  }

  private void check_stock_UI(int status) {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    // "관리자메뉴" 라벨 추가
    JLabel adminLabel = new JLabel("재고 확인", JLabel.CENTER);
    contentPane.add(adminLabel, BorderLayout.NORTH);


  }

  private void admin_UI(int status) {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    // "관리자메뉴" 라벨 추가
    JLabel adminLabel = new JLabel("관리자메뉴", JLabel.CENTER);
    contentPane.add(adminLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel();
    JButton button1 = new JButton("재고 확인");
    button1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        check_stock();
      }
    });
    JButton button2 = new JButton("재고 보충");
    button2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        add_item_menu();
      }
    });
    buttonPanel.add(button1);
    buttonPanel.add(button2);
    contentPane.add(buttonPanel, BorderLayout.SOUTH);
  }

  private void add_stock_UI(int status) {
  }

  /**
   * 1-a View Item User Action
   */
  public void view_item_list() {
    item_list_UI(1);
  }

  /**
   * 1-b Select Item
   *
   * @param item_code : Drink Code, 1 ~ 20, Matching one by one
   * @param count     : Drink Amount, 1 ~ 20, or Error Occur
   */
  public void select_item(int item_code, int count) {
    int ret;

    if (count < 1 || count > 20) {
      this.item_list_UI(1);
    } else {
      //TODO : return type setting
      /**
       * return -1 : error
       * return 1 :
       * return 2 :
       * return 3 : count > 20, count < 0
       */
      ret = dvmStock.check_stock(item_code, count);
      if (ret == 0) {
        prepay_UI(status);
      } else {
        pay_UI(status);
      }
    }
  }

  /**
   * 1-c Pay by Verification Code
   */
  public void code_verify_menu() {
    code_verify_UI(0);
  }

  public void enter_code(String verify_code) {
    if (dvmController.enter_code(verify_code)) {
      item_UI(0);
    } else {
      item_list_UI(0);
    }
  }

  /**
   * 1-d Insert Card, 1-e Insert Card(결제, 선결제)
   *
   * @param card_id : card id in user input
   *                                                                                                                                                                      TODO : require in 1-e sequence diagram
   */
  public void insert_card(int card_id) {
    if (dvmController.send_card_num(card_id)) {
      item_UI(0);
    } else {
      pay_UI(0);
    }
  }

  /**
   * 3-a Check Stock
   */
  public void check_stock() {
    int[] item;

    item = dvmStock.check_stock_all();
    //TODO : add check item amount logic
    check_stock_UI(this.status);
  }

  /**
   * 3-b Add Stock
   */
  public void show_menu() {
    admin_UI(this.status);
    //TODO : add_item_menu status shift logic
  }

  public void add_item_menu() {
    add_stock_UI(this.status);
    //TODO : add_item status shift logic
  }

  /**
   * Add item in Administer mode
   *
   * @param item_code : add item code
   * @param stock     : add item amount if add_item_admin is true : show add else : show fail add
   */
  public void add_item(int item_code, int stock) {
    if (dvmStock.add_item(item_code, stock)) {
      check_stock_UI(this.status);
    } else {
      check_stock_UI(this.status);
    }
  }
}
