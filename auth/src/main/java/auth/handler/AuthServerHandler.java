package auth.handler;

/**
 * Created by Dell on 2016/2/18.
 */

import auth.HandlerManager;
import auth.IMHandler;
import auth.Worker;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.analysis.ParseMap;
import protobuf.generate.internal.Internal;

public class AuthServerHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(AuthServerHandler.class);
    private static ChannelHandlerContext _gateAuthConnection;

    public static ChannelHandlerContext getGateAuthConnection() {
        return _gateAuthConnection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _gateAuthConnection = ctx;
        logger.info("[Gate-Auth] connection is established");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Internal.GTransfer gt = (Internal.GTransfer) message;
        int ptoNum = gt.getPtoNum();
        long netId = gt.getNetId();
        Message msg = ParseMap.getMessage(ptoNum, gt.getMsg().toByteArray());
        String userId = gt.getUserId();

        IMHandler handler = HandlerManager.getHandler(ptoNum, userId, netId, msg, getGateAuthConnection());
        Worker.dispatch(userId, handler);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // super.exceptionCaught(ctx, cause);
        logger.error("An Exception Caught");
    }
}
