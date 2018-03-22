package com.xyl.bulletchat;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat")
@Component
public class ChatWebSocket {
    private static final Logger logger = Logger.getLogger(ChatWebSocket.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    public static CopyOnWriteArraySet<ChatWebSocket> webSocketSet = new CopyOnWriteArraySet<ChatWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        session.setMaxIdleTimeout(1000 * 60 * 30);
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info(message);
        String[] split = message.split(":\\$");
        if (split.length > 1) {
            sendInfo(split[1]);
        } else {
            sendInfo(split[0]);
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("发生错误");
        if (error instanceof org.eclipse.jetty.io.EofException) {
            if (session.isOpen()) {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            error.printStackTrace();
        }

    }


    public void sendMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) {
        for (ChatWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }


    public static synchronized void addOnlineCount() {
        ChatWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ChatWebSocket.onlineCount--;
    }
}