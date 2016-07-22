package com.thoughtworks.ketsu.web;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("products")
public class ProductApi {

    @POST
    public Response postProduct(){
        return Response.status(201).build();
    }
}
