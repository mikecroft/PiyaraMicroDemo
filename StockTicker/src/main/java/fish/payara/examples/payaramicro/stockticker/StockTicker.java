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
 * @author Mike Croft
 */
@Singleton
@Startup
public class StockTicker {
    
    private Stock stock;
    
    @Inject
    private ClusteredCDIEventBus bus;
    
    @Inject
    @Outbound
    private Event<Stock> stockEvents;
    
    @PostConstruct
    private void postConstruct() {
        bus.initialize();
    }

    @Schedule(hour = "*", minute="*", second = "*/1", persistent = false)
    private void generatePrice() {        
        stock = new Stock("",Math.random()*100.0);
        System.out.println(stock);
        stockEvents.fire(stock);       
    }

    public Stock getStock() {
        return stock;
    }
}
