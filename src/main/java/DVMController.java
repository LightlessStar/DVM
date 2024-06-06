import java.io.*;
import java.net.*;
import java.util.HashMap;

import org.json.JSONObject;
//예제 파일 합치기
import com.google.gson.Gson;

public class DVMController {
    private String verify_code;
    private JSONObject stock_msg_JSON;
    private JSONObject prepayment_msg_JSON;
    private final int[] coord_xy; //주어진 우리 DVM 좌표
    private HashMap<String, int[]> other_dvm_coord;
    private HashMap<String, JSONObject> other_dvm_stock;

    //Socket 통신 관련 변수는 메서드 안에서만 쓴다면 DCD에서 제외해도 될 듯
    private ServerSocket socket;
    private InputStream in;
    private OutputStream out;

    //DCD에 없는 것들
    private Bank bank; //이거 왜 없어ㅓㅓㅓㅓㅓDCD에 넣어야 해
    private int[] price; //결제 금액 저장용 변수
    private DVMStock dvmStock = new DVMStock();

    /**
     * 생성자. 기본 변수들 초기화
     */
    public DVMController() {
        verify_code = "";
        price = new int[20];
        stock_msg_JSON = new JSONObject();
        prepayment_msg_JSON = new JSONObject();
        coord_xy = new int[]{27, 80};
        other_dvm_coord = new HashMap<String, int[]>();
        other_dvm_stock = new HashMap<String, JSONObject>();
        bank = new Bank();
        for (int i = 0; i < price.length; i++) {
            price[i] = 500;
        }
    }

    /**
     * 1-b select item DVMUI가 호출하는 함수
     * stock_msg_JSON 만들어서 브로드캐스팅으로 전송한다.
     */
    public boolean request_stock_msg(int item_code, int count) {
        stock_msg_JSON = new JSONObject()
                .put("msg_type", "req_stock")
                .put("src_id", "Team6")
                .put("dst_id", "0")
                .put("msg_content", new JSONObject()
                        .put("item_code", item_code)
                        .put("count", count)
                );
        req_stock_msg(stock_msg_JSON);
        return true;
    }

    public boolean send_code(String verify_code) {
        if (this.verify_code.equals(verify_code)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean enter_code(String verify_code) {
        return true;
    }

    public boolean send_card_num(int card_id) {
        if (bank.certify_pay(card_id, 0)) {
            return true;
        }
        return false;
    }

    /**
     * 선결제 완료된 후, 선결제코드, 팀명, x, y 좌표 보내주기
     */
    public String[] code_and_loc() {
        String[] code_team_xy = new String[4];
        return code_team_xy;
    }

    private final int PORT = 30303;
    private final Gson gson = new Gson();

    /**
     * 우리 DVM 서버에서 지속적으로 Thread로 돌면서 stock_msg 통신 받아서 응답하는 함수
     */
    public JSONObject res_stock_msg() {
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server is listening on PORT " + PORT);
                PrintWriter writer;
                BufferedReader reader;

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    try {
                        writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
                        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                    } catch (Exception e) {
                        throw new RuntimeException("Error initializing streams", e);
                    }
                    // 여기서 클라이언트로부터 메시지를 받고, 응답을 보낼 수 있습니다.

                    JSONObject other_dvm_msg = new JSONObject(reader.readLine());
                    System.out.println("Server : receive msg " + other_dvm_msg);

                    String res_item_code = other_dvm_msg.getJSONObject("msg_content").get("item_code").toString();
                    String res_item_count = other_dvm_msg.getJSONObject("msg_content").get("count").toString();

                    JSONObject res_msg = new JSONObject();
                    res_msg.put("msg_type", "req_stock")
                            .put("src_id", "Team6")
                            .put("dst_id", "0")
                            .put("msg_content", new JSONObject()
                                    .put("item_code", Integer.parseInt(res_item_code))
                                    .put("count", dvmStock.check_stock(Integer.parseInt(res_item_code), Integer.parseInt(res_item_count)))
                                    .put("coor_x", coord_xy[0])
                                    .put("coor_y", coord_xy[1])
                            );

                    writer.println(res_msg);


                    try {
                        writer.close();
                        reader.close();
                        clientSocket.close();
                        System.out.println("server end");
                    } catch (Exception e) {
                        throw new RuntimeException("Error closing streams", e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Server exception: " + e.getMessage());
                e.printStackTrace();
            }
        });
        serverThread.start();
        return null;
    }

    /**
     * 다른 DVM에 stock msg 보내는 함수
     */
    private JSONObject req_stock_msg(JSONObject msg) {
        Thread clientThread = new Thread(() -> {

            try (Socket socket = new Socket("localhost", PORT)) {
                PrintWriter writer;
                BufferedReader reader;
                try {
                    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                } catch (Exception e) {
                    throw new RuntimeException("Error initializing streams", e);
                }

                // 서버로 메시지를 보내고 응답을 받습니다.
                writer.println(msg);
                System.out.println("i'm client : send to server" + msg);

                String response_other_dvm = reader.readLine();
                System.out.println("i'm client : server res receive!!" + response_other_dvm);


                //Message response = service.receiveMessage(Message.class);
                JSONObject msg_content = new JSONObject(response_other_dvm);
                System.out.println(msg_content);
                //msg_content = response.getJSONObject("msg_content");
                other_dvm_stock.put(msg_content.get("src_id").toString(), msg_content);
                int[] coor = new int[2];
                coor[0] = msg_content.getJSONObject("msg_content").getInt("coor_x");
                coor[1] = msg_content.getJSONObject("msg_content").getInt("coor_y");
                other_dvm_coord.put(msg_content.get("src_id").toString(), coor);

                try {
                    writer.close();
                    reader.close();
                    socket.close();
                    System.out.println("client fin");
                } catch (Exception e) {
                    throw new RuntimeException("Error closing streams", e);
                }
//            return msg_content;
            } catch (Exception e) {
                System.out.println("Client exception: " + e.getMessage());
                e.printStackTrace();
            }
        });
        clientThread.start();
        return null;
    }
}
