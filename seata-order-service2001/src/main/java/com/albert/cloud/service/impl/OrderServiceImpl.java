package com.albert.cloud.service.impl;

import com.albert.cloud.dao.OrderDao;
import com.albert.cloud.domain.Order;
import com.albert.cloud.service.AccountService;
import com.albert.cloud.service.OrderService;
import com.albert.cloud.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yjw
 * @date 2022/8/3 23:03
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    StorageService storageService;

    @Autowired
    AccountService accountService;

    @Override
    @GlobalTransactional(name="fsp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        // 1.新建订单
        log.info("-----> 开始新建订单");
        orderDao.create(order);
        log.info("-----> 开始新建订单end");

        // 2.扣减库存
        log.info("-----> 订单微服务开始调用库存，做扣减count操作");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("-----> 订单微服务开始调用库存，做扣减count操作end");

        // 3.扣减账户余额
        log.info("-----> 账户余额做money扣减");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("-----> 账户余额做money扣减end");

        // .修改订单状态
        log.info("-----> 修改订单状态开始");
        orderDao.update(order.getUserId(),0);
        log.info("-----> 修改订单状态结束");

        log.info("订单已完成");
    }


}
