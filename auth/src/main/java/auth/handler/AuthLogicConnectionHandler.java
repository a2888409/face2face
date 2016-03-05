package auth.handler;

import auth.HandlerManager;
import auth.IMHandler;
import auth.Worker;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.analysis.ParseMap;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.internal.Internal;

/**
 * Created by win7 on 2016/3/5.
 */
public class AuthLogicConnectionHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(AuthLogicConnectionHandler.class);

    private static ChannelHandlerContext _authLogicConnection;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        setAuthLogicConnecttion(ctx);
        logger.info("[Auth-Logic] connection is established");

        //向logic发送Greet协议
        sendGreet2Logic();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Internal.GTransfer gt = (Internal.GTransfer) message;
        int ptoNum = gt.getPtoNum();
        Message msg = ParseMap.getMessage(ptoNum, gt.getMsg().toByteArray());

        IMHandler handler = null;
        if(msg instanceof Chat.CPrivateChat) {
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), -1L, msg, AuthServerHandler.getGateAuthConnection());
        } else {
            logger.error("Error Messgae Type: {}", msg.getClass());
            return;
        }

        Worker.dispatch(gt.getUserId(), handler);

    }

    private void sendGreet2Logic() {
        Internal.Greet.Builder ig = Internal.Greet.newBuilder();
        ig.setFrom(Internal.Greet.From.Auth);
        ByteBuf out = Utils.pack2Server(ig.build(), ParseRegistryMap.GREET, -1, Internal.Dest.Logic, "admin");
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
