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
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
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

}
