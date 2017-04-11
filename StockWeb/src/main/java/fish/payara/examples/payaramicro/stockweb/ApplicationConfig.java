/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.examples.payaramicro.stockweb;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author mike
 */
@javax.ws.rs.ApplicationPath("/rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper.class);
        resources.add(com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JsonMappingExceptionMapper.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JsonParseExceptionMapper.class);
        resources.add(fish.payara.examples.payaramicro.stockweb.StockEventsResource.class);
        resources.add(org.glassfish.json.jaxrs.JsonStructureBodyReader.class);
        resources.add(org.glassfish.json.jaxrs.JsonStructureBodyWriter.class);
    }
    
}
