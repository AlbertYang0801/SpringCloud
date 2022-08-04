package com.albert.cloud.controller;

import com.albert.cloud.entities.CommonResult;
import com.albert.cloud.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author yjw
 * @date 2022/8/3 23:41
 */
@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * 扣减账户余额
     */
    @RequestMapping("/account/decrease")
    public CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money) {
        accountService.decrease(userId,money);
        return new CommonResult(200,"账户扣减成功");
    }


}

