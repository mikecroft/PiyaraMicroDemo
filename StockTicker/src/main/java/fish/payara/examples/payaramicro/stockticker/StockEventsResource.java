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
package fish.payara.examples.payaramicro.stockticker;

import fish.payara.micro.cdi.Inbound;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * REST Web Service
 *
 * @author Mike Croft
 */
@Path("sse")
@ApplicationScoped
public class StockEventsResource {

    @Inject
    private StockTicker stockTicker;
    
    /**
     * Retrieves representation of an instance of
     * fish.payara.examples.payaramicro.stockweb.StockEventsResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getJsonSSE() {
        // We have to return the output, so we can't use try-with-resources.
        final EventOutput eventOutput = new EventOutput();
        
        new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Build and output a String representation of the Stock Object as a SSE.
                        eventOutput.write(new OutboundEvent.Builder().name("stock-update").data(String.class, 
                                stockTicker.getStock().toString()).build());
                    } catch (Exception ex) {
                        // Om nom nom
                    } finally {
                        try {
                            eventOutput.close();
                        } catch (IOException ioClose) {
                            throw new RuntimeException("Error when closing the event output.", ioClose);
                        }
                    }
                }
        }).start();
        
        return eventOutput;
    }
}
