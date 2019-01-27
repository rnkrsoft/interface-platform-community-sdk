# 接口平台

[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.rnkrsoft.platform/interface-platform-standalone/badge.svg)](http://search.maven.org/#search|ga|1|g%3A%22com.rnkrsoft.platform%22%20AND%20a%3A%22interface-platform-standalone%22)

```xml
<dependency>
    <groupId>com.rnkrsoft.platform</groupId>
    <artifactId>interface-platform-standalone</artifactId>
    <version>最新版本号</version>
</dependency>
```

## 接口平台的开发目的

用于解决一个组织内外的通信问题，并将通信过程进行记录，用于进行相关的行为分析。

## 接口平台场景

1. 外部系统请求一个对外暴露服务的内部系统

2. 内部系统主动请求一个外部系统

## 通信方式

1. 同步通信

   1. 请求后保持通信链路，在请求处理后返回应答

2. 异步通信

   1. 请求后直接返回，并不应答
   2. 处理完成后进行应答

## 幂等性

1. 在通信过程中同一个标识的请求可进行多次请求，保证有相同的结果返回


# 流程

## 外部请求内部系统

com.rnkrsoft.platform.InnerInterfaceEngine
1. 验证交易码是否为空

2. 验证IP的有效性，是否根据城市是否允许访问

3. 根据交易码获取交易定义信息

4. 根据交易定义检查是否验证TOKEN

5. 将接收到的报文填充到outerMessage

6. 获取安全服务实现

7. 根据相应算法进行验签和解密

8. 调用转换服务进行 从outerMessage-->innerObject

9. 创建请求记录

10. 根据服务类型（Local,SpringBean,Dubbo）调用服务，发生错误创建应答记录，并更新请求记录

11. 调用转换服务进行 从innerObject-->outerMessage

12. 根据相应算法进行加密和签字

13. 创建应答记录

14. 返回结果

## 内部请求外部系统

com.rnkrsoft.platform.OuterInterfaceEngine

1. 根据服务接口上标注注解的txNo创建请求对象
2. 验证交易码是否为空
3. 获取交易定义信息
4. 调用转换服务进行 从innerObject-->outerMessage
5. 根据相应算法进行加密和签字
6. 创建请求记录
7. 根据发送服务类型（HTTP,Dubbo）调用发送服务，发送错误创建应答记录，并更新请求记录
8. 接收到应答后，根据相应算法进行验签和解密
9. 调用转换服务进行 从outerMessage-->innerObject
10. 创建应答记录
11. 根据innerObject应答码更新请求记录
12. 返回innerObject对象



# 搭建

访问https://github.com/rnkrsoft/interface-platform-sdk-demo下载使用Demo快速搭建例子

