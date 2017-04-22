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
 * @author Stephen Millidge
 */
@ApplicationScoped
public class StockSessionManager {

    private HashSet<Session> sessions;

    @Inject
    private ClusteredCDIEventBus bus;

    @PostConstruct
    private void postConstruct() {
        bus.initialize();
        sessions = new HashSet<>();
    }

    void registerSession(Session session) {
        sessions.add(session);
    }

    void deregisterSession(Session session) {
        sessions.remove(session);
    }

    private void observer(@Observes @Inbound Stock stock) {
        try {
            for (Session session : sessions) {
                System.out.println("Received " + stock.toString() + " writing to " + session.getId());
                session.getBasicRemote().sendText(stock.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(StockPush.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
