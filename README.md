# SpringCloud

## 简介

尚硅谷周阳老师版本 SpringCloud 视频代码笔记。

### 学习地址

- [尚硅谷-SpringCloud2020视频地址](https://www.bilibili.com/video/BV18E411x7eT?spm_id_from=333.337.search-card.all.click)
- [Gitee笔记-轻狂书生/SpringCloud](https://gitee.com/cunjinFS/SpringCloud/tree/master)



## 注册中心

### 1. Eureka

使用 Eureka 实现**服务治理**。

#### 系统架构

![image-20220506215158478](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220506215158478.png)

#### 服务端和客户端

![image-20220506220354548](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220506220354548.png)

**服务端**

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

   ![image-20220507000000951](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220507000000951.png)

   

**客户端**

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

#### 集群版

**服务端互相注册**

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



**客户端注册多个Eureka**

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



#### restTemplate-负载均衡和服务名访问

**@LoadBalanced注解作用**

1. 开启负载均衡。

2. 使 RestTemplate 发起的请求可以通过服务名访问。

   不加 @LoadBalanced 时，restTemplate 只能访问 Ip:Port 。

   加了之后可以访问注册到 eureka 中的服务名。比如 `http://CLOUD-PAYMENT-SERVICE` 

   > 使用 RestTemplateCustomizer 对所有标注了@LoadBalanced的RestTemplate Bean 添加了一个 LoadBalancerInterceptor 拦截器。利用 RestTempllate 的拦截器，spring可以对restTemplate bean进行定制，加入loadbalance拦截器进行 ip:port 的替换，也就是将请求的地址中的服务逻辑名转为具体的服务地址。

![image-20220508182516351](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220508182516.png)

#### 服务发现-Discovery

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

![image-20220508183715557](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220508183715.png)

#### 心跳保护机制

Eureka 包含两大组件：Eureka Server 和 Eureka Client。

客户端默认情况每隔 30s 向服务端发送一次心跳，而服务端收到心跳之后证实客户端存活。

**什么是保护机制**

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

![image-20220508212422983](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220508212422983.png)



**客户端心跳配置**

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

### 2. Zookeeper

![image-20220509164722758](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220509164722758.png)

**注册进 Zookeeper 的服务节点是临时节点还是持久节点？**

临时节点。

*当 Zookeeper 服务端收不到客户端发来的心跳时，会将客户端信息删除。假如该客户端重新加入，则重新生成服务节点。*

![image-20220509164837290](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220509164837290.png)

### 3. consul

#### 安装

- 下载地址

  https://www.consul.io/downloads

- 安装地址

  ```java
  D:\SpringCloud\consul
  ```

- 启动命令

  ```properties
  #查看版本
  consul --version
  #开发模式启动
  consul agent -dev
  ```

- 管理页面

  http://localhost:8500

#### 服务注册

参考模块: **cloud-provider-payment8006**。

```yml
server:
  port: 8006
spring:
  application:
    name: consul-payment-service
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}

```

注册成功访问管理页面可以看到服务信息。

![image-20220509175912577](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220509175912577.png)

#### 服务发现

参考模块: **cloud-consumerconsul-order80**

```yml
spring:
  application:
    name: cloud-comsumer-order
  cloud:
    consul:
      port: 8500
      host: localhost
      discovery:
        service-name: ${spring.application.name}
```

### 三个注册中心的异同点

#### 相同点

- 都可以作为注册中心实现服务的注册与发现。

#### CAP理论

- C: Consistency(强一致性)
- A: Availability(可用性)
- P: Parttition tolerance(分区容错性)

![image-20220509230023366](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220509230023366.png)

#### 各个组件的类型

- Eureka - AP

  Eureka默认有个分区保护机制，当 Eureka Client 经过一定时间没有心跳发送到 Eureka Server，Eureka Server 不会将 对应的Eureka Client 的注册信息删除。所以 Eureka 在分布式情况下类型对应 AP 类型，牺牲了数据的一致性，来保证可用性。

  *Eureka 的保护机制可以关闭，当关闭保护机制时，也是 CP 类型。*

- Zookeeper

  当接收不到某个 Client 的心跳后，会立即删除该 Client 的服务信息。该 Client 重新注册后，会重新生成服务。

  所以 Zookeeper 对应 CP 类型，牺牲可用性，保证数据一致性。

- Consul

  类型 Zookeeper，当 Client 不可用时，立即标识 Client 不可用。

  同理 Consul 对应 CP类型。



## 负载均衡-Ribbon

Ribbon 的主要功能就是 **客户端的服务调用和负载均衡**。

### Ribbon工作原理

1. 选择负载较少的 Eureka Server。

2. 根据指定的负载均衡策略，从 Eureka Server 获取指定服务的服务地址。

   Ribbon 提供了多种负载均衡策略，默认是轮询策略，可以设置随机、权重、响应时间加权等策略。

3. 根据获取到的服务地址发起请求。

![img](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220510115710.png)



### Ribbon和Nginx的区别

- Ribbon 是客户端的负载均衡。

  在调用微服务接口的时候，Ribbon会从注册中心查询可用服务列表，通过固定的负载均衡策略，挑选可用服务地址，从本地发起远程调用。

- Nginx 是服务端的负载均衡。

  Nginx 是客户端发送请求给 Nginx，然后再由 Nginx 实现转发请求，这时的负载均衡是由 Nginx实现的，与客户端无关，称为服务端的负载均衡。

  



### Ribbon负载均衡的策略

![image-20220525140307612](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220525140307.png)



### 实现轮询算法

#### 原理

记录请求服务的次数，每请求一次，递增一次。访问时根据请求次数对服务总数取余。

#### 代码实现

创建一个接口

```java
public interface MyLoadBalaner {

    /**
     * 根据特定算法选择服务实例
     * @param serviceInstances 服务实例列表
     * @return 算法选中的服务实例
     */
    ServiceInstance instances(List<ServiceInstance> serviceInstances);

}
```

接口实现类

```java
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
```

服务消费者调用该方法，选择服务。

```java
@GetMapping("/consumer/payment/lb}")
public String getPaymentUrl() {
		List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
    ServiceInstance instance = myLoadBalaner.instances(instances);
    URI uri = instance.getUri();
    return restTemplate.getForObject(uri+"/payment/lb",String.class);
}
```



## 服务调用-OpenFeign

Feign是一个声明式WebService客户端。使用Feign能让编写WebService客户端更加简单。

定义一个服务接口然后在上面添加注解。Feign也能支持可拔插式的编码器和解码器。Spring Cloud对Feign进行了封装，使其能够支持SpringMVC标准注解和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用完成负载均衡。

Feign旨在使Java Http客户端变得更加容易。

前面在使用Ribbon+RestTemplate时，利用RestTemplate对Http请求做封装处理，形成一套模板化的调用方法。但是在实际开发中，由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用。所以，Feign在此基础上做了进一步的封装，由他来帮助我们定义和实现接口。在Feign的实现下，我们只需要使用注解配置接口，即可完成对服务提供方的接口绑定，简化了使用Spring Cloud Ribbon时，自己封装服务调用的工作量。

默认Feign集成了Ribbon。

### OpenFeign的超时控制

当OpenFeign配置的等待时间少于服务端的处理时间时，会发生超市错误。所以尽可能在OpenFeign的客户端配置等待时间少于服务端的处理时间。

```yml
ribbon:
  # 指的是建立连接所用的时间,适用于网络状态正常的情况下,两端连接所用的时间
  ReadTimeout: 5000
  # 指的是建立连接后从服务器读取到可用资源所用的时间
  ConnectTimeout: 5000
```

### OpenFeign的日志增强

OpenFeign在调用服务接口时，可以打印日志，对应日志类型如下。

- NONE：默认的，不显示任何日志
- BASIC：仅仅记录请求方法、URL、响应状态码以及执行时间
- HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息
- FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文以及元数据。

新增配置类。

```java
@Configuration
public class FeignConfig {

    /**
     * Feign接口的日志类型
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
```

配置文件中增加指定服务类的日志级别。

```yml
logging:
  level:
    #feign日志以什么级别监控某个服务类
    com.albert.cloud.service.PaymentFeignService: debug
```



## 服务降级-Hystrix

Hystrix 是用来处理分布式系统容错的组件，Hystrix 能够保证在链路调用，下游服务出现问题时（异常、超时等），采用断路器的机制，保证整体服务不会失败，避免链路调用情况下的雪崩情况发生。



### 服务降级

在链路调用情况下，若发现调用下游服务触发服务降级的条件（比如超时、异常等），执行 fallback 降级机制（比如返回友好提示等）。

服务降级是先执行正常的方法，判断满足服务降级的条件后，进行服务降级的策略。

- fallbackMethod

  服务降级触发时执行的方法。

- execution.isolation.thread.timeoutInMilliseconds

  方法执行最大时间，超时则触发 fallback 降级机制。

  ```java
  	//在方法调用超过5s时，调用指定方法返回友好提示。
      @HystrixCommand
      @HystrixCommand(fallbackMethod = "getPaymentServiceError",commandProperties = {
              @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
      })
  ```

- **方法内的异常也会触发 fallback 降级机制**。



### 服务熔断

服务熔断是一种链路调用增强的保护机制。

- 当链路服务调用时，下游服务出现不可用时，会进行服务降级，执行 fallback 降级机制。
- 当连续多次发生不可用时，会触发服务熔断。开启断路器，直接熔断对该下游服务的调用，调用该下游服务会直接执行 fallback降级机制。
- **当检测到该下游服务可用时，则恢复调用链路**。

**和服务降级的不同点**

- 服务降级是先进行服务调用后，发现满足服务降级的条件后，触发 fallback 降级机制。

- 而服务熔断是在多次请求满足了开启断路器的条件后，执行的熔断机制。触发熔断机制之后，所有请求在规定时间内直接触发 fallback降级机制，不再调用下游服务。

  **减少客户端因为调用下游服务产生的长时间阻塞等待，避免分布式系统中的调用延时，雪崩问题。**

  

#### 服务熔断重要参数

- **circuitBreaker.enabled** 

  是否开启断路器

- **circuitBreaker.errorThresholdPercentage**

  请求错误百分比，默认值50%。例如一段时间内 （10s）请求100个，错误请求超过50个，才会打开断路器。

- **circuitBreaker.requestVolumeThreshold**

  请求阈值，默认20。至少有20个请求，才会进行错误百分比计算。否则不会打开断路器。

- **circuitBreaker.sleepWindowInMilliseconds**

  时间窗口期，默认5s。在 5s 内请求错误率达到配置值，开启断路器。*所有经过断路器的请求直接走 fallback降级机制，不调用下游服务。*

  而断路器开启时间到达5s后，变更状态为 Half-Open (半开) 状态，允许一个请求转发到下游服务检测下游服务是否正常，若下游服务正常，断路器 closed（关闭）。否则继续 open（开启），继续等待下一个周期。

  ![image-20220606230013329](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220606230013329.png)
  
  

```java
	@HystrixCommand(
            fallbackMethod = "paymentCircuitBreakerFallback", commandProperties =
            {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间窗口期
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率达到多少后跳闸
            }
    )
    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("-----------------------id不能为负数-------------------");
        }
        return Thread.currentThread().getName() + "\t" + "成功调用，流水号是：" +IdUtil.simpleUUID();
    }
```

服务熔断测试效果

>正常调用：http://localhost/order/info/circuit/1
>
>- hystrix-PaymentServiceImpl-10 成功调用，流水号是：6c23fe7b0ee645d89b6fa5422246f0b3\
>
>错误调用：http://localhost/order/info/circuit/-111
>
>- hystrix-PaymentServiceImpl-10-----------服务端服务熔断触发----ERROR-----111
>
>多次错误调用，触发断路器之后：
>
>错误请求：hystrix-PaymentServiceImpl-10-----------服务端服务熔断触发----ERROR-----111
>
>正常请求：http-nio-8001-exec-9-----------服务端服务熔断触发----ERROR----3

可以看到触发断路器之后，不管正常还是异常请求都会触发 fallback 机制。

### 实际场景

当业务流量比较大，原有逻辑已经满足不了服务调用时，可以采取服务降级或服务熔断的机制来进行优化。

1. 在实际业务中，要区分出主要和次要的功能，次要功能可以选择加上服务熔断的机制。
2. 针对主要功能中调用次要功能的部分，可以将调用机制改为异步，即将调用的强一致性改为最终一致性。



## 服务网关



### SpringCloud Gateway

Gateway 是 Spring 社区提供的网关组件，提供了反向代理、鉴权、熔断、日志监控、路由转发等功能。

- 路由 - route
- 断言 - Predicate 
- 过滤器 - Filter

![自己手绘的](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220613180031.awebp)



### 工作流程

当用户发出请求达到 `GateWay` 之后，会通过一些匹配条件，定位到真正的服务节点，并且在这个转发过程前后，进行一些细粒度的控制，其中 **Predicate（断言）** 是我们的匹配条件，**Filter** 是一个拦截器，有了这两点，再加上URL，就可以实现一个具体的路由，核心思想：**路由转发+执行过滤器链**。

![img](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/20220613180015.awebp)

### 实现方式

- yml配置

  ```yml
  spring:
    application:
      name: cloud-gateway-service
    cloud:
      gateway:
        routes:
          - id: payment_routh
            uri: http://localhost:8001
            predicates:
              - Path=/payment/get/**
          - id: payment_routh2
            uri: http://localhost:8001
            predicates:
              - Path=/payment/lb/**
  ```

  

- 配置类

  ```java
  @Configuration
  public class GateWayConfig {
  
      @Bean
      public RouteLocator routeLocator(RouteLocatorBuilder builder) {
          return builder.routes().route("path_route", r -> r.path("/guonei").uri("https://news.baidu.com")).build();
      }
  
  }
  ```

### 服务名动态配置

默认情况下 Gateway 会根据注册中心注册的服务列表，以注册中心上注册的**微服务名称创建动态路由**进行转发。

> lb://serverName是spring cloud  gatway在微服务中自动为我们创建的负载均衡uri

```yml
spring:
  application:
    name: cloud-gateway-service
  cloud:
    gateway:
      routes:
        - id: payment_routh
          #服务动态路由，lb指开启负载均衡
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/get/**
        - id: payment_routh2
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/lb/**
      discovery:
        locator:
          enabled: true  #开启从注册中心动态生成路由的功能，用微服务名进行路由
```



### Predicate

路由匹配时，可以进行 Predicate 操作进行判断。

1. After Route Predicate 

   在指定时间之后才可以进行路由访问。

   ```yml
             predicates:
               - Path=/payment/lb/**
               - After=2022-06-08T23:26:09.468+08:00[Asia/Shanghai]
   ```

2. Before Route Predicate 

3. Between Route Predicate 

4. Cookie Route Predicate 

   ```yml
             predicates:
               - Path=/payment/get/**
               - After=2022-06-07T23:26:09.468+08:00[Asia/Shanghai]
               - Cookie=username,yyds
   ```




### 路由过滤器

路由过滤器可以在路由请求之前和请求之后进行功能性的增强。

- 对路由请求进行全局日志打印。
- 统一认证拦截。

Gateway 内置了很多过滤器，还可以自定义过滤器。

**自定义过滤器的实现:**

```java
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
```



## 分布式链路追踪-Sleuth

Sleuth 负载拦截网络请求生成调用数据，发给 zipkin 进行存储和展示。

### zipkin

- 运行zipkin

  ```java
  java -jar zipkin-server-2.23.16-exec.jar
  ```

- 访问 [http://localhost:9411/zipkin/](http://localhost:9411/zipkin/)



### Sleuth配置

- 依赖

  ```xml
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-zipkin</artifactId>
          </dependency>
  ```

- 配置

  ```yml
  spring:
    application:
      name: cloud-payment-service
  
    zipkin:
      base-url: http://localhost:9411
    sleuth:
      sampler:
        #采样率值介于0到1之间，1代表全部采集
        probability: 1
  ```

---



**调用链路展示效果**

![image-20220613233044122](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220613233044122.png)



### Sleuth链路追踪原理





## Nacos

nacos 可以作为注册中心和配置中心。

### 注册中心-Nacos

Nacos 作为注册中心时，支持 CP 和 AP 模式的切换。

![image-20220619185557909](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220619185557909.png)



![image-20220619185547657](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220619185547657.png)



#### CP 模式

保证**数据强一致性**和**分区容忍性**。

CP 模式下支持注册实例的持久化。

像 K8s 中的服务，适用于 CP 模式。需要对服务信息做持久化，保证读取到的服务信息的一致性。

#### AP模式

保证**高可用性**和**分区容忍性**。

AP 模式下注册的服务都是临时实例，不对实例信息做持久化。



### 配置中心-Nacos

nacos作为配置中心时，在项目初始化时，要保证先从配置中心拉取配置，然后保证项目正常启动。

Spring 加载配置文件存在优先级顺序，bootstrap.yml 和 application.yml 相比，**bootstrap.yml 的优先级更高**。



#### 配置规则

[Nacos Spring Cloud 快速开始](https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html)

Nacos 的配置功能具有**动态刷新**的功能。

![image-20220619220624415](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220619220624415.png)

---

配置流程，注意 **Data ID** 的配置规则：`${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}`

![image-20220619221032541](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220619221032541.png)

![image-20220619221106581](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220619221106581.png)



#### 分类配置

- DataId
- Group
- Namespace

### 持久化机制

Nacos 默认使用内置的 derby 数据库，可以配置自定义 MySQL 数据源保存配置信息。

在 Nacos 的 `config` 目录下的 `application.properties` 修改如下内容：

```properties
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=123456
```







### 集群配置

通过负载均衡，实现 Nacos 服务端的反向代理。

![image-20220619225524484](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220619225524484.png)

![image-20220619230332763](https://fastly.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220619230332763.png)



1. 使用 docker 搭建 Nacos 集群。注意映射端口和配置文件。
2. 搭建 nginx，配置转发策略。



## Sentinal

[Sentinal 官方文档](https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D)



### 启动命令

- Sentinal 其它端口启动

  ```java
  java -Dserver.port=8888 -Dcsp.sentinel.dashboard.server=localhost:8888 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.1.jar
  ```

- IDEA

  ![image-20220801215522180](C:\Users\yjw\AppData\Roaming\Typora\typora-user-images\image-20220801215522180.png)

  ```java
  -Dserver.port=8888
  -Dcsp.sentinel.dashboard.server=localhost:8888
  -Dproject.name=sentinel-dashboard
  -jar
  D:\IdeaWorkSpace\SpringCloud\sentinel\sentinel-dashboard-1.7.1.jar
  ```

  

### 限流规则

![](https://s2.loli.net/2022/07/18/br4ehKujQoYmWLD.png)

#### 限流类型

- QPS

  QPS配置时，1秒内的请求达到上限后，会进行限流。

- 线程数

  当调用接口的线程达到阈值的时候，会进行限流。

#### 流控模式

- 直接

  达到限流条件后，直接限流。

- 关联

  当关联的资源达到阈值时，限流自己。B 达到阈值，限流 A。

  ![image-20220718203246914](https://s2.loli.net/2022/07/18/DJmsCE7zKpbVkxA.png)

  

  **使用postman并发测试接口**

  ![image-20220718205455950](https://s2.loli.net/2022/07/18/JCSgmMGE6Fs4bfd.png)

- 链路

  当多个请求调用了相同的微服务时，可以针对某个请求进行限流。

  比如有两个接口 `/order/save` 和 `/order/query` 同时调用了 `goods` 接口，针对其中一个接口进行限流。

  ![image-20220718203027471](https://s2.loli.net/2022/07/18/DJmsCE7zKpbVkxA.png)

  


#### 流控效果

- 快速失败

  达到阈值，立即拒绝并抛出异常，是**默认的处理方式**。

  ```java
  2022-07-18 21:17:38.764 ERROR 24712 --- [nio-8401-exec-6] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.reflect.UndeclaredThrowableException] with root cause
  
  com.alibaba.csp.sentinel.slots.block.flow.FlowException: null
  ```

- Warm Up

  **预热模式**，对超出阈值的请求同样是拒绝并抛出异常，但这种模式阈值能够动态改变，从一个较小值逐渐增大到最大阈值。

  ![image-20220718212349214](https://s2.loli.net/2022/07/18/mocHSujvNGkWBsn.png)

  预热方式是为了保护系统不被大流量击垮，逐渐调大阈值来达到保护系统的目的。

- 排队等待

  ![img](https://s2.loli.net/2022/07/18/yiShNGu8BY47feA.png)

  排队等待的效果是 QPS的效果，1s1次请求。

  ![image-20220718220615395](https://s2.loli.net/2022/07/18/aMUIJrbq18NLzFD.png)





### 熔断降级

>除了流量控制以外，对调用链路中不稳定的资源进行熔断降级也是保障高可用的重要措施之一。一个服务常常会调用别的模块，可能是另外的一个远程服务、数据库，或者第三方 API 等。例如，支付的时候，可能需要远程调用银联提供的 API；查询某个商品的价格，可能需要进行数据库查询。然而，这个被依赖服务的稳定性是不能保证的。如果依赖的服务出现了不稳定的情况，请求的响应时间变长，那么调用服务的方法的响应时间也会变长，线程会产生堆积，最终可能耗尽业务自身的线程池，服务本身也变得不可用。

![chain](https://user-images.githubusercontent.com/9434884/62410811-cd871680-b61d-11e9-9df7-3ee41c618644.png)

> 现代微服务架构都是分布式的，由非常多的服务组成。不同服务之间相互调用，组成复杂的调用链路。以上的问题在链路调用中会产生放大的效果。复杂链路上的某一环不稳定，就可能会层层级联，最终导致整个链路都不可用。因此我们需要对不稳定的**弱依赖服务调用**进行熔断降级，暂时切断不稳定调用，避免局部不稳定因素导致整体的雪崩。熔断降级作为保护自身的手段，通常在客户端（调用端）进行配置。

#### 熔断策略

*练习项目为1.7版本，Sentinal在1.8版本发生较大变化。*

![image-20220724214409625](https://s2.loli.net/2022/07/24/hyPgVQ64SG8f5ZU.png)

- RT

  ![image-20220724220136770](https://s2.loli.net/2022/07/24/tC56sP7M42K3zgb.png)

  ![img](https://s2.loli.net/2022/07/24/3dT8XKNj1a4VwGH.png)

  

- 异常比例

  ![img](https://s2.loli.net/2022/07/24/3FaMbfgDiCZ291y.png)

  ![img](https://s2.loli.net/2022/07/24/G1LFSgRYWufNZM5.png)

- 异常数

  ![img](https://s2.loli.net/2022/07/24/icyNZFVkQzrOAh2.png)

  时间窗口一定要大于等于60s

  

  ![img](https://s2.loli.net/2022/07/24/ebTR5nMzh7WXo1Q.png)



#### 半开状态

在 Hystrix 种，断路器开启后达到一个时间周期时，会变更断路器的状态为 Half-Open (半开) 状态，此时会允许一个请求到下游服务，若请求正常则 closed (关闭) 断路器，否则继续 open (打开) 断路器。等待下一个时间周期，以此循环。

![image-20220724215038759](https://s2.loli.net/2022/07/24/Psa7LoHbf1NXmir.png)

而断路器开启时间到达5s后，变更状态为 Half-Open (半开) 状态，允许一个请求转发到下游服务检测下游服务是否正常，若下游服务正常，断路器 closed（关闭）。否则继续 open（开启），继续等待下一个周期。

**Sentinel 的断路器在 1.8 版本之前是不支持半开状态的。**

#### 1.8版本熔断策略

- 慢调用比例 (`SLOW_REQUEST_RATIO`)：选择以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），请求的响应时间大于该值则统计为慢调用。当单位统计时长（`statIntervalMs`）内请求数目大于设置的最小请求数目，并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断。
- 异常比例 (`ERROR_RATIO`)：当单位统计时长（`statIntervalMs`）内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是 `[0.0, 1.0]`，代表 0% - 100%。
- 异常数 (`ERROR_COUNT`)：当单位统计时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。



### 热点key限流

热点规则，能够对某个资源的参数进行限制。

![b68e22d702c646bc84b830767b10af7d](E:\Desktop\b68e22d702c646bc84b830767b10af7d.png)

比如对第1个参数 p1 进行限制，达到指定 QPS 进行限流。若不指定 p1 ，不会触发限流。

```java
http://localhost:8401/testHotKey?p1=5&p2=1
```

**参数例外项**

可以针对参数，设置特殊值进行单独限流。

比如设置 p1=5 时，达到指定的 QPS=10 才会触发限流。

![image-20220801222129332](https://s2.loli.net/2022/08/01/cyMWpO79JGEPLfT.png)





### @SentinelResource

```
@SentinelResource(value = "customerBlockHandler",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "blockHandlerExce")
```

![3d323ed95b024571a83ee5ef0dd220bb](E:\Desktop\3d323ed95b024571a83ee5ef0dd220bb.png)










## 参考链接

- [2020周阳SpringCloud完整版笔记一](https://www.cnblogs.com/dataoblogs/p/14121874.html)

- [Hystrix-深入 Hystrix 断路器执行原理](https://zhuanlan.zhihu.com/p/84403081)

- [这篇SpringCloud GateWay 详解，你用的到](https://juejin.cn/post/7107911617601339423)

- [SpringCloud 学习笔记](https://blog.csdn.net/shuai_aaaaaaa/article/details/122550335)

  
