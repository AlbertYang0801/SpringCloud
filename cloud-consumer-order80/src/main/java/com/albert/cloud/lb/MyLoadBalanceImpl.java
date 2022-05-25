package com.albert.cloud.lb;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

/**
 * 轮询算法
 * @author yangjunwei
 * @date 2022/5/25 3:44 下午
 */
@Component
@Slf4j
public class MyLoadBalanceImpl implements MyLoadBalaner {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        //轮询的负载均衡策略（采用取余的情况）
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }

    /**
     * 值加一
     * @return 自旋后的值
     */
    public final int getAndIncrement() {
        int current = 0;
        int next;
        //自旋，增加值
        do {
            current = atomicInteger.get();
            next = current + 1;
        } while (!atomicInteger.compareAndSet(current, next));
        return next;
    }


}
