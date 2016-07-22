package com.thoughtworks.ketsu.web;


import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.domain.product.ProductRepository;
import com.thoughtworks.ketsu.web.exception.InvalidParameterException;
import com.thoughtworks.ketsu.web.jersey.Routes;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("products")
public class ProductApi {

    @Context
    ProductRepository productRepository;
    @Context
    Routes routes;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postProduct(Map<String, Object> info){
        List<String> list = new ArrayList<>();
        if(info.getOrDefault("name", "").toString().trim().isEmpty())
            list.add("name");
        if(info.getOrDefault("description", "").toString().trim().isEmpty())
            list.add("description");
        if(info.getOrDefault("price", "").toString().trim().isEmpty())
            list.add("price");
        if (list.size() > 0)
            throw new InvalidParameterException(list);
        Product product = productRepository.createProduct(info).get();
        return Response.created(routes.productUri(product)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> listProducts(){
        return productRepository.listProducts();
    }

    @GET
    @Path("{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product findProduct(@PathParam("productId") long id){
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Can not find the product by id"));
    }
}
