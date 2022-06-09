package com.albert.cloud;

import org.junit.Test;

import java.time.ZonedDateTime;

/**
 * @author yjw
 * @date 2022/6/8 23:20
 */
public class TestTime {

    @Test
    public void test(){
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);
    }


}
