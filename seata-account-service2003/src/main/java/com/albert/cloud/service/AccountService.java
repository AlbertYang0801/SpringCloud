package com.albert.cloud.service;

import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author yjw
 * @date 2022/8/3 23:02
 */
public interface AccountService {

    void decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);


}
