import org.json.JSONObject;
import sun.security.x509.IPAddressName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DVMUI extends JFrame {

    final int ERROR = -1;
    final int DEF = 0;
    final int PAY = 100;
    final int PREPAY = 200;
    final int NULLABLE = 300;
    final private String[] drink = {
            "없음", "콜라", "사이다",
            "녹차", "홍차", "밀크티", "탄산수",
            "보리차", "캔커피", "물", "에너지드링크",
            "유자차", "식혜", "아이스티", "딸기주스",
            "오렌지주스", "포도주스", "이온음료", "아메리카노",
            "핫초코", "카페라떼"
    };
    final private Integer[] price = {
            0, 2000, 2000, 1500,
            1500, 2000, 1500, 1500,
            1000, 1000, 2500, 2000,
            1000, 2500, 3000, 3000,
            3000, 2000, 2000, 1000,
            2500, 1000
    };
    private int status;
    final private JFrame frame;
    private DVMStock dvmStock;
    private DVMController dvmController;
    private String[] str;
    private int tmp_item;
    private int tmp_count;
    private static String[] tmp_coord;

    private static int find_string_index(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    public DVMUI(DVMStock dvmStock) {
        this.dvmStock = dvmStock;
        this.dvmController = new DVMController();
        frame = new JFrame("DVM TEAM6");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        item_list_UI(DEF);
        frame.setVisible(true); // 화면에 프레임 출력
    }

    private void item_list_UI(int status) {
        Container contentPane = frame.getContentPane();
        JLabel title = new JLabel("DVM TEAM6");
        contentPane.setLayout(new GridLayout(4, 1));
        contentPane.removeAll();
        //4 * 5 짜리 jpanel을 만들어 jframe에 부착하는 작업.
        if (status == ERROR) {
            title.setText("결제에 실패했습니다. 다시 확인해주세요");
        } else if (status == PREPAY) {
            title.setText("좌표는 " + tmp_coord[1] + "입니다  x : " + tmp_coord[3] + ", y : " + tmp_coord[4] + "에 접근해주세요");
        } else if (status == NULLABLE) {
            title.setText("존재하는 음료 재고가 아예 없습니다. 다시 확인해주세요");
        }
        contentPane.add(title);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        JLabel choice = new JLabel("음료 개수를 선택해주세요");
        JTextField text = new JTextField(10);
        panel.add(choice);
        panel.add(text);
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new GridLayout(4, 5));
        for (int i = 1; i <= 20; i++) {
            JButton button = new JButton(drink[i]);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton b = (JButton) e.getSource();
                    String buttonText = b.getText();
                    int itemCode = find_string_index(drink, buttonText);
                    int count = Integer.parseInt(text.getText());
                    select_item(itemCode, count);
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
        contentPane.add(buttonPanel1, BorderLayout.NORTH);
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(buttonPanel2, BorderLayout.SOUTH);

        //새로운 contentPane으로 설정
        frame.setContentPane(contentPane);

        //새 contentPane 재배치 및 다시 그리기
        frame.revalidate();
        frame.repaint();
    }

    /**
     * 1-b Select Item 1-e 선결제 파트가 실제로 진행되어야 함.
     *
     * @param status - 1 ~ 20 : drink status
     */
    private void prepay_UI(int status) {
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new GridLayout(2, 1));
        String drinkSomething = drink[status];

        String printTitle = "현재 재고가 부족합니다.";
        String printSub = "선결제를 진행하고 구매할 수 있습니다.\n " + drinkSomething;
        String cardTitle = "카드 번호를 입력해주세요";

        JPanel top = new JPanel(new GridLayout(4, 1));
        JLabel title = new JLabel(printTitle);
        JLabel subTitle = new JLabel(printSub);
        JLabel card = new JLabel(cardTitle);
        JTextField cardId = new JTextField();
        top.add(title);
        top.add(subTitle);
        top.add(card);
        top.add(cardId);

        JPanel bottom = new JPanel(new GridLayout(1, 2));
        JButton button1 = new JButton("구매");
        this.status = PREPAY;
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int card_id = Integer.parseInt(cardId.getText());
                System.out.println(card_id);
                insert_card(card_id);
            }
        });
        //그 구현상에는 취소버튼이 없어...
        JButton button2 = new JButton("직접 구매");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                item_list_UI(PREPAY);
            }
        });
        bottom.add(button1);
        bottom.add(button2);

        contentPane.add(top);
        contentPane.add(bottom);
        //새로운 contentPane으로 설정
        frame.setContentPane(contentPane);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * 1-b Select Item
     * 1-d Insert Card
     *
     * @param status PAY - normal pay condition ERROR - error
     */
    private void pay_UI(int status) {
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());

        if (status == DEF) {
            JLabel adminLabel = new JLabel("카드 입력");
            contentPane.add(adminLabel, BorderLayout.NORTH);
            JTextField cardId = new JTextField();
            contentPane.add(cardId, BorderLayout.CENTER);
            JButton button = new JButton("버튼을 눌러 카드를 입력해주세요");
            this.status = PAY;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int card_id = Integer.parseInt(cardId.getText());
                    insert_card(card_id);
                }
            });
            contentPane.add(button, BorderLayout.SOUTH);
        } else if (status == ERROR) {
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
        frame.repaint();
    }

    private void code_verify_UI(int status) {
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new GridLayout(3, 1));

        JLabel adminLabel = new JLabel("선결제 코드 확인");
        contentPane.add(adminLabel);

        JTextField prepayCode = new JTextField();

        JButton submit = new JButton("선결제 번호 입력");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = prepayCode.getText();
                enter_code(code);
            }
        });

        contentPane.add(prepayCode, BorderLayout.CENTER);
        contentPane.add(submit, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * pay ok after insert_card or After code input is ok
     *
     * @param status - item code
     */
    private void item_UI(int status) {
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        JLabel adminLabel = new JLabel(drink[tmp_item] + ", " + tmp_count + "개 구매를 완료했습니다.");
        panel.add(adminLabel);
        JLabel subtitleLabel = new JLabel("자판기에서 꺼내가주세요");
        panel.add(subtitleLabel);

        contentPane.add(panel, BorderLayout.NORTH);
        JPanel bottom = new JPanel();
        JButton button = new JButton("음료 선택 화면으로 돌아가기");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                item_list_UI(status);
            }
        });
        bottom.add(button);
        contentPane.add(adminLabel, BorderLayout.NORTH);
        contentPane.add(bottom, BorderLayout.SOUTH);

        //새로운 contentPane으로 설정
        frame.setContentPane(contentPane);

        //새 contentPane 재배치 및 다시 그리기
        frame.revalidate();
        frame.repaint();
    }

    private void complete_prepay_UI(int status) {
        /**
         * str
         * 선결제 가능하면 string[0] 코드 string[1] 팀명, x y string[2] 거리,
         *
         * string[3] : x좌표 / string[4] : y좌표  추가로 담아 보낼게여
         */
        String[] str = this.str;

        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new GridLayout(3, 2));

        JLabel title1 = new JLabel("선결제 완료");
        JLabel body1 = new JLabel("코드 : " + str[0]);
        JLabel body2 = new JLabel("자판기 명");
        JLabel body3 = new JLabel(str[1]);
        JLabel body4 = new JLabel("거리는 " + str[2] + " 좌표는 " + str[3] + ", " + str[4] + "입니다");
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

        frame.revalidate();
        frame.repaint();
    }

    /**
     * 원래는 status만 존재, int[] 가 추가되어 item 재고가 들어가게 됨.
     *
     * @param status
     */
    private void check_stock_UI(int status, int[] item) {
        Container contentPane = getContentPane();
        //removeAll이 없어서 깨지는 현상이 있었음.
        contentPane.removeAll();
        contentPane.setLayout(new GridLayout(3, 1));

        // "관리자메뉴" 라벨 추가
        if (status == DEF) {
            JLabel adminLabel = new JLabel("재고 확인", JLabel.CENTER);
            contentPane.add(adminLabel, BorderLayout.NORTH);
        } else {
            JLabel adminLabel = new JLabel("재고가 추가되지 않았습니다. 다시해주세요.", JLabel.CENTER);
            contentPane.add(adminLabel, BorderLayout.NORTH);
        }
        JPanel stock = new JPanel();
        stock.setLayout(new GridLayout(4, 5));
        for (int i = 1; i <= 20; i++) {
            JLabel eachStock = new JLabel(drink[i] + " : " + item[i - 1]);
            stock.add(eachStock);
        }
        JPanel bottom = new JPanel();
        JButton button1 = new JButton("돌아가기");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show_menu();
            }
        });
        JButton button2 = new JButton("음료 선택으로 돌아가기");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                item_list_UI(DEF);
            }
        });
        bottom.add(button1);
        bottom.add(button2);
        contentPane.add(stock);
        contentPane.add(bottom);

        frame.setContentPane(contentPane);
        frame.revalidate();
        frame.repaint();
    }

    private void admin_UI(int status) {
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());

        // "관리자메뉴" 라벨 추가
        JLabel adminLabel = new JLabel("관리자메뉴");
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
        frame.repaint();
    }

    private void add_stock_UI(int status, int[] item) {
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        if (status == DEF) {
            String[] choices = new String[20];
            for (int i = 1; i <= 20; i++) {
                choices[i - 1] = drink[i];
            }
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 2));
            JComboBox<String> comboBox = new JComboBox<>(choices);
            panel.add(comboBox);

            JTextField numberInput = new JTextField(10);
            panel.add(numberInput);

            contentPane.add(panel, BorderLayout.CENTER);
            JPanel bottom = new JPanel();
            bottom.setLayout(new GridLayout(1, 2));
            JButton button = new JButton("적용");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedChoice = (String) comboBox.getSelectedItem();

                    String numberText = numberInput.getText();

                    //TODO : 에러 처리, integer이 아닌 경우
                    add_item(find_string_index(drink, selectedChoice), Integer.parseInt(numberText));
                }
            });
            bottom.add(button);
            JButton button2 = new JButton("돌아가기");
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    admin_UI(DEF);
                }
            });
            bottom.add(button2);
            contentPane.add(bottom, BorderLayout.SOUTH);
        }
        //새로운 contentPane으로 설정
        frame.setContentPane(contentPane);

        //새 contentPane 재배치 및 다시 그리기
        frame.revalidate();
        frame.repaint();
    }

    /**
     * 1-b Select Item
     *
     * @param item_code : Drink Code, 1 ~ 20, Matching one by one
     * @param count     : Drink Amount, 1 ~ 20, or Error Occur
     */
    public void select_item(int item_code, int count) {
        int ret;

//        charge = price[item_code] * count;
        this.tmp_item = item_code;
        this.tmp_count = count;
        /**
         * return 0 : 재고 부족, 다른 곳에 재고 존재
         * return 1 : 재고 존재
         * return -1 : 입력 값이 잘못되었을 때
         */
        ret = dvmStock.check_stock(item_code, count);
        if (ret == 0) {
            /**
             * request_stock_msg -> -1, 0, 1
             */
            this.tmp_coord = dvmController.request_stock_msg(tmp_item, tmp_count);

            if (tmp_coord[0].equals("1")) {
                prepay_UI(item_code);
            } else {
                item_list_UI(NULLABLE);
            }
        } else if (ret == 1) {
            pay_UI(DEF);
        } else if (ret == -1) {
            this.item_list_UI(ERROR);
        }
    }

    /**
     * 1-c Pay by Verification Code
     */
    public void code_verify_menu() {
        code_verify_UI(0);
    }

    public void enter_code(String verify_code) {
        int item_code = dvmController.send_code(verify_code);
        tmp_item = item_code % 100;
        tmp_count = item_code / 100;
        if (item_code > 0) {
            item_UI(item_code);
        } else {
            item_list_UI(ERROR);
        }
    }

    /**
     * 1-d Insert Card, 1-e Insert Card(결제, 선결제) send_card_num -> true -> 재고 있음 / false ->재고 없음, 잔액
     * 부족(둘다 동일함)
     *
     * @param card_id : card id in user input this->status -> PAY or PREPAY Check req b
     *                : require in 1-e sequence diagram
     */
    public void insert_card(int card_id) {
        int charge = price[tmp_item] * tmp_count;
        if (status == PAY) {
            if (dvmController.send_card_num(card_id, charge)) {
                item_UI(DEF);
            } else {
                /**
                 * 다른 카드 입력 받는 창.
                 */
                add_item(tmp_item, tmp_count);
                pay_UI(ERROR);
            }
        } else if (status == PREPAY) {
            if (dvmController.send_card_num(card_id, charge)) {
                this.str = dvmController.prepay_info(tmp_item, tmp_count);
                if (str[0].equals("0")) {
                    if (dvmController.cancel_prepay(card_id, charge) == true) {
                        item_list_UI(ERROR);
                    } else {
                        System.exit(1);
                    }
                } else {
                    complete_prepay_UI(tmp_item);
                }
            } else {
                add_item(tmp_item, tmp_count);
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
        boolean add_bool = dvmStock.add_item(item_code, stock);
        item = dvmStock.check_stock_all();
        if (add_bool) {
            check_stock_UI(DEF, item);
        } else {
            check_stock_UI(ERROR, item);
        }
    }
}