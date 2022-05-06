# SpringCloud

### 学习地址

- [尚硅谷-SpringCloud2020视频地址](https://www.bilibili.com/video/BV18E411x7eT?spm_id_from=333.337.search-card.all.click)
- [Gitee笔记-轻狂书生/SpringCloud](https://gitee.com/cunjinFS/SpringCloud/tree/master)



## Eureka

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

   



eureka正常运行，客户端正常注册之后，访问eureka服务端地址（Ip:Port），即可看到服务列表。

![image-20220507000000951](https://cdn.jsdelivr.net/gh/AlbertYang0801/pic-bed@main/img/image-20220507000000951.png)