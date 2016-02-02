package gate.utils;

import gate.GateServer;
import gate.starter.GateStarter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.generate.internal.Internal;
import thridparty.ThreeDES.ThreeDES;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Qzy on 2016/1/29.
 * 客户端连接的封装类
 */

public class ClientConnection {
    private final AtomicLong netidGenerator = new AtomicLong(0);
    ClientConnection(ChannelHandlerContext c) {
        netId = netidGenerator.incrementAndGet();
        ctx = c;
    }

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    public static AttributeKey<ThreeDES> ENCRYPT = AttributeKey.valueOf("encrypt");
    public static AttributeKey<Long> NETID = AttributeKey.valueOf("netid");

    private long userId;
    private long netId;
    private ChannelHandlerContext ctx;

    public long getNetId() {
        return netId;
    }

    public long getUserId() {
        return userId;
    }

    public void readUserIdFromDB() {

    }


}
