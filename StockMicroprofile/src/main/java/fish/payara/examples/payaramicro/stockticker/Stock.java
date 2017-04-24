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

import javax.json.bind.JsonbBuilder;
import java.io.Serializable;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;


/**
 *
 * @author Stephen Millidge
 */
public class Stock implements Serializable {
    
    @JsonbTransient
    private String description;
    
    @JsonbProperty("TickerPrice")
    private double price;

    public Stock(String description, double price) {
        this.description = description;
        this.price = price;
    }
    
    public Stock() {
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return JsonbBuilder.create().toJson(this);
    }
    
    
}
