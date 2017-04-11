/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 *
 * @author mike
 */
@Path("rest")
@ApplicationScoped
public class StockResource {

    private Stock stock = new Stock("PYA", "Payara Stock", 20.0);

    @Inject
    private ClusteredCDIEventBus bus;
    
    private EventSource eventSource;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        this.eventSource = openEventSource();
        bus.initialize();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStock() {
        return stock.toString();
    }

    public void observer(@Observes @Inbound Stock stock) {
        this.stock = stock;
    }
    
    public EventSource openEventSource() {

        //see https://jersey.java.net/documentation/latest/sse.html#d0e11986
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        // explicitly listen on 9999 to avoid autobindhttp nonsense
        WebTarget target = client.target("http://localhost:9999/StockWeb-1.0-SNAPSHOT/rest/sse");


        EventSource eventSource = EventSource.target(target).build();
        EventListener listener = new EventListener() {
            @Override
            public void onEvent(InboundEvent inboundEvent) {
                System.out.println(inboundEvent.getName() + "; "
                        + inboundEvent.readData(String.class));
            }
        };   
        eventSource.register(listener, "stock-update");
        eventSource.open();
        return eventSource;
    }

    
    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        this.eventSource.close();
    }
}
