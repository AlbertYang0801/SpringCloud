package com.albert.cloud.service.impl;

import com.albert.cloud.dao.AccountDao;
import com.albert.cloud.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author yjw
 * @date 2022/8/3 23:40
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    AccountDao accountDao;

    @Override
    public void decrease(Long userId, BigDecimal money) {
        try {
            //sleep 模拟接口超时 feign默认超时时间10s
//           TimeUnit.SECONDS.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("------> account-service 中开始扣减账户余额");
        accountDao.decrease(userId,money);
        logger.info("------> account-service 中扣减账户余额结束");
    }


}
