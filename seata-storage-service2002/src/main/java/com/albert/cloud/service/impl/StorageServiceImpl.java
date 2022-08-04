package com.albert.cloud.service.impl;

import com.albert.cloud.dao.StorageDao;
import com.albert.cloud.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yjw
 * @date 2022/8/3 23:35
 */
@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Resource
    private StorageDao storageDao;

    @Override
    public void decrease(Long productId, Integer count) {
        logger.info("------>storage-service开始扣减库存");
        storageDao.decrease(productId,count);
        logger.info("------>storage-service扣减库存结束");
    }

}
