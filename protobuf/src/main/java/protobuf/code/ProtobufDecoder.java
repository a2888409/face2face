package protobuf.code;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ProtobufDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ProtobufDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {

        int length = checkLength(ctx, in);
        if (length == -1 || length == 0)
            return;

        ByteBuf byteBuf = Unpooled.buffer(length);
        in.readBytes(byteBuf);

        try {
            byte[] inByte = byteBuf.array();

            // 解密消息体
            //ThreeDES des = ctx.channel().attr(ClientAttr.ENCRYPT).get();
            //byte[] bareByte = des.decrypt(inByte);

            /*-----------------------------------------------------------------------
            Message msg = Message.parseFrom(bareByte);
            logger.info("[APP-SERVER][RECV][remoteAddress:"
                    + ctx.channel().remoteAddress() + "][total length:"
                    + length + "][bare length:" + msg.getSerializedSize()
                    + "]:\r\n" + msg.toString());

            if (msg != null) {
                // 获取业务消息头
                out.add(msg);

            }
            -----------------------------------------------------------------------*/
        } catch (Exception e) {
            logger.error(ctx.channel().remoteAddress() + ",decode failed.", e);
        }
    }

    int checkLength(ChannelHandlerContext ctx, ByteBuf in){
        in.markReaderIndex();

        if (in.readableBytes() < 2) {
            logger.error("readableBytes length less than 2 bytes");
            return 0;
        }

        int length = in.readUnsignedShort();

        if (length < 0) {
            ctx.close();
            logger.error("message length less than 0, channel closed");
        }

        if (length > in.readableBytes()) {
            in.resetReaderIndex();
            logger.error("message received is incomplete");
            return -1;
        }
        return length;
    }
}
