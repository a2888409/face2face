package gate;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protobuf.analysis.ParseMap;

/**
 * Created by Dell on 2016/2/1.
 */
public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //保存客户端连接
    }


    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        //解析出ptoNum再转给相应的服务
        int ptoNum = ParseMap.msg2ptoNum.get(message.getClass());

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }
}
