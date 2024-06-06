import java.io.*;
import java.net.*;
import java.util.HashMap;

import org.json.JSONObject;
//예제 파일 합치기
import com.google.gson.Gson;

public class DVMController {
    private JSONObject stock_msg_JSON;
    private JSONObject prepayment_msg_JSON;
    private final int[] coord_xy; //주어진 우리 DVM 좌표
    private HashMap<String, int[]> other_dvm_coord;
    private HashMap<String, JSONObject> other_dvm_stock;
    private String verify_codes[];// 새로 추가된 인증코드용 배열
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
        price = new int[20];
        stock_msg_JSON = new JSONObject();
        prepayment_msg_JSON = new JSONObject();
        coord_xy = new int[]{27, 80};
        other_dvm_coord = new HashMap<String, int[]>();
        other_dvm_stock = new HashMap<String, JSONObject>();
        verify_codes = new String[100];
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
                        .put("item_num", count)
                );
        req_stock_msg(stock_msg_JSON);
        return true;
    }

    public boolean send_code(String verify_code) {
        for(int i=0; i<verify_codes.length; i++) {
            if(verify_code.equals(verify_codes[i])){
                return true;
            }
        }
        return false;
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
    private final String HOST = "localhost";
    private final Gson gson = new Gson();

    /**
     * 우리 DVM 서버에서 지속적으로 Thread로 돌면서 stock_msg 통신 받아서 응답하는 함수
     * stock msg인지 prepay msg인지 JSON 항목으로 구분
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
                    JSONObject res_msg = new JSONObject();

                    //prepayment 메시지일 경우, 함수로 던져줌.
                    if (other_dvm_msg.get("msg_type").equals("req_prepay")) {
                        res_msg = res_prepayment_msg(other_dvm_msg);

                    } else if (other_dvm_msg.get("msg_type").equals("req_stock")) {
                        String res_item_code = other_dvm_msg.getJSONObject("msg_content").get("item_code").toString();
                        int[] our_item_counts = dvmStock.check_stock_all();

                        res_msg.put("msg_type", "resp_stock")
                                .put("src_id", "Team6")
                                .put("dst_id", "0")
                                .put("msg_content", new JSONObject()
                                        .put("item_code", Integer.parseInt(res_item_code))
                                        .put("item_num", our_item_counts[Integer.parseInt(res_item_code) - 1])
                                        .put("coor_x", coord_xy[0])
                                        .put("coor_y", coord_xy[1])
                                );
                    } else {
                        continue;
                    }

                    writer.println(res_msg);

                    try {
                        writer.close();
                        reader.close();
                        clientSocket.close();
                        //System.out.println("server end");
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
     * 다른 DVM의 선결제 요청에 대한 우리 DVM의 응답 | 선결제 가능할 경우 재고 감소
     */
    private JSONObject res_prepayment_msg(JSONObject other_dvm_msg) {
        int res_item_code = Integer.parseInt(other_dvm_msg.getJSONObject("msg_content").get("item_code").toString());
        int res_item_num = Integer.parseInt(other_dvm_msg.getJSONObject("msg_content").get("item_num").toString());
        int[] our_item_counts = dvmStock.check_stock_all();

        boolean possible_prepay = false;
        if (our_item_counts[res_item_code - 1] >= res_item_num) {
            dvmStock.check_stock(res_item_code, res_item_num);
            possible_prepay = true;
        }

        JSONObject res_msg = new JSONObject();
        res_msg.put("msg_type", "resp_prepay")
                .put("src_id", "Team6")
                .put("dst_id", other_dvm_msg.get("src_id"))
                .put("msg_content", new JSONObject()
                        .put("item_code", res_item_code)
                        .put("item_num", our_item_counts[res_item_code - 1]) //[질문] 선결제 성공 했다면, 차감된 재고를 보내야 하는지, 차감되기 전 재고를 보내야 하는지?
                        .put("availability", possible_prepay ? "T" : "F")
                );

        return res_msg;
    }


    /**
     * 다른 DVM에 stock msg 보내는 함수
     */
    private JSONObject req_stock_msg(JSONObject msg) { //broad cast 문제
        Thread clientThread = new Thread(() -> {

            try (Socket socket = new Socket(HOST, PORT)) {
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
