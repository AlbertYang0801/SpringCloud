# SpringCloud

### 简介

尚硅谷周阳老师版本 SpringCloud 视频代码笔记。

### 学习地址

- [尚硅谷-SpringCloud2020视频地址](https://www.bilibili.com/video/BV18E411x7eT?spm_id_from=333.337.search-card.all.click)
- [Gitee笔记-轻狂书生/SpringCloud](https://gitee.com/cunjinFS/SpringCloud/tree/master)



## 注册中心-Eureka

使用 Eureka 实现**服务治理**。

### 系统架构

![image-20220506215158478](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220506215158478.png)

### 服务端和客户端

![image-20220506220354548](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220506220354548.png)



#### 服务端

1. 配置文件

   ```yml
   eureka:
     instance:
       #eureka服务端的实例名称
       hostname: localhost
     #    hostname: www.eureka7001.com
     client:
       #表示不向注册中心不注册自己
       fetch-registry: false
       #不进行服务检索，作为注册中心只需要维护服务实例
       register-with-eureka: false # 不检索自己
       service-url:
         #      defaultZone: http://www.eureka7002.com:7002/eureka/
         defahltZone: http://${eureka.instance.hostname}:${server.port}/eureka/
     server:
       #关闭自我保护机制，保证不可用服务立即被踢出
       enable-self-preservation: false
       eviction-interval-timer-in-ms: 2000
   ```

2. 启动类加注解

   ```java
   @EnableEurekaServer
   ```

3. 访问测试

   eureka正常运行，客户端正常注册之后，访问eureka服务端地址（Ip:Port），即可看到服务列表。

   ![image-20220507000000951](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220507000000951.png)

   

#### 客户端

1. 配置文件

   ```yml
   eureka:
     client:
       register-with-eureka: true
       fetchRegistry: true
       service-url:
         defaultZone: http://localhost:7001/eureka
   ```

2. 启动类加注解

   ```java
   @EnableEurekaClient
   ```

---

### 集群版

#### 服务端互相注册

比如 7001 配置 7002 的地址，将自己注册到其它注册中心。

```yaml
server:
  port: 7001

spring:
  application:
    name: cloud-eureka-server7001

eureka:
  instance:
    #eureka服务端的实例名称
    hostname: localhost
  #    hostname: www.eureka7001.com
  client:
    #表示不向注册中心不注册自己
    fetch-registry: false
    #不进行服务检索，作为注册中心只需要维护服务实例
    register-with-eureka: false # 不检索自己
    service-url:
      # Eureka 实例之间互相注册，即把自己注册到另外的注册中心实例中
      defaultZone: http://eureka7002.com:7002/eureka/
  #      defahltZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    #关闭自我保护机制，保证不可用服务立即被踢出
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 2000
```



#### 客户端注册多个Eureka

客户端注册 Eureka 时，配置每个 Eureka Server 的地址，以逗号隔开。

```yaml
server:
  port: 80

spring:
  application:
    name: cloud-comsumer-order

eureka:
  client:
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7001.com:7001/eureka/
#      defaultZone: http://localhost:7001/eureka/
```



### restTemplate-负载均衡和服务名访问

#### @LoadBalanced注解作用

1. 开启负载均衡。

2. 使 RestTemplate 发起的请求可以通过服务名访问。

   不加 @LoadBalanced 时，restTemplate 只能访问 Ip:Port 。

   加了之后可以访问注册到 eureka 中的服务名。比如 `http://CLOUD-PAYMENT-SERVICE` 

   > 使用RestTemplateCustomizer对所有标注了@LoadBalanced的RestTemplate Bean添加了一个LoadBalancerInterceptor拦截器。利用RestTempllate的拦截器，spring可以对restTemplate bean进行定制，加入loadbalance拦截器进行ip:port的替换，也就是将请求的地址中的服务逻辑名转为具体的服务地址。



![image-20220508182516351](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220508182516.png)

### 服务发现-Discovery

通过服务发现，获取注册到 Eureka 中的服务信息。

启动类增加 `@EnableDiscoveryClient` 注解。

```java
    @GetMapping("discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        System.out.println(JSONUtil.toJsonStr(services));
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return this.discoveryClient;
    }
```

![image-20220508183715557](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220508183715.png)

### 心跳保护机制

Eureka 包含两大组件：Eureka Server 和 Eureka Client。

客户端默认情况每隔 30s 向服务端发送一次心跳，而服务端收到心跳之后证实客户端存活。

#### 什么是保护机制

Eureka Server 的保护机制。

当 Eureka Server 收不到 Eureka Client 发来的心跳后，等待一定时间（默认90s），会认为服务端挂掉了。此时 Eureka Server 尝试保护其服务注册表中的信息，不会删除服务注册表中的数据，不会注销任何注册过的微服务。

**配置文件中修改该配置可以关闭自我保护机制**。

```yml
eureka:
  ......
  server:
    #关闭自我保护机制，保证不可用服务立即被踢出
    enable-self-preservation: false
```

**该机制默认开启**，源码默认为 true。

![image-20220508212422983](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220508212422983.png)



#### 客户端心跳配置

- 客户端发送心跳的时间间隔。

  ```java
  # Eureka客户端向服务端发送心跳的时间间隔，单位为s（默认30s）。
  eureka.instance.lease-renewal-interval-in-seconds: 30
  ```

- 服务端收到心跳后的等待时间间隔。

  ```java
  # Eureka服务端在收到最后一次心跳的等待时间间隔，单位为s（默认90s），超时会剔除服务。
  eureka.instance.lease-expiration-duration-in-seconds: 90
  ```

---

客户端完整配置如下：

```yaml
eureka:
  client:
    register-with-eureka: true
    fetchRegistry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
  #      defaultZone: http://eureka7001.com:7001/eureka/

  instance:
    instance-id: payment8002
    # 访问路径可以显示Ip地址
    prefer-ip-address: true
    # Eureka客户端向服务端发送心跳的时间间隔，单位为s（默认30s）。
    lease-renewal-interval-in-seconds: 30
    # Eureka服务端在收到最后一次心跳的等待时间间隔，单位为s（默认90s），超时会剔除服务。
    lease-expiration-duration-in-seconds: 90
```





## 注册中心-Zookeeper
