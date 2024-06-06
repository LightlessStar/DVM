import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONObject;
//예제 파일 합치기
import com.google.gson.Gson;

public class DVMController {
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
        price = new int[20];
        stock_msg_JSON = new JSONObject();
        prepayment_msg_JSON = new JSONObject();
        coord_xy = new int[]{27, 80};
        other_dvm_coord = new HashMap<String, int[]>();
        other_dvm_stock = new HashMap<String, Integer>();
        verify_codes = new String[100];
        code_stock = new HashMap<>();
        bank = new Bank();
        for (int i = 0; i < price.length; i++) {
            price[i] = 500;
        }
        dvmStock = new DVMStock();
    }

    public DVMController(DVMStock dvmStock) {
        price = new int[20];
        stock_msg_JSON = new JSONObject();
        prepayment_msg_JSON = new JSONObject();
        coord_xy = new int[]{27, 80};
        other_dvm_coord = new HashMap<String, int[]>();
        other_dvm_stock = new HashMap<String, Integer>();
        verify_codes = new String[100];
        code_stock = new HashMap<>();
        bank = new Bank();
        for (int i = 0; i < price.length; i++) {
            price[i] = 500;
        }
        this.dvmStock = dvmStock;
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

        //broadcast일 경우 HOST, POST 바꾸거나 for 돌리기!
        req_stock_msg(stock_msg_JSON, HOST, CLIENT_PORT);
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

    public boolean send_card_num(int card_id, int charge) {
        System.out.println("send_card_num");
        if (bank.certify_pay(card_id, charge)) {
            return true;
        }
        return false;
    }

    public boolean cancel_prepay(int card_id, int charge) {
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

    private final int SERVER_PORT = 30303;  //우리 컴퓨터에 열릴 포트
    private final int CLIENT_PORT = 42424;  //남의 컴퓨터로 보낼 포트
    private final String HOST = "localhost";    //남의 컴퓨터 주소!
    private final Gson gson = new Gson();

    /**
     * 우리 DVM 서버에서 지속적으로 Thread로 돌면서 stock_msg 통신 받아서 응답하는 함수
     * stock msg인지 prepay msg인지 JSON 항목으로 구분
     */
    public JSONObject res_stock_msg() {
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
                System.out.println("Server is listening on PORT " + SERVER_PORT);
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
    private JSONObject req_stock_msg(JSONObject msg, String HOST, int CLIENT_PORT) { //broad cast 문제
        Thread clientThread = new Thread(() -> {
            try (Socket socket = new Socket(HOST, CLIENT_PORT)) {
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
                System.out.println("Client : send to server" + msg);

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
    public String[] prepay_info(int item, int count) {
        //item 보유 중이고, 가장 거리가 가까운 dvm id 선별
        //충분한 item 있는 거만 뽑아서 따로 배열 만듦.
        HashMap<String, Integer> item_owner_so_many = new HashMap<>();
        for (Map.Entry<String, Integer> entry : other_dvm_stock.entrySet()) {
            if (entry.getValue() >= count) {
                item_owner_so_many.put(entry.getKey(), entry.getValue());
            }
        }

        //가장 거리 가까운 거 dvm 뽑기
        String[] ret_str = new String[5];
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
                        ret_str[3] = Integer.toString(coord[0]);
                        ret_str[4] = Integer.toString(coord[1]);
                    } else if (min_distance > distance) {
                        min_distance = distance;
                        dst_dvm_id = entry_coord.getKey();
                        ret_str[3] = Integer.toString(coord[0]);
                        ret_str[4] = Integer.toString(coord[1]);
                    }
                }
            }
        }


        //min_distance가 -1이면 선결제 불가능
        if (min_distance == -1) {
            ret_str[0] = "0";
            return ret_str;
        } else {

            // 랜덤 코드 생성기! 가능한 문자열의 글자
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sb = new StringBuilder();
            // 랜덤 객체 생성
            Random random = new Random();
            // 주어진 길이만큼 반복하여 랜덤 문자열 생성
            for (int i = 0; i < 10; i++) {
                // characters 문자열에서 랜덤한 글자 선택하여 sb에 추가
                int randomIndex = random.nextInt(characters.length());
                sb.append(characters.charAt(randomIndex));
            }

            ret_str[0] = sb.toString();
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

            if (req_prepayment_msg(prepayment_msg_JSON)) {
                return ret_str;
            } else {
                ret_str[0] = "0";
                return ret_str;
            }
        }
        //선결제 불가능하면 return string[0] = "0" 으로 return
        //선결제 가능하면 string[0] 코드 string[1] 팀명, x y string[2] 거리
    }

    /**
     * 다른 DVM에 prepay 보내는 함수
     */
    private boolean req_prepayment_msg(JSONObject prepayment_msg_JSON) {
        AtomicBoolean possible_prepay = new AtomicBoolean(false);
        Thread clientThread = new Thread(() -> {
            try (Socket socket = new Socket(HOST, CLIENT_PORT)) {
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

                //선결제 요청을 보내서, 선결제가 가능한지 여부 받아오기
                //boolean possible_prepay = false;
                possible_prepay.set(response_other_dvm.getJSONObject("msg_content").get("availability").equals("T") ? true : false);


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

        try {
            clientThread.join();

            return possible_prepay.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
