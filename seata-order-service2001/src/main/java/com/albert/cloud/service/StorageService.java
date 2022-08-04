package com.albert.cloud.service;

import com.albert.cloud.entities.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yjw
 * @date 2022/8/3 23:02
 */
@FeignClient(value = "seata-storage-service")//调用微服务的名字
public interface StorageService {

    @PostMapping(value = "/storage/decrease")
    CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);


}
