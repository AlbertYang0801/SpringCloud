package com.albert.cloud.lb;

import java.util.List;
import org.springframework.cloud.client.ServiceInstance;

/**
 * @author yangjunwei
 * @date 2022/5/25 3:43 下午
 */
public interface MyLoadBalaner {

    /**
     * 根据特定算法选择服务实例
     * @param serviceInstances 服务实例列表
     * @return 算法选中的服务实例
     */
    ServiceInstance instances(List<ServiceInstance> serviceInstances);


}
