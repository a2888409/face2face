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
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.internal.Internal;

import java.util.HashMap;

public class AuthServerHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(AuthServerHandler.class);
    private static ChannelHandlerContext _gateAuthConnection;

    public static void setGateAuthConnection(ChannelHandlerContext ctx) {
        _gateAuthConnection = ctx;
    }

    public static ChannelHandlerContext getGateAuthConnection() {
        if(_gateAuthConnection != null) {
            return _gateAuthConnection;
        } else {
            return null;
        }
    }

    private static HashMap<String, Long> userid2netidMap = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Internal.GTransfer gt = (Internal.GTransfer) message;
        int ptoNum = gt.getPtoNum();
        Message msg = ParseMap.getMessage(ptoNum, gt.getMsg().toByteArray());

        IMHandler handler;
        if(msg instanceof Internal.Greet) {
            //来自gate的连接请求
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), gt.getNetId(), msg, channelHandlerContext);
        } else {
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), gt.getNetId(), msg, getGateAuthConnection());
        }

        Worker.dispatch(gt.getUserId(), handler);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // super.exceptionCaught(ctx, cause);
        logger.error("An Exception Caught");
    }

    public static void putInUseridMap(String userid, Long netId) {
        userid2netidMap.put(userid, netId);
    }

    public static Long getNetidByUserid(String userid) {
        Long netid = userid2netidMap.get(userid);
        if( netid != null) {
            return netid;
        } else {
            return null;
        }
    }
}
