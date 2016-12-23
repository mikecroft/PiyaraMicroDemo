/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.examples.payaramicro.stockweb;

import fish.payara.examples.payaramicro.stockticker.Stock;
import fish.payara.micro.cdi.ClusteredCDIEventBus;
import fish.payara.micro.cdi.Inbound;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * REST Web Service
 *
 * @author mike
 */
@Path("sse")
@RequestScoped
public class StockEventsResource {

    @Context
    private UriInfo context;

    @Inject
    private ClusteredCDIEventBus bus;

    @Inbound
    Stock stock;

    private EventOutput eo;

    /**
     * Creates a new instance of StockEventsResource
     */
    public StockEventsResource() {

    }

    @PostConstruct
    public void postConstruct() {
        eo = new EventOutput();
        bus.initialize();
    }

    /**
     * Retrieves representation of an instance of
     * fish.payara.examples.payaramicro.stockweb.StockEventsResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getJsonSSE() {

        System.out.println(stock.toString());

        return this.eo;
    }

    public void observer(@Observes @Inbound Stock stock) {
        System.out.println("Received " + stock.toString());
        this.stock = stock;
        try {
            eo.write(new OutboundEvent.Builder().name("stock-update")
                    .data(String.class, stock.toString()).build());
        } catch (IOException ex) {
            Logger.getLogger(StockEventsResource.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * PUT method for updating or creating an instance of StockEventsResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
