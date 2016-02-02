package gate.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import thridparty.ThreeDES.ThreeDES;

/**
 * Created by Qzy on 2016/1/29.
 * 客户端连接的封装类
 */

public class ClientConnection {
    public static AttributeKey<ThreeDES> ENCRYPT = AttributeKey.valueOf("encrypt");

    private long userId;
    private ChannelHandlerContext ctx;

}
