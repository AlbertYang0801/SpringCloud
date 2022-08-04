package com.albert.cloud.controller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yjw
 * @date 2022/8/3 23:27
 */
@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    /**
     * 扣减库存
     */
    @RequestMapping("/storage/decrease")
    public CommonResult decrease(Long productId, Integer count) {
        storageService.decrease(productId,count);
        return new CommonResult(200,"扣减库存成功");
    }


}
