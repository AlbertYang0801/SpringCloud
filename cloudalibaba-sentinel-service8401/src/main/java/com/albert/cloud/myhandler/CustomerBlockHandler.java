package com.albert.cloud.myhandler;

import com.albert.cloud.entities.CommonResult;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Component;

/**
 * @author yangjunwei
 * @date 2022/8/1 4:56 下午
 */
public class CustomerBlockHandler {

    public static CommonResult blockHandlerExce(BlockException blockException){
        return new CommonResult(444,"CustomerBlockHandler 自定义服务降级异常"+ blockException.getMessage());
    }

}
