import java.io.*;
import java.net.*;
import java.util.HashMap;
import org.json.JSONObject;

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
    public boolean send_card_num(int card_id){
        if (bank.certify_pay(card_id)){

        }
        return true;
    }

    private final int PORT = 30303;

    public void runServer() {
        Thread serverThread = new Thread(() -> {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("start server");

            // 클라이언트로부터 연결을 대기하고, 연결되면 소켓을 생성합니다.
            Socket clientSocket = serverSocket.accept();
            System.out.println("connect client");

            // 클라이언트로부터 받은 데이터를 읽어서 화면에 출력합니다.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("client msg: " + line);

                // JSON 형식의 문자열을 JSONObject로 변환합니다.
                JSONObject receivedJson = new JSONObject(line);

                // JSONObject에서 원하는 데이터를 추출합니다.
                String name = receivedJson.getString("name");
                int age = receivedJson.getInt("age");

                System.out.println("name: " + name);
                System.out.println("age: " + age);
            }

            // 클라이언트와의 연결을 종료합니다.
            clientSocket.close();
            System.out.println("client connect finish");
        } catch (IOException e) {
            e.printStackTrace();
        }
        });
        serverThread.start();
    }

    public void runClient() {
        Thread clientThread = new Thread(() -> {
            try (Socket socket = new Socket("localhost", PORT)) {
                System.out.println("connected server");

                // 서버로 보낼 JSON 데이터를 생성합니다.
                JSONObject jsonToSend = new JSONObject();
                jsonToSend.put("name", "John Doe");
                jsonToSend.put("age", 30);

                // 서버로 데이터를 전송합니다.
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(jsonToSend.toString());

                // 서버에서 받은 응답을 읽어서 화면에 출력합니다.
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
                System.out.println("response from server: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

    public void runClient2() {
        Thread clientThread = new Thread(() -> {
            try (Socket socket = new Socket("localhost", PORT)) {
                System.out.println("connected server");

                // 서버로 보낼 JSON 데이터를 생성합니다.
                JSONObject jsonToSend = new JSONObject();
                jsonToSend.put("name", "John Doe");
                jsonToSend.put("age", 30);

                // 서버로 데이터를 전송합니다.
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(jsonToSend.toString());

                // 서버에서 받은 응답을 읽어서 화면에 출력합니다.
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
                System.out.println("response from server: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

    private JSONObject res_stock_msg(JSONObject msg){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    this.writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
                    this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                } catch (Exception e) {
                    throw new RuntimeException("Error initializing streams", e);
                }
                // 여기서 클라이언트로부터 메시지를 받고, 응답을 보낼 수 있습니다.
                service.sendMessage("Hello from server");

                try {
                    writer.close();
                    reader.close();
                    clientSocket.close();
                } catch (Exception e) {
                    throw new RuntimeException("Error closing streams", e);
                }
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }
    private JSONObject req_stock_msg(JSONObject msg){
        try (Socket socket = new Socket(host, port)) {
            JsonSocketServiceImpl service = new JsonSocketServiceImpl(socket);
            service.start();

            // 서버로 메시지를 보내고 응답을 받습니다.
            service.sendMessage(msg);
            Message response = service.receiveMessage(Message.class);
            JSONObject msg_content = new JSONObject();
            msg_content = response.getJSONObject("msg_content");
            other_dvm_stock.put(response.get("src_id"),msg_content);
            int[] coor = new int[2];
            coor[0] =  msg_content.getInt("coor_x");
            coor[1] = msg_content.getInt("coor_y");
            other_dvm_coord.put(response.get("src_id"), coor);

            service.stop();
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }
}
