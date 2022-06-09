package com.alber.cloud.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义全局拦截器
 * 请求的统一认证拦截
 * 统一日志打印
 * @author yjw
 * @date 2022/6/9 22:55
 */
@Component
@Order(value = 1)
@Slf4j
public class GatewayGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //拦截所有用户请求，判断用户名是否为空
        ServerHttpRequest request = exchange.getRequest();
        //请求参数
        String id = request.getQueryParams().getFirst("id");
        //请求头
        String name = request.getHeaders().getFirst("name");
        if(StrUtil.isBlank(name)){
            log.info("name为空，非法用户！");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }


}