package edu.nyu;


import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class RESTTester {

  public static void main(String[] args) {

    
/***
     * Old API
     * 
     * ClientConfig config = new DefaultClientConfig();
     * 
     * Client client = Client.create(config);
     * 
     * WebResource service = client.resource(getBaseURI());
     * 
     * // Fluent interfaces
     * 
     * System.out.println(service.path("rest").path("hello").accept(* MediaType.TEXT_PLAIN).get(ClientResponse.class).toString());
     * 
     * // Get plain text
     * 
     * System.out.println(service.path("rest").path("hello").accept(* MediaType.TEXT_PLAIN).get(String.class));
     * 
     * // Get XML
     * 
     * System.out.println(service.path("rest").path("hello").accept(* MediaType.TEXT_XML).get(String.class));
     * 
     * // The HTML
     * 
     * System.out.println(service.path("rest").path("hello").accept(* MediaType.TEXT_HTML).get(String.class));
     ***/


    ClientConfig config = new ClientConfig();
   
    Client client = ClientBuilder.newClient(config);

    WebTarget target = client.target(getBaseURI());

    System.out.println(target.path("rest").path("recipe/").request()

    .accept(MediaType.APPLICATION_JSON).get(String.class).toString());
/*
    System.out.println(target.path("rest").path("hello").request()

    .accept(MediaType.TEXT_PLAIN).get(String.class));

    System.out.println(target.path("rest").path("hello").request()

    .accept(MediaType.TEXT_XML).get(String.class));

    System.out.println(target.path("rest").path("hello").request()

    .accept(MediaType.TEXT_HTML).get(String.class));*/

  }

  private static URI getBaseURI() {

    return UriBuilder.fromUri("http://localhost:8080/FH").build();

  }
}