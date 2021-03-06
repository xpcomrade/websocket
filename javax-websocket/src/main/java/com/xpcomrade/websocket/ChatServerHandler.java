package com.xpcomrade.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by xpcomrade on 2016/3/8.
 * Copyright (c) 2016, xpcomrade@gmail.com All Rights Reserved.
 * Description: TODO(这里用一句话描述这个类的作用). <br/>
 */
@ServerEndpoint(value = "/chat")
public class ChatServerHandler {

    private String id;

    private Session session;

    private static final Set<ChatServerHandler> connections = new CopyOnWriteArraySet<ChatServerHandler>();

    public ChatServerHandler(){

    }


    /**
     * 打开连接
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.id = session.getId();

        connections.add(this);
        String meg = String.format("System > %s %s", this.id, "has connected...");
        ChatServerHandler.broadCast(meg);
    }

    /**
     * 接收信息
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        ChatServerHandler.broadCast(this.id + " : " + message);
    }

    /**
     * 错误信息响应
     * @param throwable
     */
    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @OnClose
    public void onClose(){
        connections.remove(this);
        String message = String.format("System > %s, %s", this.id, " has disconnection.");
        ChatServerHandler.broadCast(message);
    }

    private static void broadCast(String msg) {
        for (ChatServerHandler chat : connections) {
            try {
                synchronized (chat) {
                    chat.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                connections.remove(chat);
                try {
                    chat.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ChatServerHandler.broadCast(String.format("System > %s %s", chat.id, " has bean disconnection."));
            }
        }
    }



}
