package com.thoughtworks.ketsu.domain.payment;

import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.OrderMapper;
import com.thoughtworks.ketsu.infrastructure.records.Record;
import com.thoughtworks.ketsu.web.jersey.Routes;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payment implements Record {
    private long orderId;
    private String payType;
    private float amount;
    private Date time;

    @Inject
    OrderMapper orderMapper;

    public long getOrderId() {
        return orderId;
    }

    public String getPayType() {
        return payType;
    }

    public float getAmount() {
        return amount;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public Map<String, Object> toRefJson(Routes routes) {
        return toJson(routes);
    }

    @Override
    public Map<String, Object> toJson(Routes routes) {
        Order order = orderMapper.findById(orderId);
        return new HashMap<String, Object>(){{
            put("order_uri", routes.orderUri(order));
            put("uri", routes.paymentUri(Payment.this, order.getUserId()));
            put("pay_type", payType);
            put("amount", amount);
            put("create_at", time);
        }};
    }
}
