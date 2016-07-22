package com.thoughtworks.ketsu.domain.order;

import com.thoughtworks.ketsu.domain.payment.Payment;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.PaymentMapper;
import com.thoughtworks.ketsu.infrastructure.records.Record;
import com.thoughtworks.ketsu.web.jersey.Routes;

import javax.inject.Inject;
import java.util.*;

public class Order implements Record{
    private long id;
    private long userId;
    private String name;
    private String address;
    private String phone;
    private float totalPrice;
    private Date time;
    private List<OrderItem> items;

    @Inject
    PaymentMapper paymentMapper;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public Date getTime() {
        return time;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Optional<Payment> createPayment(Map<String, Object> info){
        info.put("order_id", id);
        paymentMapper.save(info);
        return Optional.ofNullable(paymentMapper.findByOrderId(id));
    }

    public Optional<Payment> findPayment(){
        return Optional.ofNullable(paymentMapper.findByOrderId(id));
    }


    @Override
    public Map<String, Object> toJson(Routes routes) {
        return new HashMap<String, Object>(){{
            put("uri", routes.orderUri(Order.this));
            put("name", name);
            put("address", address);
            put("phone", phone);
            put("total_price", totalPrice);
            put("create_at", time);
            List<Map<String, Object>> orderItems = new ArrayList<Map<String, Object>>();
            for(int i = 0; i < items.size(); i++){
                Map<String, Object> map = new HashMap<>();
                map.put("product_id", items.get(i).getProductId());
                map.put("quantity", items.get(i).getQuantity());
                map.put("amount", items.get(i).getAmount());
                orderItems.add(map);
            }
            put("order_items", orderItems);
        }};
    }

    @Override
    public Map<String, Object> toRefJson(Routes routes) {
        return new HashMap<String, Object>() {{
            put("uri", routes.orderUri(Order.this));
            put("name", name);
            put("address", address);
            put("phone", phone);
            put("total_price", totalPrice);
            put("create_at", time);
        }};
    }
}
