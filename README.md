# face2face
##基于netty的实时聊天(IM)服务器。<br>
实现基本功能后的目标是将每个服务设计为可水平扩展的集群，并且重点放在架构上的优化而不是逻辑功能上。<br>
auth服务：负责登录认证。<br> 
gate服务：负责客户端接入，也是服务器和客户端通信的媒介。<br> 
logic服务：负责处理各种业务逻辑。<br>

###quicksStart:
windows:<br>
1、使用intellij maven方式导入该工程，执行mvn clean compile<br>
2、启动本地redis服务:使用windows cmd命令行进入到thirdparty模块resources目录下的redis文件夹，执行redis-server.exe redis.windows.conf<br>
3、按照提示，设置好auth logic gate正确的配置文件路径，依次启动auth、logic、gate服务<br>
<br>
注册和登录功能，测试方法如下(建议跟踪断点)：<br>
1、启动client模块中的测试用客户端，按照提示依次任意输入2个用户名，如name1 name2，服务便会自动执行注册，登录的流程。<br>
<br>
单聊功能测试方法：<br>
1、启动第一个client模块中的测试用客户端1，按照提示依次输入用户名test，aaa<br>
2、再启动第二个客户端2，依次输入aaa，test<br>
3、在启动的客户端1输入任意文字，之后便能在第客户端2查看到客户端1发送的文字<br>
