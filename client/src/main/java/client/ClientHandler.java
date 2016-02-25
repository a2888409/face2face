package client;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.Utils;
import protobuf.generate.cli2srv.login.Auth;

/**
 * Created by Dell on 2016/2/15.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    String _userId = "qzy";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        sendCRegister(ctx);
        sendCLogin(ctx);
    }

    void sendCRegister(ChannelHandlerContext ctx) {
        Auth.CRegister.Builder cb = Auth.CRegister.newBuilder();
        cb.setUserid("qzy");
        cb.setPasswd("123");

        ByteBuf byteBuf = Utils.pack2Client(cb.build());
        ctx.writeAndFlush(byteBuf);
    }

    void sendCLogin(ChannelHandlerContext ctx) {
        Auth.CLogin.Builder loginInfo = Auth.CLogin.newBuilder();
        loginInfo.setUserid("qzy");
        loginInfo.setPasswd("123");
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
                case 400 :
                    logger.info("Login succeed, description: {}", desc);
                case 404:
                    logger.info("Login Failed, description: {}", desc);
            }
        }


    }

    void loop() {
        logger.info("WelCome to face2face Chat room !");
        while(true) {

        }
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
