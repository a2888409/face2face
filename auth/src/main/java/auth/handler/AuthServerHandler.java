package auth.handler;

/**
 * Created by Dell on 2016/2/18.
 */

import auth.starter.AuthStarter;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.Utils;
import protobuf.analysis.ParseMap;
import protobuf.generate.cli2srv.login.Auth;
import protobuf.generate.internal.Internal;
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
        Internal.GTransfer gt = (Internal.GTransfer) message;
        long netId = gt.getNetId();
        Message msg = ParseMap.getMessage(gt.getPtoNum(), gt.getMsg().toByteArray());

        if(msg instanceof Auth.CRegister)
            dealWithRegistry((Auth.CRegister)msg, netId);
        else if(msg instanceof Auth.CLogin)
            dealWithAuthCmd((Auth.CLogin)msg, netId);
    }

    void dealWithRegistry(Auth.CRegister msg, long netId) throws TException {
        //long userkey = Common.generateUserId();
        String userid = msg.getUserid();
        String passwd = msg.getPasswd();
        Account account = new Account();
        account.setUserid(userid);
        account.setPasswd(passwd);

        //todo 写数据库要加锁
        Jedis jedis = AuthStarter._redisPoolManager.getJedis();

        if (!jedis.exists(UserUtils.genDBKey(userid))) {
            sendResponse(404, "Account already exists", netId);
            logger.info("Account already exists, userid: {}", userid);
            return;
        } else {
            jedis.hset(UserUtils.genDBKey(userid), UserUtils.userFileds.Account.field, DBOperator.Serialize(account));
            sendResponse(400, "User registerd successd", netId);
            logger.info("User registerd successd, userid: {}", userid);
        }
    }

    void dealWithAuthCmd(Auth.CLogin msg, long netId) throws TException {
        Jedis jedis = AuthStarter._redisPoolManager.getJedis();
        String userId = msg.getUserid();
        Account account;

        if(!jedis.exists(UserUtils.genDBKey(userId))) {
            sendResponse(404, "Account not exists", netId);
            logger.info("Account not exists, userid: {}", userId);
            return;
        } else {
            byte[] userIdBytes = jedis.hget(UserUtils.genDBKey(userId), UserUtils.userFileds.Account.field);
            account = DBOperator.Deserialize(new Account(), userIdBytes);
        }

        if(account.getUserid().equals(msg.getUserid()) && account.getPasswd().equals(msg.getPasswd())) {
            sendResponse(200, "Verify passed", netId);
            logger.info("userid: {} verify passed", userId);
        } else {
            sendResponse(404, "Account not exist or passwd error", netId);
            logger.info("userid: {} verify failed", userId);
        }
    }

    void sendResponse(int code, String desc, long netId) {
        Auth.SResponse.Builder sb = Auth.SResponse.newBuilder();
        sb.setCode(code);
        sb.setDesc(desc);

        ByteBuf byteBuf = Utils.pack2Server(sb.build(), 1002, netId, Internal.Dest.Client);
        getGateAuthConnection().writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // super.exceptionCaught(ctx, cause);
        logger.error("An Exception Caught");
    }
}
