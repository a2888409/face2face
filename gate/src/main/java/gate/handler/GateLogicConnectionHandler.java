package gate.handler;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dell on 2016/2/2.
 */
public class GateLogicConnectionHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(GateLogicConnectionHandler.class);
    private static ChannelHandlerContext _gateLogicConnection;

    public static ChannelHandlerContext getGatelogicConnection() {
        return _gateLogicConnection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _gateLogicConnection = ctx;
        logger.info("[Gate-Logic] connection is established");
    }
    @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

    }
}
