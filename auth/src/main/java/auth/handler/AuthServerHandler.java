package auth.handler;

/**
 * Created by Dell on 2016/2/18.
 */

import auth.starter.AuthStarter;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.Utils;
import protobuf.generate.cli2srv.login.Auth;
import redis.clients.jedis.Jedis;
import thirdparty.redis.utils.UserUtils;
import thirdparty.thrift.generate.db.user.Account;
import thirdparty.thrift.utils.DBOperator;

public class AuthServerHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(AuthServerHandler.class);
    private static ChannelHandlerContext _gateAuthConnection;

    public static ChannelHandlerContext getGateAuthConnection() {
        return _gateAuthConnection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _gateAuthConnection = ctx;
        logger.info("[Gate-Auth] connection is established");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if(message instanceof Auth.CRegister)
            dealWithRegistry((Auth.CRegister)message);
        else if(message instanceof Auth.CLogin)
            dealWithAuthCmd((Auth.CLogin)message);
    }

    void dealWithRegistry(Auth.CRegister msg) throws TException {
        //long userkey = Common.generateUserId();
        String userid = msg.getUserid();
        Account account = new Account();
        account.setUserid(userid);
        account.setPasswd(msg.getPasswd());

        //todo 写数据库要加锁
        Jedis jedis = AuthStarter._redisPoolManager.getJedis();
        jedis.hset(UserUtils.genDBKey(userid), UserUtils.userFileds.Account.field, DBOperator.Serialize(account));
    }

    void dealWithAuthCmd(Auth.CLogin msg) throws TException {
        Jedis jedis = AuthStarter._redisPoolManager.getJedis();
        String userId = msg.getUserid();
        Account account = null;
        byte[] userIdBytes = jedis.hget(UserUtils.genDBKey(userId), UserUtils.userFileds.Account.field);
        if(userIdBytes == null) {
            sendResponse(404, "Account is not registered");
        } else {
            account = DBOperator.Deserialize(new Account(), userIdBytes);
        }

        if(account.getUserid().equals(msg.getUserid()) && account.getPasswd().equals(msg.getPasswd()))
            sendResponse(200, "Verify passed");
        else
            sendResponse(404, "Account not exist or passwd error");
    }

    void sendResponse(int code, String desc) {
        Auth.SResponse.Builder sb = Auth.SResponse.newBuilder();
        sb.setCode(code);
        sb.setDesc(desc);

        Utils.packAndSend(sb.build(), getGateAuthConnection());
    }
}
