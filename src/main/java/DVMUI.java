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
  final private JFrame frame;
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
    frame = new JFrame("DVM TEAM6");
//    this->frame = frame;
    frame.setSize(600, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    item_list_UI(DEF);
    frame.setVisible(true); // 화면에 프레임 출력
  }

  private void item_list_UI(int status) {
    Container contentPane = frame.getContentPane();
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

    //새로운 contentPane으로 설정
    frame.setContentPane(contentPane);

    //새 contentPane 재배치 및 다시 그리기
    frame.revalidate();
    ;
    frame.repaint();
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

    String printTitle = "현재 재고가 부족합니다.";
    String printSub = "선결제를 진행하고 구매할 수 있습니다.\n " + drinkSomething + "\n선결제를 진행하시겠습니까?\n";

    JPanel top = new JPanel(new GridLayout(1, 3));
    JLabel title = new JLabel(printTitle);
    top.add(title, BorderLayout.NORTH);
    JPanel subTitle = new JPanel();
    top.add(subTitle, BorderLayout.CENTER);
    JPanel bottom = new JPanel(new GridLayout(2, 1));
    JButton button1 = new JButton("구매");
    JButton button2 = new JButton("취소");
    bottom.add(button1, BorderLayout.WEST);
    bottom.add(button2, BorderLayout.EAST);

    contentPane.add(top);
    contentPane.add(bottom);
    //새로운 contentPane으로 설정
    frame.setContentPane(contentPane);

    //새 contentPane 재배치 및 다시 그리기
    frame.revalidate();
    ;
    frame.repaint();
  }

  /**
   * 1-b Select Item
   *
   * @param status PAY - normal pay condition ERROR - error
   */
  private void pay_UI(int status) {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    if (status == ERROR) {
      JLabel adminLabel = new JLabel("잔고 부족", JLabel.CENTER);
      contentPane.add(adminLabel, BorderLayout.NORTH);
      JButton button = new JButton("돌아가기");
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          item_list_UI(status);
        }
      });
      contentPane.add(button, BorderLayout.SOUTH);
    } else if (status == PAY) {
      JLabel adminLabel = new JLabel("결제 완료", JLabel.CENTER);
      contentPane.add(adminLabel, BorderLayout.NORTH);
      JButton button = new JButton("돌아가기");
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          item_list_UI(status);
        }
      });
      contentPane.add(button, BorderLayout.SOUTH);
    }

    //새로운 contentPane으로 설정
    frame.setContentPane(contentPane);

    //새 contentPane 재배치 및 다시 그리기
    frame.revalidate();
    ;
    frame.repaint();
  }

  private void code_verify_UI(int status) {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    JLabel adminLabel = new JLabel("재고 확인", JLabel.CENTER);
    contentPane.add(adminLabel, BorderLayout.NORTH);
    //TODO : implement in this line
    //새로운 contentPane으로 설정
    frame.setContentPane(contentPane);

    //새 contentPane 재배치 및 다시 그리기
    frame.revalidate();
    ;
    frame.repaint();
  }

  /**
   * pay ok after insert_card
   *
   * @param status - item code
   */
  private void item_UI(int status) {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    JLabel adminLabel = new JLabel(drink[status], JLabel.CENTER);
    contentPane.add(adminLabel, BorderLayout.NORTH);
    JPanel bottom = new JPanel();
    JButton button = new JButton("예");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        item_list_UI(status);
      }
    });
    bottom.add(button);
    contentPane.add(adminLabel, BorderLayout.NORTH);
    contentPane.add(bottom, BorderLayout.SOUTH);
  }

  private void complete_prepay_UI(int status) {
    //TODO : implement
    //TODO : use code_and_loc
    String[] str;

    str = dvmController.code_and_loc();
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(1, 6));

    JLabel title1 = new JLabel("선결제 완료");
    JLabel body1 = new JLabel(str[0]);
    JLabel body2 = new JLabel("가장 가까운 자판기에 가서 수령하세요.");
    JLabel body3 = new JLabel(str[1]);
    JLabel body4 = new JLabel(str[2]);
    JButton button = new JButton("확인");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        item_list_UI(DEF);
      }
    });
    contentPane.add(title1);
    contentPane.add(body1);
    contentPane.add(body2);
    contentPane.add(body3);
    contentPane.add(body4);
    contentPane.add(button);

    //새로운 contentPane으로 설정
    frame.setContentPane(contentPane);

    //새 contentPane 재배치 및 다시 그리기
    frame.revalidate();
    ;
    frame.repaint();
  }

  /**
   * 원래는 status만 존재, int[] 가 추가되어 item 재고가 들어가게 됨.
   *
   * @param status
   */
  private void check_stock_UI(int status, int[] item) {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    // "관리자메뉴" 라벨 추가
    JLabel adminLabel = new JLabel("재고 확인", JLabel.CENTER);
    contentPane.add(adminLabel, BorderLayout.NORTH);

    //TODO : Implement in this line

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
    //새로운 contentPane으로 설정
    frame.setContentPane(contentPane);

    //새 contentPane 재배치 및 다시 그리기
    frame.revalidate();
    ;
    frame.repaint();
  }

  private void add_stock_UI(int status, int[] item) {
    Container contentPane = frame.getContentPane();
    contentPane.setLayout(new BorderLayout());
    if (status == DEF) {
      String[] choices = new String[20];
      for (int i = 1; i <= 20; i++) {
        choices[i - 1] = drink[i];
      }
      JComboBox<String> comboBox = new JComboBox<>(choices);
      contentPane.add(comboBox);

      JTextField numberInput = new JTextField(10);
      contentPane.add(numberInput);

      JPanel bottom = new JPanel();
      JButton button = new JButton("적용");
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          String selectedChoice = (String) comboBox.getSelectedItem();

          String numberText = numberInput.getText();

          //TODO : 에러 처리, integer이 아닌 경우
          add_item(findStringIndex(drink, selectedChoice), Integer.parseInt(numberText));
        }
      });
      contentPane.add(button);
      JButton button2 = new JButton("메뉴");
      contentPane.add(button2);
    }
    //새로운 contentPane으로 설정
    frame.setContentPane(contentPane);

    //새 contentPane 재배치 및 다시 그리기
    frame.revalidate();
    ;
    frame.repaint();
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
      this.item_list_UI(ERROR);
    } else {
      /**
       * return 0 : 재고 존재
       * return 1 : 재고 부족, 다른 곳에 재고 존재
       * return 2 : 다른 곳에 재고 부족일때
       * return 3 : count > 20, count < 0
       */
      ret = dvmStock.check_stock(item_code, count);
      if (ret == 0) {
        pay_UI(status);
      } else if (ret == 1) {
        prepay_UI(status);
      } else if (ret == 2) {
        item_list_UI(ERROR);
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
   * 1-d Insert Card, 1-e Insert Card(결제, 선결제) send_card_num -> true -> 재고 있음 / false ->재고 없음, 잔액
   * 부족(둘다 동일함)
   *
   * @param card_id : card id in user input this->status -> PAY or PREPAY Check req b
   *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                TODO : require in 1-e sequence diagram
   */
  public void insert_card(int card_id) {
    if (status == PAY) {
      if (dvmController.send_card_num(card_id)) {
        item_UI(0);
      } else {
        /**
         * 다른 카드 입력 받는 창.
         */
        pay_UI(0);
      }
    } else if (status == PREPAY) {
      if (dvmController.send_card_num(card_id)) {
        complete_prepay_UI(status);
      } else {
        item_list_UI(ERROR);
      }
    }
  }

  /**
   * 3-a Check Stock
   */
  public void check_stock() {
    int[] item;

    item = dvmStock.check_stock_all();

    check_stock_UI(this.status, item);
  }

  /**
   * 3-b Add Stock
   */
  public void show_menu() {
    admin_UI(this.status);
  }

  public void add_item_menu() {
    int[] item;

    item = dvmStock.check_stock_all();
    add_stock_UI(DEF, item);
  }

  /**
   * Add item in Administer mode
   *
   * @param item_code : add item code
   * @param stock     : add item amount if add_item_admin is true : show add else : show fail add
   */
  public void add_item(int item_code, int stock) {
    int[] item;
    if (dvmStock.add_item(item_code, stock)) {
      item = dvmStock.check_stock_all();
      check_stock_UI(DEF, item);
    } else {
      item = dvmStock.check_stock_all();
      check_stock_UI(DEF, item);
    }
  }
}
