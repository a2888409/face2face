package auth.handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.Utils;
import protobuf.generate.internal.Internal;

/**
 * Created by win7 on 2016/3/5.
 */
public class AuthLogicConnectionHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(AuthLogicConnectionHandler.class);

    private static ChannelHandlerContext _authLogicConnection;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _authLogicConnection = ctx;
        logger.info("[Auth-Logic] connection is established");

        //向logic发送Greet协议
        sendGreet2Logic();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
    }

    private void sendGreet2Logic() {
        Internal.Greet.Builder ig = Internal.Greet.newBuilder();
        ig.setFrom(Internal.Greet.From.Auth);
        ByteBuf out = Utils.pack2Server(ig.build(), 901, -1, Internal.Dest.Logic, "admin");
        getAuthLogicConnection().writeAndFlush(out);
        logger.info("Auth send Green to Logic.");
    }

    public static ChannelHandlerContext getAuthLogicConnection() {
        return _authLogicConnection;
    }

    public static void setAuthLogicConnecttion(ChannelHandlerContext ctx) {
        _authLogicConnection = ctx;
    }
}
