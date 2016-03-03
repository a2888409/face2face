package auth.handler;

import auth.IMHandler;
import auth.Worker;
import auth.starter.AuthStarter;
import auth.utils.RouteUtil;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.Utils;
import protobuf.generate.cli2srv.login.Auth;
import protobuf.generate.internal.Internal;
import redis.clients.jedis.Jedis;
import thirdparty.redis.utils.UserUtils;
import thirdparty.thrift.generate.db.user.Account;
import thirdparty.thrift.utils.DBOperator;

/**
 * Created by win7 on 2016/3/2.
 */
public class CRegisterHandler extends IMHandler {
    private static final Logger logger = LoggerFactory.getLogger(CRegisterHandler.class);

    public CRegisterHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) {
        Auth.CRegister msg = (Auth.CRegister)_msg;

        String userid = msg.getUserid();
        String passwd = msg.getPasswd();
        Account account = new Account();
        account.setUserid(userid);
        account.setPasswd(passwd);

        //todo 写数据库要加锁
        Jedis jedis = AuthStarter._redisPoolManager.getJedis();

        if (!jedis.exists(UserUtils.genDBKey(userid))) {
            RouteUtil.sendResponse(404, "Account already exists", _netid);
            logger.info("Account already exists, userid: {}", userid);
            return;
        } else {
            //todo 异常处理？？
            //jedis.hset(UserUtils.genDBKey(userid), UserUtils.userFileds.Account.field, DBOperator.Serialize(account));
            RouteUtil.sendResponse(400, "User registerd successd",_netid);
            logger.info("User registerd successd, userid: {}", userid);
        }
    }

}


