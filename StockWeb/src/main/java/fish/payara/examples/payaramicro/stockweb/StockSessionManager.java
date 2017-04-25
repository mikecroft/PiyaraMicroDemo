/*

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright (c) 2015 C2B2 Consulting Limited. All rights reserved.

 The contents of this file are subject to the terms of the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License.  You can
 obtain a copy of the License at
 https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 or packager/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.

 When distributing the software, include this License Header Notice in each
 file and include the License file at packager/legal/LICENSE.txt.
 */
package fish.payara.examples.payaramicro.stockweb;

import fish.payara.examples.payaramicro.stockticker.Stock;
import fish.payara.micro.cdi.ClusteredCDIEventBus;
import fish.payara.micro.cdi.Inbound;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author steve
 */
@ApplicationScoped
public class StockSessionManager {

    private HashSet<Session> sessions;

    @Inject
    private ClusteredCDIEventBus bus;

    @PostConstruct
    public void postConstruct() {
        bus.initialize();
        sessions = new HashSet<>();
    }

    void registerSession(Session session) {
        sessions.add(session);
    }

    void deregisterSession(Session session) {
        sessions.remove(session);
    }

    public void observer(@Observes @Inbound String stock) {
        try {
            for (Session session : sessions) {
                System.out.println("Received " + stock+ " writing to " + session.getId());
                session.getBasicRemote().sendText(stock);
            }
        } catch (IOException ex) {
            Logger.getLogger(StockPush.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
