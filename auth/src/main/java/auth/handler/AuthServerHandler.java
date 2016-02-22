package auth.handler;

/**
 * Created by Dell on 2016/2/18.
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.generate.cli2srv.login.Auth;

public class AuthServerHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(AuthServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("[Gate-Auth] connection is established");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if(message instanceof Auth.CRegister)
            dealWithRegistry((Auth.CRegister)message);
        else if(message instanceof Auth.CLogin)
            dealWithAuthCmd((Auth.CLogin)message);
    }

    void dealWithRegistry(Auth.CRegister msg) {
        String accountName = msg.getAccount();
        String passWd = msg.getPasswd();

    }

    void dealWithAuthCmd(Auth.CLogin msg) {
        long userId = msg.getUserId();

    }
}
