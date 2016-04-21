package auth.handler;

import auth.IMHandler;
import auth.Worker;
import auth.utils.Common;
import auth.utils.RouteUtil;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.generate.cli2srv.login.Auth;
import thirdparty.redis.utils.UserUtils;
import thirdparty.thrift.generate.db.user.Account;
import thirdparty.thrift.utils.DBOperator;

/**
 * Created by win7 on 2016/3/3.
 */
public class CLoginHandler extends IMHandler {
    private static final Logger logger = LoggerFactory.getLogger(CLoginHandler.class);

    public CLoginHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) throws TException {
        Auth.CLogin msg = (Auth.CLogin)_msg;
        Account account;

        if(!_jedis.exists(UserUtils.genDBKey(_userid))) {
            RouteUtil.sendResponse(Common.ACCOUNT_INEXIST, "Account not exists", _netid, _userid);
            logger.info("Account not exists, userid: {}", _userid);
            return;
        } else {
            byte[] userIdBytes = _jedis.hget(UserUtils.genDBKey(_userid), UserUtils.userFileds.Account.field);
            account = DBOperator.Deserialize(new Account(), userIdBytes);
        }

        if(account.getUserid().equals(_userid) && account.getPasswd().equals(msg.getPasswd())) {
            AuthServerHandler.putInUseridMap(_userid, _netid);
            RouteUtil.sendResponse(Common.VERYFY_PASSED, "Verify passed", _netid, _userid);
            logger.info("userid: {} verify passed", _userid);
        } else {
            RouteUtil.sendResponse(Common.VERYFY_ERROR, "Account not exist or passwd error", _netid, _userid);
            logger.info("userid: {} verify failed", _userid);
            return;
        }
    }
}
