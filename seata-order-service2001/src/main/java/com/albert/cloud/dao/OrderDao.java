package com.albert.cloud.dao;

import com.albert.cloud.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yjw
 * @date 2022/8/3 22:59
 */
@Mapper
public interface OrderDao {
    // 新建订单
    void create(Order order);

    // 修改订单状态 从0改为1
    void update(@Param("userId") Long userId, @Param("status") Integer status);
}
