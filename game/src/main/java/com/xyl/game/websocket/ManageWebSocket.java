package com.xyl.game.websocket;

import com.xyl.game.dto.UserDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.Page;
import com.xyl.game.utils.JsonUtils;
import com.xyl.game.utils.UserUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AnswerWebSocket
 *
 * @author Naah
 * @date 2018-01-22
 */
@ServerEndpoint(value = "/Manage")
@Component
public class ManageWebSocket {


    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static AtomicInteger onlineCount = new AtomicInteger();

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */

    public static ConcurrentLinkedDeque<ManageWebSocket> webSocketList = new ConcurrentLinkedDeque<>();


    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        session.setMaxIdleTimeout(1000*60*30);
        webSocketList.add(this);
        System.out.println("连接Manage成功！当前在线人数为" + addOnlineCount());
        sendUserInfo();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketList.remove(this);
        System.out.println("有一连接Manage关闭！当前在线人数为" + subOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        sendUserInfo();

    }

    public static void sendCount() {
        GridPage page = new GridPage();
        page.setMethod("count");
        page.setMessage(AnswerWebSocket.getOnlineCount()+ "");
        sendGridPageToAll(page);
    }

    public static void sendRank() {
        GridPage page = new GridPage();
        page.setMethod("rank");
        Page<UserDTO> userDTO = UserUtils.getRank();
        page.setPageList(userDTO);
        sendGridPageToAll(page);
    }

    public static void sendUserInfo() {
        sendCount();
        sendRank();
    }

    /**
     * 发生错误时调用
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) {
            this.session.getAsyncRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public void sendGridPage(GridPage result) {
        sendMessage(JsonUtils.objectToJSON(result));
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) {
        for (ManageWebSocket item : webSocketList) {
            item.sendMessage(message);
        }
    }

    public static void sendGridPageToAll(GridPage result) {
        sendInfo(JsonUtils.objectToJSON(result));
    }


    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    private synchronized int addOnlineCount() {
        return onlineCount.incrementAndGet();
    }

    private synchronized int subOnlineCount() {
        return onlineCount.decrementAndGet();
    }

    public String getSessionId() {
        return session.getId();
    }

}
