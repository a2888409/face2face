package gate;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Dell on 2016/2/1.
 */
public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg)
            throws Exception {}

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        //解析出ptoNum再进行转给相应的服务
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    }
