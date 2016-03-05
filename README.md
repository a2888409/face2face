# face2face
##基于netty的分布式实时聊天(IM)服务器。<br> 
实现基本功能后的目标是将每个服务设计为可水平扩展的集群，并且重点放在架构上的优化而不是逻辑功能上。<br>
auth服务：负责登录认证。<br> 
gate服务：负责客户端接入，也是服务器和客户端通信的媒介。<br> 
logic服务：负责处理各种业务逻辑。<br>

###quicksStart:
windows:<br>
1、使用intellij maven方式导入该工程，执行mvn clean compile<br>
2、启动本地redis服务:使用windows cmd命令行进入到thirdparty模块resources目录下的redis文件夹，执行redis-server.exe redis.windows.conf<br>
3、按照提示，设置好auth logic gate正确的配置文件路径，依次启动auth、logic、auth服务<br>
<br>
注册和登录功能，测试方法如下(建议跟踪断点)：<br>
1、启动client模块中的测试用客户端，根据提示输入你的用户名，以及friend用户名，服务便会自动执行注册，登录的流程。<br>
<br>
单聊功能测试方法：<br>
1、启动第一个client模块中的测试用客户端，输入用户名admin，以及聊天对象friend用户名假设为test<br>
2、按照步骤1，再启动第二个client客户端，依次输入test，admin<br>
3、在启动的第一个客户端输入任意文字，之后便能在第2个客户端查看到客户端1发送的文字<br>
