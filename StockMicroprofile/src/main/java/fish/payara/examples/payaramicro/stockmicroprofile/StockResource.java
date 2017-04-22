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
package fish.payara.examples.payaramicro.stockmicroprofile;

import fish.payara.examples.payaramicro.stockticker.Stock;
import fish.payara.micro.cdi.ClusteredCDIEventBus;
import fish.payara.micro.cdi.Inbound;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 *
 * @author Mike Croft
 */
@Path("rest")
@ApplicationScoped
public class StockResource {

    private Stock cdiStock = new Stock("PYA", "Payara Stock", 20.0);
    private Stock sseStock = new Stock("PYA", "Payara Stock", 20.0);

    @Inject
    private ClusteredCDIEventBus bus;

    private EventSource eventSource;

    // CDI is lazily initialised, so we need to give it a poke.
    private void init(@Observes @Initialized(ApplicationScoped.class) Object initialised) {
        openEventSource();
        bus.initialize();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStock() {
        return cdiStock.toString();
    }

    private void observer(@Observes @Inbound Stock stock) {
        cdiStock = stock;

        // Prove SSE + JSONB deserialisation is working by printing out the stock object recieved from both sources:
        System.out.println("CDI-Stock: " + cdiStock.toString());
        System.out.println("SSE-Stock: " + sseStock.toString());
    }

    private void openEventSource() {
        // See https://jersey.java.net/documentation/latest/sse.html#d0e11986
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        
        // Explicitly listen on 9999 to avoid autoBindHttp nonsense
        WebTarget target = client.target("http://localhost:9999/StockTicker-1.0-SNAPSHOT/rest/sse");

        eventSource = EventSource.target(target).build();
        EventListener listener = new EventListener() {
                @Override
                public void onEvent(InboundEvent inboundEvent) {
                    sseStock = JsonbBuilder.create().fromJson(inboundEvent.readData(String.class), Stock.class);
                }
        };
        
        eventSource.register(listener, "stock-update");
        eventSource.open();
    }

    /**
     * Close the Event Source
     * @param destroyed 
     */
    private void destroy(@Observes @Destroyed(ApplicationScoped.class) Object destroyed) {
        eventSource.close();
    }
}
