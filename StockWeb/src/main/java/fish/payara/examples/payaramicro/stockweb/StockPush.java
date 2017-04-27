/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) [2016-2017] Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 */
package fish.payara.examples.payaramicro.stockweb;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Stephen Millidge
 */
@ServerEndpoint("/graph")
public class StockPush {
    
    @Inject
    StockSessionManager sessionManager;
    
    private Session mySession;
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Opened Session" + session.getId());
        mySession = session;
        sessionManager.registerSession(session);
    }
    
    @OnClose
    public void onClose(Session session) {
        System.out.println("Closed Session" + session.getId());
        sessionManager.deregisterSession(session);
    }
    

    @OnMessage
    public String onMessage(String message, Session session) {
        return null;
    }


    @OnError
    public void onError(Throwable t) {
        System.out.println("Error ");
        t.printStackTrace();
        sessionManager.deregisterSession(mySession);
    }
    
      
}
