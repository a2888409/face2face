package gate.utils;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by Dell on 2016/2/2.
 */
public class ClientConnectionMap {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionMap.class);

    //保存一个gateway上所有的客户端连接
    public static HashMap<Long, ClientConnection> allClientMap = new HashMap<>();

    public static ClientConnection getClientConnection(ChannelHandlerContext ctx) {
        Long netId = ctx.attr(ClientConnection.NETID).get();

        ClientConnection conn = allClientMap.get(netId);
        if(conn != null)
            return conn;
        else {
            logger.error("ClientConenction not found in allClientMap, netId: {}", netId);
        }
        return null;
    }

}
