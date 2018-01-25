import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;


@ClientEndpoint
public class WebSocketTest {

    public static List<Session> list=new LinkedList<>();
    private String deviceId;

    private Session session;

    public WebSocketTest () {

    }

    public WebSocketTest (String deviceId) {
        this.deviceId = deviceId;
    }

    protected boolean start() {
        WebSocketContainer Container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:80/answer";
        System.out.println("Connecting to " + uri);
        try {
            session = Container.connectToServer(WebSocketTest.class, URI.create(uri));
            System.out.println("count: " + deviceId);
            list.add(session);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public static void main(String[] args) {
        for (int i = 1; i< 1000; i++) {
            WebSocketTest wSocketTest = new WebSocketTest(String.valueOf(i));
            if (!wSocketTest.start()) {
                System.out.println("测试结束！");
                break;
            }

        }
         System.out.println(list.size()+"个");
        try {
            Thread.sleep(1000*60*5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Session session : list) {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}