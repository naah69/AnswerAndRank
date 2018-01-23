package com.xyl.game.websocket;

import com.xyl.game.Service.InitService;
import com.xyl.game.Service.UploadScoreService;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.dto.RequestDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AnswerWebSocket
 *
 * @author Naah
 * @date 2018-01-22
 */
@ServerEndpoint(value = "/answer")
@Component
public class AnswerWebSocket {

    @Autowired
    private UploadScoreService uploadSocreService;

    @Autowired
    private InitService initService;

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static AtomicInteger onlineCount = new AtomicInteger();

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */

    public static ConcurrentLinkedDeque<AnswerWebSocket> webSocketList = new ConcurrentLinkedDeque<>();

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
        webSocketList.add(this);
        System.out.println("有新连接加入！当前在线人数为" + addOnlineCount());
        sendMessage("Naah");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketList.remove(this);
        System.out.println("有一连接关闭！当前在线人数为" + subOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        RequestDTO req = JSONUtils.jsonToObject(message, RequestDTO.class);
        GridPage<QuestionDTO> result = new GridPage<>();
        String sessionId = session.getId();
        switch (req.getMethod()) {
            case "init":

                result = initService.initGame(sessionId, req.getUser());
                break;
            case "update":
                result = uploadSocreService.uploadScore(req.getId(), req.getAnswer(), req.getTimes(), session.getId(), HeapVariable.usersMap.get(sessionId));
                break;
            default:
                result.setErrorCode("250");
                result.setMessage("can't find method");
                break;
        }
        sendGridPage(result);
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
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //this.session.getAsyncRemote().sendText(message);
    }

    public void sendGridPage(GridPage result) {
        sendMessage(JSONUtils.objectToJSON(result));
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (AnswerWebSocket item : webSocketList) {
            item.sendMessage(message);
        }
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

}
