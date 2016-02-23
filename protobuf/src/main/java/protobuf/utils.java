package protobuf;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import protobuf.analysis.ParseMap;

/**
 * Created by Dell on 2016/2/23.
 */
public class Utils {
    public static void packAndSend(Message msg, ChannelHandlerContext dest) {
        byte[] bytes = msg.toByteArray();
        int length =bytes.length;
        int ptoNum = ParseMap.msg2ptoNum.get(msg);

        ByteBuf buf = Unpooled.buffer(8 + length);
        buf.writeInt(length);
        buf.writeInt(ptoNum);     //协议号
        buf.writeBytes(bytes);

        dest.writeAndFlush(buf);
    }
}
