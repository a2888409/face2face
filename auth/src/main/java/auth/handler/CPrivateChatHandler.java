package auth.handler;

import auth.utils.Common;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import auth.IMHandler;
import auth.Worker;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.internal.Internal;

/**
 * Created by win7 on 2016/3/5.
 */
public class CPrivateChatHandler extends IMHandler{
    private static final Logger logger = LoggerFactory.getLogger(CPrivateChatHandler.class);

    public CPrivateChatHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) throws TException {
        Chat.CPrivateChat msg = (Chat.CPrivateChat) _msg;
        ByteBuf byteBuf;

        String dest = msg.getDest();
        Long netid = AuthServerHandler.getNetidByUserid(dest);
        if(netid == null) {
            logger.error("Dest User not online");
            return;
        }

        Chat.SPrivateChat.Builder sp = Chat.SPrivateChat.newBuilder();
        sp.setContent(msg.getContent());
        byteBuf = Utils.pack2Server(sp.build(), ParseRegistryMap.SPRIVATECHAT, netid, Internal.Dest.Gate, dest);
        _ctx.writeAndFlush(byteBuf);

        logger.info("message has send from {} to {}", msg.getSelf(), msg.getDest());
    }
}
