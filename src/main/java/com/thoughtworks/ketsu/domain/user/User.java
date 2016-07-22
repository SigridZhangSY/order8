package com.thoughtworks.ketsu.domain.user;

import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.domain.product.ProductRepository;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.OrderMapper;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.ProductMapper;
import com.thoughtworks.ketsu.infrastructure.records.Record;
import com.thoughtworks.ketsu.web.exception.InvalidParameterException;
import com.thoughtworks.ketsu.web.jersey.Routes;

import javax.inject.Inject;
import java.util.*;

public class User implements Record {
    private long id;
    private String name;

    @Inject
    OrderMapper orderMapper;

    @Inject
    ProductRepository productRepository;

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Order> createOrder(Map<String, Object> info){
        info.put("user_id", id);
        float totalPrice = 0;
        List<Map<String, Object>> items = (List<Map<String, Object>>) info.get("order_items");
        for (int i = 0; i < items.size(); i++){
            Product product = productRepository.findById(
                    Long.valueOf(String.valueOf
                            (items.get(i).get("product_id")))).orElseThrow(() -> new InvalidParameterException("Product not exist"));
            float amount = product.getPrice() * Integer.valueOf(String.valueOf(items.get(i).get("quantity")));
            items.get(i).put("amount", amount);
            totalPrice += amount;
        }

        info.put("total_price", totalPrice);
        orderMapper.save(info);

        return Optional.ofNullable(orderMapper.findById(Long.valueOf(String.valueOf(info.get("id")))));

    }

    public List<Order> listOrders(){
        return orderMapper.getAll(id);
    }

    public Optional<Order> findOrder(long orderId){
        return Optional.ofNullable(orderMapper.findById(orderId));
    }

    @Override
    public Map<String, Object> toRefJson(Routes routes) {
        return toJson(routes);
    }

    @Override
    public Map<String, Object> toJson(Routes routes) {
        return new HashMap<String, Object>(){{
            put("id", id);
            put("uri", routes.userUri(User.this));
            put("name", name);
        }};
    }
}
