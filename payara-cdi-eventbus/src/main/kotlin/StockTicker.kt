import fish.payara.micro.cdi.ClusteredCDIEventBus
import fish.payara.micro.cdi.Outbound

import javax.annotation.PostConstruct
import javax.ejb.Schedule
import javax.ejb.Singleton
import javax.ejb.Startup
import javax.enterprise.event.Event
import javax.inject.Inject

@Singleton
@Startup
open class StockTicker {

    @Inject
    lateinit var bus: ClusteredCDIEventBus

    @Inject
    @Outbound
    lateinit var stockEvents: Event<String>

    @PostConstruct
    open fun postConstruct() {
        bus.initialize()
    }

    @Schedule(hour = "*", minute="*", second = "*/1", persistent = false)
    open fun generatePrice() {

        var symbol = "PAYARA"
//        var stock = Stock(symbol,"New #badassfish stock",Math.random()*100.0)
        var stock = Stock(symbol = symbol, description = "New #badassfish stock", price = Math.random()*100.0)
        System.out.println(stock)
        stockEvents.fire(stock.toString())

    }

}