package gate.Utils;

import io.netty.util.AttributeKey;
import thridparty.ThreeDES.ThreeDES;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ClientAttr {
    public static AttributeKey<ThreeDES> ENCRYPT = AttributeKey.valueOf("encrypt");

}
