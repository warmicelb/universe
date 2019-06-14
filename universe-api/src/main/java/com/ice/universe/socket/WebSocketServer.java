package com.ice.universe.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName WebSocketConfig
 * @Description TODO
 * @Author liubin
 * @Date 2019/6/11 8:07 PM
 **/
@ServerEndpoint("/websocket")
public class WebSocketServer {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        if(sessionSet.add(session)){
            System.out.println("新用户已连接,用户");
        }
    }

    @OnClose
    public void onClose(Session session){
        sessionSet.remove(session);
        System.out.println("用户退出登录");
    }

    @OnMessage
    public void onMessage(String message){
        for (Session session:sessionSet
             ) {
            session.getAsyncRemote().sendText(message);
        }
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
