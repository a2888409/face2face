package auth.handler;

import auth.IMHandler;
import auth.Worker;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by win7 on 2016/3/5.
 */
public class GreetHandler extends IMHandler {
    private static final Logger logger = LoggerFactory.getLogger(GreetHandler.class);

    public GreetHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) throws TException {
        AuthServerHandler.setGateAuthConnection(_ctx);
        logger.info("[Gate-Auth] connection is established");
    }
}
