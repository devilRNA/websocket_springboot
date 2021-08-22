package com.example.pd_0;

import com.alibaba.fastjson.JSON;
import entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 前后端交互的类实现消息的接收推送(自己发送给所有人(不包括自己))
 *
 * @ServerEndpoint(value = "/test/oneToMany") 前端通过此URI 和后端交互，建立连接
 */
@Slf4j
@ServerEndpoint(value = "/test/oneToMany_obj",
                encoders = messageEncoder.class)
@Component
public class OneToManyWebSocket_obj {

    /** 记录当前在线连接数 */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /** 存放所有在线的客户端 */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineCount.incrementAndGet(); // 在线数加1
        clients.put(session.getId(), session);
        log.info("有新连接加入：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        clients.remove(session.getId());
        log.info("有一连接关闭：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        message ms= JSON.parseObject(message,message.class);
        if(ms.getType().equals("group")){
            log.info("服务端收到客户端[{}]的群发消息:{}", session.getId(), ms.getText());
            this.sendMessage(ms, session);
        }
        else if(ms.getType().equals("single")){
            log.info("服务端收到客户端[{}]的单发消息:{}", session.getId(), ms.getText());
            this.sendMessage_single(ms, session);
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 群发消息
     *
     * @param message
     *            消息内容
     */
    private void sendMessage(message message, Session fromSession) {
        // 来源为 存放所有在线客户端的Map --- clients
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            Session toSession = sessionEntry.getValue();
            // 排除掉自己
            // 自己的id---> fromSession.getSid()
            // 遍历对象的id---> toSession.getSid()
            // 核心机制为-遍历clients的键值对-获得所有值，也就是session对象-通过比较session中的ID元素选择是否发送-调用session的发送方法
            if (!fromSession.getId().equals(toSession.getId())) {
                log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message.getText());
//                toSession.getAsyncRemote().sendText(message);
                toSession.getAsyncRemote().sendObject(new message(Integer.parseInt(fromSession.getId(),16),message.getText()));
            }
        }
    }


    /**
     * 单发消息
     *
     * @param message
     * 消息的字符串对象，其中包含sid,rid等元素
     * */
    private void sendMessage_single(message message, Session fromSession) {
        // 来源为 存放所有在线客户端的Map --- clients
        boolean isSent = false;
        String receiveId=String.valueOf(message.getRid());
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            Session toSession = sessionEntry.getValue();
            if (receiveId.equals(toSession.getId())) {
                log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message.getText());
//                toSession.getAsyncRemote().sendText(message);
                toSession.getAsyncRemote().sendObject(new message(Integer.parseInt(fromSession.getId(),16),message.getText()));
                isSent=true;
                break;
            }
        }
        if (!isSent){
            log.info("消息未发送成功！内容为："+message.getText());
        }
    }

}