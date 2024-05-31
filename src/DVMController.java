import java.io.*;
import java.net.*;
import java.util.HashMap;
import org.json.JSONObject;


public class DVMController {
    private String verify_code;
    private JSONObject stock_msg_JSON;
    private JSONObject prepayment_msg_JSON;
    private int[] coord_xy;
    private HashMap<String, int[]> other_dvm_coord;
    private HashMap<String, JSONObject> other_dvm_stock;
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public boolean request_stock_msg(int item_code, int count){
        return true;
    }
    public boolean send_code(String verify_code){
        return true;
    }
    public boolean send_card_num(int card_id){
        return true;
    }
    public JSONObject res_stock_msg(JSONObject msg){
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
    public JSONObject req_stock_msg(JSONObject msg){
        try (socket = new Socket(host, port)) {
            JsonSocketServiceImpl service = new JsonSocketServiceImpl(socket);
            service.start();

            // 서버로 메시지를 보내고 응답을 받습니다.
            service.sendMessage(new Message("Hello, server!"));
            Message response = service.receiveMessage(Message.class);

            service.stop();
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }
}
