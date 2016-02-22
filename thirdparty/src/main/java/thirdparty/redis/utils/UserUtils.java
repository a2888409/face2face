package thirdparty.redis.utils;

import java.nio.charset.Charset;

/**
 * Created by Dell on 2016/2/22.
 */
public class UserUtils {
    public static enum userFileds {
        LastUserId,
        Account;

        public final byte[] field;

        private userFileds() {this.field = this.name().toLowerCase().getBytes(Charset.forName("UTF-8"));}
    }
}
