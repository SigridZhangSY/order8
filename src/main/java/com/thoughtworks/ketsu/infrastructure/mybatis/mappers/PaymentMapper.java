package com.thoughtworks.ketsu.infrastructure.mybatis.mappers;

import com.thoughtworks.ketsu.domain.payment.Payment;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface PaymentMapper {
    int save(@Param("info")Map<String, Object> info);

    Payment findByOrderId(@Param("orderId")long orderId);
}
