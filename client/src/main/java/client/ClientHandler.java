package client;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.Utils;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.cli2srv.login.Auth;

import java.util.Scanner;

/**
 * Created by Dell on 2016/2/15.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    public static ChannelHandlerContext _gateClientConnection;

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    String _userId = "";
    String _friend = "";
    boolean _verify = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        _gateClientConnection = ctx;
        String passwd = "123";

        Scanner sc = new Scanner(System.in);
        logger.info("Please Enter YourSelf UserId:");
        _userId = sc.nextLine();

        logger.info("Please Enter Userid Who You Want To Chat: ");
        _friend = sc.nextLine();

        sendCRegister(ctx, _userId, passwd);
        sendCLogin(ctx, _userId, passwd);
    }

    void sendCRegister(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CRegister.Builder cb = Auth.CRegister.newBuilder();
        cb.setUserid(userid);
        cb.setPasswd(passwd);

        ByteBuf byteBuf = Utils.pack2Client(cb.build());
        ctx.writeAndFlush(byteBuf);
    }

    void sendCLogin(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CLogin.Builder loginInfo = Auth.CLogin.newBuilder();
        loginInfo.setUserid(userid);
        loginInfo.setPasswd(passwd);
        loginInfo.setPlatform("ios");
        loginInfo.setAppVersion("1.0.0");

        ByteBuf byteBuf = Utils.pack2Client(loginInfo.build());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message msg) throws Exception {
        logger.info("received message: {}", msg.getClass());
        if(msg instanceof Auth.SResponse) {
            Auth.SResponse sp = (Auth.SResponse) msg;
            int code = sp.getCode();
            String desc = sp.getDesc();
            switch (code) {
                //登录成功
                case Common.VERYFY_PASSED:
                    logger.info("Login succeed, description: {}", desc);
                    _verify = true;
                    break;
                //登录账号不存在
                case Common.ACCOUNT_INEXIST:
                    logger.info("Account inexsit, description: {}", desc);
                    break;
                //登录账号或密码错误
                case Common.VERYFY_ERROR:
                    logger.info("Account or passwd Error, description: {}", desc);
                    break;
                //账号已被注册
                case Common.ACCOUNT_DUMPLICATED:
                    logger.info("Dumplicated registry, description: {}", desc);
                    break;
                //注册成功
                case Common.REGISTER_OK:
                    logger.info("User registerd successd, description: {}", desc);
                    break;
                case Common.Msg_SendSuccess:
                    logger.info("Chat Message Send Successed, description: {}", desc);
                default:
                    logger.info("Unknow code: {}", code);
            }
        } else if(msg instanceof Chat.SPrivateChat) {
            logger.info("{} receiced chat message: {}", _userId, ((Chat.SPrivateChat) msg).getContent());
        }

        //这样设置的原因是，防止两方都阻塞在输入上
        if(_verify && (_userId.equals("test"))) {
            loop();
        }
    }

    void loop() {
        logger.info("WelCome To Face2face Chat Room, You Can Say Something Now: ");
        Scanner sc = new Scanner(System.in);
        String content = sc.nextLine();
        logger.info("{} Send Message: {} to {}", _userId, content, _friend);

        Chat.CPrivateChat.Builder cp = Chat.CPrivateChat.newBuilder();
        cp.setContent(content);
        cp.setSelf(_userId);
        cp.setDest(_friend);

        ByteBuf byteBuf = Utils.pack2Client(cp.build());
        _gateClientConnection.writeAndFlush(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
