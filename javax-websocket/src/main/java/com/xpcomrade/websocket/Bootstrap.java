package com.xpcomrade.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;

/**
 * Created by xpcomrade on 2016/3/8.
 * Copyright (c) 2016, xpcomrade@gmail.com All Rights Reserved.
 * Description: TODO(这里用一句话描述这个类的作用). <br/>
 */
public class Bootstrap {
    public static void main(String[] args) {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(9999);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/ws");
        server.setHandler(context);

        try {
            ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(context);
            wsContainer.addEndpoint(ChatHandler.class);


            server.start();
            //server.dump(System.err);
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
