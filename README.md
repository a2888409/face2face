# face2face
##基于netty的实时聊天(IM)服务器。<br>
为以后将服务设计为水平可扩展的服务准备，将整个服务拆分为3个进程。<br>
实现基本功能后的目标是将每个服务设计为可水平扩展的集群，并且重点放在架构上的优化而不是逻辑功能上。<br>
auth服务：负责登录认证。<br> 
gate服务：负责客户端接入，也是服务器和客户端通信的媒介。<br> 
logic服务：负责处理各种业务逻辑。<br>

###quicksStart:
启动服务器:<br>
1、使用intellij maven方式导入该工程，执行mvn clean compile<br>
2、在对应模块的resources目录下配置auth、gate服redis数据库地址。<br>
3、启动redis服务：thirdparty中附带了一个windows cmd命令行可以直接启动的redis进程。只需用cmd进入该目录，执行：redis-server.exe redis.windos.conf
4、根据工程路径重新配置auth logic gate服务启动项的-cfg选项：(比如工程clone在了E盘code目录，那么就把logic的启动项改为-cfg E:\code\face2face\logic\src\main\resources\logic.xm)，auth、gate同理。<br>
5、依次启动logic、auth、gate服务(因服务间断线重连暂时未加入)<br>
<br>
启动客户端：测试注册和登录功能，以及单聊功能(建议跟踪断点)：<br>
1、按照quickstart流程依次启动服务器后，再启动client模块的客户端，服务便会自动执行注册，登录的流程，并每隔100ms给自己发送聊天信息。<br>
