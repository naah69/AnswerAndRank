package com.xyl.game.websocket;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.dto.RequestDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.service.InitService;
import com.xyl.game.service.UploadScoreService;
import com.xyl.game.utils.FinalVariable;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.JsonUtils;
import org.apache.log4j.Logger;
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
@ServerEndpoint(value = "/answer")
@Component
public class AnswerWebSocket {

    private static final Logger logger = Logger.getLogger(AnswerWebSocket.class);

    /**
     * 当前连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger();

    /**
     * websocket集合
     */
    public static ConcurrentLinkedDeque<AnswerWebSocket> webSocketList = new ConcurrentLinkedDeque<>();

    public String tel;
    /**
     * 当前session
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        session.setMaxIdleTimeout(1000 * 60 * 30);
        webSocketList.add(this);
        logger.info("Answer连接！人数:" + addOnlineCount());
        ManageWebSocket.sendCount();

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketList.remove(this);
        logger.info("Answer关闭！人数:" + subOnlineCount());
        ManageWebSocket.sendCount();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        RequestDTO req = JsonUtils.jsonToObject(message, RequestDTO.class);
        GridPage<QuestionDTO> result = new GridPage<>();
        String sessionId = session.getId();

        String tel;
        try {
            tel = req.getUser().getTel();
        } catch (Exception e) {
            result.setErrorCode("999");
            result.setMessage("找不到用户");
            sendGridPage(result);
            return;
        }
        switch (req.getMethod()) {
            case "init":
                this.tel=req.getUser().getTel();
                result = initService().initGame(sessionId, req.getUser());
                ManageWebSocket.sendUserInfo();
                break;
            case "updateScore":
                result = uploadScoreService().uploadScore(req.getId(), req.getAnswer() != null ? req.getAnswer().byteValue() : (byte) 0, req.getTimes(), sessionId, HeapVariable.usersMap.get(tel));
                break;
            default:
                result.setErrorCode(FinalVariable.NO_METHOD_ERROR_STATUS_CODE);
                result.setMessage(FinalVariable.NO_METHOD_ERROR_MESSAGE);
                break;
        }
        sendGridPage(result);
    }


    /**
     * 发生错误时调用
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        //同步发送
        //this.session.getBasicRemote().sendText(message);
        //异步发送
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 发送GirdPage
     *
     * @param result
     */
    public void sendGridPage(GridPage result) {
        sendMessage(JsonUtils.objectToJSON(result));
    }

    /**
     * 群发消息
     */
    public static int sendInfo(String message) {
        int i = 0;
        for (AnswerWebSocket item : webSocketList) {
            item.sendMessage(message);
            i++;
        }
        return i;
    }

    /**
     * 群发GirdPage
     *
     * @param result
     * @return
     */
    public static int sendGridPageToAll(GridPage result) {
        return sendInfo(JsonUtils.objectToJSON(result));
    }


    /**
     * 获取当前人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 增加人数
     *
     * @return
     */
    private synchronized int addOnlineCount() {
        return onlineCount.incrementAndGet();
    }

    /**
     * 减少人数
     *
     * @return
     */
    private synchronized int subOnlineCount() {
        return onlineCount.decrementAndGet();
    }

    /**
     * 获取sessionId
     *
     * @return
     */
    public String getSessionId() {
        return session.getId();
    }

    /**
     * uploadScoreService服务
     *
     * @return
     */
    private UploadScoreService uploadScoreService() {
        return HeapVariable.context.getBean(UploadScoreService.class);
    }

    /**
     * initService服务
     *
     * @return
     */
    private InitService initService() {
        return HeapVariable.context.getBean(InitService.class);
    }


}
