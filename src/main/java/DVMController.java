import java.io.*;
import java.net.*;
import java.util.HashMap;
import org.json.JSONObject;
//예제 파일 합치기
import com.google.gson.Gson;

public class DVMController {
    private String verify_code;
    private int[] price; //결제 금액 저장용 변수
    private JSONObject stock_msg_JSON;
    private JSONObject prepayment_msg_JSON;
    private final int[] coord_xy; //주어진 우리 DVM 좌표
    private HashMap<String, int[]> other_dvm_coord;
    private HashMap<String, JSONObject> other_dvm_stock;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Bank bank; //이거 왜 없어ㅓㅓㅓㅓㅓDCD에 넣어야 해

    public DVMController() {
        verify_code = "";
        price = new int[20];
        stock_msg_JSON = new JSONObject();
        coord_xy = new int[] {27,80};
        other_dvm_coord = new HashMap<String, int[]>();
        other_dvm_stock = new HashMap<String, JSONObject>();
        bank = new Bank();
        for(int i = 0; i<price.length; i++){
            price[i] = 500;
        }
    }
    public boolean request_stock_msg(int item_code, int count){

        JSONObject item_JSON = new JSONObject();
        stock_msg_JSON.put("msg_type","req_stock");
        stock_msg_JSON.put("src_id","Team6");
        stock_msg_JSON.put("dst_id","0");
        item_JSON.put("item_code",item_code);
        item_JSON.put("count",count);
        stock_msg_JSON.put("msg_content", item_JSON);
        JSONObject temp = new JSONObject();
        temp = stock_msg_JSON.getJSONObject("msg_content");
        req_stock_msg(stock_msg_JSON);
        return true;
    }
    public boolean send_code(String verify_code){

        return true;
    }
    public boolean enter_code(String verify_code) {
        return true;
    }
    public boolean send_card_num(int card_id){
        int charge = 1000000;
        if (bank.certify_pay(card_id, charge)){

        }
        return true;
    }

    private final int PORT = 30303;

//    public void runServer() {
//        Thread serverThread = new Thread(() -> {
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("start server");
//
//            // 클라이언트로부터 연결을 대기하고, 연결되면 소켓을 생성합니다.
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("connect client");
//
//            // 클라이언트로부터 받은 데이터를 읽어서 화면에 출력합니다.
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                System.out.println("client msg: " + line);
//
//                // JSON 형식의 문자열을 JSONObject로 변환합니다.
//                JSONObject receivedJson = new JSONObject(line);
//
//                // JSONObject에서 원하는 데이터를 추출합니다.
//                String name = receivedJson.getString("name");
//                int age = receivedJson.getInt("age");
//
//                System.out.println("name: " + name);
//                System.out.println("age: " + age);
//            }
//
//            // 클라이언트와의 연결을 종료합니다.
//            clientSocket.close();
//            System.out.println("client connect finish");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        });
//        serverThread.start();
//    }
//
//    public void runClient() {
//        Thread clientThread = new Thread(() -> {
//            try (Socket socket = new Socket("localhost", PORT)) {
//                System.out.println("connected server");
//
//                // 서버로 보낼 JSON 데이터를 생성합니다.
//                JSONObject jsonToSend = new JSONObject();
//                jsonToSend.put("name", "John Doe");
//                jsonToSend.put("age", 30);
//
//                // 서버로 데이터를 전송합니다.
//                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                out.println(jsonToSend.toString());
//
//                // 서버에서 받은 응답을 읽어서 화면에 출력합니다.
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String response = in.readLine();
//                System.out.println("response from server: " + response);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        clientThread.start();
//    }



//    public class JsonSocketServiceImpl {
//        private Socket socket;
//
//
//        public JsonSocketServiceImpl(Socket socket) {
//            this.socket = socket;
//        }
//
//        public void start() {
//            try {
//                this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
//                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//            } catch (Exception e) {
//                throw new RuntimeException("Error initializing streams", e);
//            }
//        }
//
//        public void stop() {
//            try {
//                writer.close();
//                reader.close();
//                socket.close();
//            } catch (Exception e) {
//                throw new RuntimeException("Error closing streams", e);
//            }
//        }
//
//
//        public void sendMessage(Object message) {
//            writer.println(gson.toJson(message));
//        }
//
//
//        public <T> T receiveMessage(Class<T> clazz) {
//            try {
//                return gson.fromJson(reader.readLine(), clazz);
//            } catch (Exception e) {
//                throw new RuntimeException("Error receiving message", e);
//            }
//        }
//    }


    private PrintWriter writer;
    private BufferedReader reader;
    private final Gson gson = new Gson();

    public JSONObject res_stock_msg(){
        Thread serverThread = new Thread(() -> {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on PORT " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    this.writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
                    this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                } catch (Exception e) {
                    throw new RuntimeException("Error initializing streams", e);
                }
                // 여기서 클라이언트로부터 메시지를 받고, 응답을 보낼 수 있습니다.

                String other_dvm_msg = reader.readLine();
                System.out.println("i'm server : receive msg " + other_dvm_msg);

                JSONObject res_msg = new JSONObject();
                JSONObject item_JSON = new JSONObject();
                res_msg.put("msg_type","req_stock");
                res_msg.put("src_id","Team6");
                res_msg.put("dst_id","0");
                item_JSON.put("item_code",20);
                item_JSON.put("count",10);
                item_JSON.put("coor_x", coord_xy[0]);
                item_JSON.put("coor_y", coord_xy[1]);
                res_msg.put("msg_content", item_JSON);
                writer.println(res_msg);


//                try {
//                    writer.close();
//                    reader.close();
//                    clientSocket.close();
//                } catch (Exception e) {
//                    throw new RuntimeException("Error closing streams", e);
//                }
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
        });
        serverThread.start();
        return null;
    }
    private JSONObject req_stock_msg(JSONObject msg){
        Thread clientThread = new Thread(() -> {

        try (Socket socket = new Socket("localhost", PORT)) {
            try {
                this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
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
            other_dvm_stock.put(msg_content.get("src_id").toString(),msg_content);
            int[] coor = new int[2];
            coor[0] =  msg_content.getJSONObject("msg_content").getInt("coor_x");
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
