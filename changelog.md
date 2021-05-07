# 1.2
### Release Note
  - Bug Fixed
  - 在application.yml中配置多环境启动(dev和pro)
  

# 1.1
#### BugFix :

  - 查询消息时, 用户昵称显示乱码问题
  - 部分页面checkToken接口返回异常问题
 
#### Feature :

 - 改用MDC来设置调用链标识requestId并在application.yml中统一配置pattern, 避免每次打印业务日志都要从ThreadContext类中提取
 - 利用AOP切面类打印统一的API日志
 - 优化日志输出格式为: "[%date][%-5level][reqId:%X{requestId}][%logger] - %msg%n"
    ```yaml
   logging:
      pattern:
        console: "[%date][%-5level][reqId:%X{requestId}][%logger] - %msg%n"
    ```
    
 
# 1.0

#### Description 
LHC(lake-hill-community) 1.0版本, 第一个正式版本, 包含4项服务:

- lhc-eureka-server: Eureka服务器(服务发现注册中心)
- lhc-zuul-server: API网关
- lhc-user: 用户模块, 提供账号, 鉴权等服务
- lhc-msg: 消息模块, 支持公共聊天室服务
