# face2face
##基于netty的实时聊天(IM)服务器。<br>
实现基本功能后的目标是将每个服务设计为可水平扩展的集群，并且重点放在架构上的优化而不是逻辑功能上。<br>
auth服务：负责登录认证。<br> 
gate服务：负责客户端接入，也是服务器和客户端通信的媒介。<br> 
logic服务：负责处理各种业务逻辑。<br>

###quicksStart:
windows:<br>
1、使用intellij maven方式导入该工程，执行mvn clean compile<br>
2、在对应模块的resources目录下配置auth、gate服redis数据库地址。工程提供了一个windows可以直接启动的redis服务:使用windows cmd命令行进入到thirdparty模块resources目录下的redis文件夹，执行redis-server.exe redis.windows.conf<br>
3、重新配置auth logic gate服务启动项的-cfg选项指向的对应模块resources目录的xml配置文件(比如工程clone在了E盘根目录，那么就把logic的启动项改为-cfg E:\face2face\logic\src\main\resources\logic.xm)，auth、gate同理。<br>
4、依次启动logic、auth、gate服务(因服务间断线重连暂时未加入)<br>
<br>
注册和登录功能，以及单聊功能， 测试方法(建议跟踪断点)：<br>
1、启动client模块中的单个测试用客户端，服务便会自动执行注册，登录的流程，并每隔100ms源源不断的给自己发送信息。<br>
