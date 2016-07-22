package com.thoughtworks.ketsu.infrastructure.mybatis.mappers;

import com.thoughtworks.ketsu.domain.order.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    int save(@Param("info")Map<String, Object> info);

    Order findById(@Param("id")long id);

    List<Order> getAll(long userId);
}
