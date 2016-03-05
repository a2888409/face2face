package auth.utils;

import auth.handler.AuthServerHandler;
import io.netty.buffer.ByteBuf;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.cli2srv.login.Auth;
import protobuf.generate.internal.Internal;

/**
 * Created by win7 on 2016/3/3.
 */
public class RouteUtil {
    public static void sendResponse(int code, String desc, long netId,String userId) {
        Auth.SResponse.Builder sb = Auth.SResponse.newBuilder();
        sb.setCode(code);
        sb.setDesc(desc);

        ByteBuf byteBuf = Utils.pack2Server(sb.build(), ParseRegistryMap.SRESPONSE, netId, Internal.Dest.Client, userId);
        AuthServerHandler.getGateAuthConnection().writeAndFlush(byteBuf);
    }
}
