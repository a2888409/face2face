package gate.handler;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Dell on 2016/2/2.
 */
public class GateAuthConnectionHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

    }

}
