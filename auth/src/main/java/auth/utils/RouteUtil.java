package auth.utils;

import auth.handler.AuthServerHandler;
import io.netty.buffer.ByteBuf;
import protobuf.Utils;
import protobuf.generate.cli2srv.login.Auth;
import protobuf.generate.internal.Internal;

/**
 * Created by win7 on 2016/3/3.
 */
public class RouteUtil {
    public static void sendResponse(int code, String desc, long netId) {
        Auth.SResponse.Builder sb = Auth.SResponse.newBuilder();
        sb.setCode(code);
        sb.setDesc(desc);

        ByteBuf byteBuf = Utils.pack2Server(sb.build(), 1002, netId, Internal.Dest.Client);
        AuthServerHandler.getGateAuthConnection().writeAndFlush(byteBuf);
    }
}
