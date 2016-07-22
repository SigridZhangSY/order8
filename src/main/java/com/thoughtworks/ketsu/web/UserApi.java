package com.thoughtworks.ketsu.web;

import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.domain.payment.Payment;
import com.thoughtworks.ketsu.domain.user.User;
import com.thoughtworks.ketsu.domain.user.UserRepository;
import com.thoughtworks.ketsu.web.exception.InvalidParameterException;
import com.thoughtworks.ketsu.web.jersey.Routes;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserApi {

    private User user;

    @Context
    UserRepository userRepository;



    public UserApi(User user) {
        this.user = user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User findUser(){return user;}

    @POST
    @Path("orders")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOder(Map<String, Object> info,
                               @Context Routes routes){
        List<String> list = new ArrayList<>();
        if (info.getOrDefault("name", "").toString().trim().isEmpty())
            list.add("name");
        if (info.getOrDefault("phone", "").toString().trim().isEmpty())
            list.add("phone");
        if (info.getOrDefault("address", "").toString().trim().isEmpty())
            list.add("address");
        if (info.getOrDefault("order_items", "").toString().trim().isEmpty())
            list.add("order_items");
        else {
            List<Map<String, Object>> items = (List<Map<String, Object>>) info.get("order_items");
            for(Map<String, Object> item : items){
                if (item.getOrDefault("product_id", "").toString().trim().isEmpty())
                    list.add("product_id");
                if (item.getOrDefault("quantity", "").toString().trim().isEmpty())
                    list.add("quantity");
            }
        }
        if (list.size() > 0)
            throw new InvalidParameterException(list);
        Order order = user.createOrder(info).get();
        return Response.created(routes.orderUri(order)).build();
    }

    @GET
    @Path("orders")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> listOrders(){
        return user.listOrders();
    }

    @GET
    @Path("orders/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order findOrder(@PathParam("orderId") long orderId){
        return user.findOrder(orderId).orElseThrow(() -> new NotFoundException("Can not find the order by id"));
    }

    @POST
    @Path("orders/{orderId}/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPayment(Map<String, Object> info,
                                  @Context Routes routes,
                                  @PathParam("orderId") long orderId){
        List<String> list = new ArrayList<>();
        if (info.getOrDefault("pay_type", "").toString().trim().isEmpty())
            list.add("pay_type");
        if (info.getOrDefault("amount", "").toString().trim().isEmpty())
            list.add("amount");

        if (list.size() > 0)
            throw new InvalidParameterException(list);

        Order order = user.findOrder(orderId).orElseThrow(() -> new InvalidParameterException("Order not exists"));
        Payment payment = order.createPayment(info).get();
        return Response.created(routes.paymentUri(payment, user.getId())).build();
    }

    @GET
    @Path("orders/{orderId}/payment")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment findPayment(@PathParam("orderId") long orderId){
        Order order = user.findOrder(orderId).orElseThrow(() -> new InvalidParameterException("Order not exists"));
        return order.findPayment().orElseThrow(() -> new NotFoundException("Can not find the payment of the order, the order is not paied yet."));
    }
}
