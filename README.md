# face2face
##基于netty的异步非阻塞实时聊天(IM)服务器。<br>
本项目也可作为事件驱动的游戏服务器。<br>
项目整体架构如下：<br>
![image](https://github.com/a2888409/face2face/blob/master/arch.png)<br> 
auth服务：负责登录认证。<br> 
gate服务：负责客户端接入，也是服务器和客户端通信的媒介。<br> 
logic服务：负责处理各种业务逻辑。<br>

为以后将服务设计为水平可扩展的服务准备，将整个服务拆分为3个进程。<br>
实现基本功能后的目标是将每个服务设计为可水平扩展的集群，并且重点放在架构上的优化而不是逻辑功能上。<br>

###quicksStart:
启动服务器:<br>
1、使用intellij maven方式导入该工程，执行mvn clean compile<br>
2、在对应模块的resources目录下配置auth、gate服redis数据库地址。<br>
3、启动redis服务：thirdparty中附带了一个windows cmd命令行可以直接启动的redis进程。只需用cmd进入该目录，执行：redis-server.exe redis.windos.conf<br>
4、根据工程路径重新配置auth logic gate服务启动项的-cfg选项：(比如工程clone在了E盘code目录，那么就把logic的启动项改为-cfg E:\code\face2face\logic\src\main\resources\logic.xm)，auth、gate同理。<br>
5、依次启动logic、auth、gate服务(因服务间断线重连暂时未加入)<br>
<br>
启动客户端：测试注册和登录功能，以及单聊功能(建议跟踪断点)：<br>
1、按照quickstart流程依次启动服务器后，再启动client模块的下的客户端(Client类)，服务便会自动执行注册，登录的流程，并每隔100ms给自己发送聊天信息。<br>
<br>
###添加自己定制的业务逻辑<br>
1、协议流动方式介绍：客户端先连接gate，gate服务根据客户端发送的协议类型转发到auth服或者logic，到达auth或者logic之后，IO线程将消息dispatch到后端worker线程处理。<br>
2、定制业务逻辑：<br>
   (1)只需在三个地方注册协议：protobuf模块的ParseRegistryMap，gate模块的TransferHandlerMap，最后是根据协议类型在auth模块或者logic模块的HandlerManager注册即可。注意：HandlerManager注册的是协议最终处理的业务逻辑。<br>
   (2)在protobuf模块对应.proto文件添加你自己定义的协议，执行proto.bat即可生成对应pb文件
<br>
###压力测试<br>
1、client模块的AIOClient提供了进行压力测试的方法，可以修改启动客户端线程的数量AIOClient.aioClientNum，以及每秒向服务器发送的协议的频率AIOClient.frequency进行压力测试。<br>
2、CPU 8核E3-1231v3， 每个服务分配1G的堆内存，启动3000个客户端，不停发送单聊协议，发现auth、logic、gate服务占用的cpu非常低，客户端能够立即收到相应。压力主要集中在创建客户端的机器上，3000个客户端大约1G内存，cpu接近100%<br>




