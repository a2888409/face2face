# face2face
![image](https://github.com/a2888409/face2face/blob/master/arch.png)<br> 
`auth服务`：负责登录认证。<br> 
`gate服务`：负责客户端接入，也是服务器和客户端通信的媒介。<br> 
`logic服务`：负责处理各种业务逻辑。<br>

为以后将服务设计为**水平可扩展**的服务准备，将整个服务拆分为3个进程。<br>
实现基本功能后的目标是将每个服务设计为可水平扩展的集群，并且重点放在架构上的优化而不是逻辑功能上。<br>

## **一、QuicksStart**:
**(a)启动服务器:**<br>

 1. 使用intellij `maven方式`导入该工程，执行`mvn clean compile`<br>
 2. 在对应模块的resources目录下配置auth、logic服`redis数据库地址`。<br>
 3. 启动redis服务：thirdparty中附带了一个windows cmd命令行可以直接启动的redis进程。只需用cmd进入该目录，执行：`redis-server.exe redis.windows.conf`<br>
 4. 打开intellij的debug/Run configuration根据工程路径重新配置auth logic gate服务启动项**program argument**的`-cfg`选项：(比如工程clone在了E盘code目录，那么就把logic的启动项改为-cfg E:\code\face2face\logic\src\main\resources\logic.xml)，auth、gate同理。<br>
 5. 按顺序启动`logic`、`auth`、`gate服务`(因服务间断线重连暂时未加入)<br>
<br>

**(b)启动客户端**：<br>
测试注册和登录功能，以及单聊功能(建议跟踪断点)：<br>

 - 按照quickstart流程依次启动服务器后，再启动client模块的下的客户端(Client类)，服务便会自动执行注册，登录的流程，并每隔100ms给自己发送聊天信息。<br>

## **二、添加业务逻辑**<br>
 - `协议流动方式介绍`：客户端先连接gate，gate服务根据客户端发送的协议类型转发到auth服或者logic，到达auth或者logic之后，IO线程将消息dispatch到后端worker线程处理。<br>
 - `定制业务逻辑`：<br>
    - 只需在三个地方注册协议：protobuf模块的**ParseRegistryMap**，gate模块的**TransferHandlerMap**，最后是根据协议类型在auth模块或者logic模块的**HandlerManager**注册即可。注意：HandlerManager注册的是协议最终处理的业务逻辑。<br>
    - 在protobuf模块对应**.proto**文件添加你自己定义的协议，执行**proto.bat**即可生成对应pb文件。<br>

## **三、压力测试**<br>
 1. client模块中的`client.Client`类提供了进行压力测试的方法，可以修改启动客户端连接的数量`Client.clientNum`，以及每秒向服务器发送的协议的频率`Client.frequency`进行压力测试。<br>
 2. CPU 8核E3-1231v3， 每个服务分配1G的堆内存，启动5000个客户端后(需要一定时间)，不停给自己发送单聊协议，发现auth、logic、gate服务占用的cpu非常低，客户端能够立即收到响应。对应的TPS统计将在后续加入。<br>

## **四、水平扩展思路(processing)**<br>
实际上最简单的做法，就是利用`消息中间件`对服务之间的调用进行**解耦**，这里以消息中间件RocketMQ为例子：
```
    client(Producer) ->  gate(Consumer)
```
所有用户我们都可以看做是**生产者**，用户操作对应生产者产生消息，gate网关服此时看做是**消费者**订阅用户产生的消息。我们使用RocketMQ的`顺序消息`特性，保证同个用户的消息都转发到同一个消息队列，这样就保证了同个用户操作的先后。
```
    gate(Producer)   ->  auth(Consumer)
    gate(Producer)   ->  logic(Consumer)
```
网关服同时又可以看做是生产者，他接收到用户请求后产生逻辑操作消息，这些逻辑操作消息根据不同的类型，又可以被auth服，logic服订阅。auth，logic收到订阅的消息，此时才进行真正的消费。<br>
**使用集群模式，消费者是可以任意水平扩展的，rocketmq天生的优势保证了只要生产者消息不丢，即使某个消费者挂了，消息也能够被集群中其他consumer正确消费，但是消费的幂等性要自己保证。**













