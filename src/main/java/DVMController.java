import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
//예제 파일 합치기
import com.google.gson.Gson;

public class DVMController {

    private String verify_code;
    private JSONObject stock_msg_JSON;
    private JSONObject prepayment_msg_JSON;
    private final int[] coord_xy; //주어진 우리 DVM 좌표
    private HashMap<String, int[]> other_dvm_coord;
    private HashMap<String, Integer> other_dvm_stock;
  
    private String verify_codes[];// 새로 추가된 인증코드용 배열
    //Socket 통신 관련 변수는 메서드 안에서만 쓴다면 DCD에서 제외해도 될 듯
    private ServerSocket socket;
    private InputStream in;
    private OutputStream out;

    //DCD에 없는 것들
    private Bank bank; //이거 왜 없어ㅓㅓㅓㅓㅓDCD에 넣어야 해
    private int[] price; //결제 금액 저장용 변수

    private HashMap<String, Integer> code_stock;
    private DVMStock dvmStock;
    private int tmp_item;
    private int tmp_count;

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
        other_dvm_stock = new HashMap<String, Integer>();
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
                        .put("count", count)
                );
        req_stock_msg(stock_msg_JSON);
        return true;
    }

    public int send_code(String verify_code) {
        if (code_stock.containsKey(verify_code)) {
            Integer ret = code_stock.get(verify_code);
            code_stock.remove(verify_code);
            return ret;
        } else {
            return 0;
        }
//        if (this.verify_code.equals(verify_code)) {
//            return true;
//        } else {
//            return false;
//        }
    }

    public boolean enter_code(String verify_code) {
        return true;
    }

    public boolean send_code(String verify_code) {
        for (int i = 0; i < verify_codes.length; i++) {
            if (verify_code.equals(verify_codes[i])) {
                return true;
            }
        }
        return false;
    }

    public String[] prepay_info(int item, int count) {
        //TODO : req_prepayment_msg() 구현 필요
        //TODO : ret string 구현 필요.
        JSONObject prepayment_msg_JSON = new JSONObject();
        this.tmp_item = item;
        this.tmp_count = count;
        String[] ret;
        return ret;
    }

    public boolean cancle_prepay(int card_id, int charge) {
        if (bank.cancel_pay(card_id, charge)) {
            if (dvmStock.add_item(tmp_item, tmp_count)) {
                tmp_item = 0;
                tmp_count = 0;
                return true;
            }
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
                    System.out.println("Server : receive item_code, count" + other_dvm_msg.get());


                    JSONObject res_msg = new JSONObject();
                    res_msg.put("msg_type", "req_stock")
                            .put("src_id", "Team6")
                            .put("dst_id", "0")
                            .put("msg_content", new JSONObject()
                                    .put("item_code", 20)
                                    .put("count", 10)
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

                JSONObject response_other_dvm = new JSONObject(reader.readLine());
                System.out.println("Client : server res receive!!" + response_other_dvm);

                //응답 받은 것을 저장
                other_dvm_stock.put(response_other_dvm.get("src_id").toString(), Integer.parseInt(response_other_dvm.getJSONObject("msg_content").get("item_num").toString()));
                int[] coor = new int[2];
                coor[0] = Integer.parseInt(response_other_dvm.getJSONObject("msg_content").get("coor_x").toString());
                coor[1] = Integer.parseInt(response_other_dvm.getJSONObject("msg_content").get("coor_y").toString());
                other_dvm_coord.put(response_other_dvm.get("src_id").toString(), coor);

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


    /**
     * 1-e 선결제 -> item, count
     */
    private String[] prepay_info(int item, int count) {
        //item 보유 중이고, 가장 거리가 가까운 dvm id 선별
        //충분한 item 있는 거만 뽑아서 따로 배열 만듦.
        HashMap<String, Integer> item_owner_so_many = new HashMap<>();
        for (Map.Entry<String, Integer> entry : other_dvm_stock.entrySet()) {
            if (entry.getValue() >= count) {
                item_owner_so_many.put(entry.getKey(), entry.getValue());
            }
        }

        //가장 거리 가까운 거 dvm 뽑기
        String dst_dvm_id = "";
        double min_distance = -1;
        for (Map.Entry<String, int[]> entry_coord : other_dvm_coord.entrySet()) {
            for (Map.Entry<String, Integer> entry_stock : item_owner_so_many.entrySet()) {
                //좌표 저장된 것 중 충분한 item 많은 거 dvm만 탐색.
                if (entry_coord.getKey().equals(entry_stock.getKey())) {
                    //최단거리 계산
                    int[] coord = entry_coord.getValue();
                    int del_x = coord[0] - coord_xy[0];
                    int del_y = coord[1] - coord_xy[1];
                    double distance = Math.sqrt(del_x * del_x + del_y * del_y);

                    if (min_distance == -1) {
                        min_distance = distance;
                        dst_dvm_id = entry_coord.getKey();
                    } else if (min_distance > distance) {
                        min_distance = distance;
                        dst_dvm_id = entry_coord.getKey();
                    }
                }
            }
        }

        String[] ret_str = new String[3];
        //min_distance가 -1이면 선결제 불가능
        if (min_distance == -1) {
            ret_str[0] = "0";
            return ret_str;
        } else {
            ret_str[0] = "";
            ret_str[1] = dst_dvm_id;
            ret_str[2] = Double.toString(min_distance);
            prepayment_msg_JSON = new JSONObject()
                    .put("msg_type", "req_prepay")
                    .put("src_id", "Team6")
                    .put("dst_id", dst_dvm_id)
                    .put("msg_content", new JSONObject()
                            .put("item_code", item)
                            .put("item_num", count)
                            .put("cert_code", ret_str[0])
                    );

            req_prepayment_msg(prepayment_msg_JSON);

            return ret_str;
        }
        //선결제 불가능하면 return string[0] = "0" 으로 return
        //선결제 가능하면 string[0] 코드 string[1] 팀명, x y string[2] 거리
    }

    /**
     * 다른 DVM에 prepay 보내는 함수
     */
    private JSONObject req_prepayment_msg(JSONObject prepayment_msg_JSON) {
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
                writer.println(prepayment_msg_JSON);
                System.out.println("Client : send to server" + prepayment_msg_JSON);

                JSONObject response_other_dvm = new JSONObject(reader.readLine());
                System.out.println("Client : server res receive!!" + response_other_dvm);


                //응답 받은 것을 저장
                other_dvm_stock.put(response_other_dvm.get("src_id").toString(), response_other_dvm);
                int[] coor = new int[2];
                coor[0] = Integer.parseInt(response_other_dvm.getJSONObject("msg_content").get("coor_x").toString());
                coor[1] = Integer.parseInt(response_other_dvm.getJSONObject("msg_content").get("coor_y").toString());
                other_dvm_coord.put(response_other_dvm.get("src_id").toString(), coor);

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
