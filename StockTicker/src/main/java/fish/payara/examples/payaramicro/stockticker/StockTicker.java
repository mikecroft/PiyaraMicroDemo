/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.examples.payaramicro.stockticker;

import fish.payara.micro.cdi.ClusteredCDIEventBus;
import fish.payara.micro.cdi.Outbound;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 *
 * @author martin
 */
@Singleton
@Startup
public class StockTicker {
    
    @Inject
    ClusteredCDIEventBus bus;
    
    @Inject
    @Outbound
    Event<Stock> stockEvents;
    
    @PostConstruct
    public void postConstruct() {
        bus.initialize();
    }

    @Schedule(hour = "*", minute="*", second = "*/1", persistent = false)
    public void generatePrice() {
        
        String symbol = "PAYARA";
        Stock stock = new Stock(symbol,"",Math.random()*100.0);
        System.out.println(stock);
        stockEvents.fire(stock);
        
    }

}
