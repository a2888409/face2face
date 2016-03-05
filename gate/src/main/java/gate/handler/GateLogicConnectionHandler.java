package gate.handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.internal.Internal;

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

        //向logic发送Greet
        sendGreet2Logic();
    }
    @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

    }

    private void sendGreet2Logic() {
        Internal.Greet.Builder ig = Internal.Greet.newBuilder();
        ig.setFrom(Internal.Greet.From.Gate);
        ByteBuf out = Utils.pack2Server(ig.build(), ParseRegistryMap.GREET, -1, Internal.Dest.Logic, "admin");
        getGatelogicConnection().writeAndFlush(out);
        logger.info("Gate send Green to Logic.");
    }
}
