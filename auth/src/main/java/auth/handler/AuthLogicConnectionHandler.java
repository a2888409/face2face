package auth.handler;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by win7 on 2016/3/5.
 */
public class AuthLogicConnectionHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(AuthLogicConnectionHandler.class);

    private static ChannelHandlerContext _authLogicConnection;
    public static ChannelHandlerContext getGateLogicConnection() {
        return _authLogicConnection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _authLogicConnection = ctx;
        logger.info("[Auth-Logic] connection is established");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

    }
}
