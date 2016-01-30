package gate.Utils;

import cli_generate.Login;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/1/29.
 */
public class parseFromMap {
    @FunctionalInterface
    interface Parsing{
        public void process(byte[] bytes);
    }

    private static final Logger logger = LoggerFactory.getLogger(parseFromMap.class);

    static HashMap<Integer, Parsing> parseFromMap = new HashMap<Integer, Parsing>();

    public static void register(int ptoNum, Parsing cla) {
        if (parseFromMap.get(ptoNum) == null)
            parseFromMap.put(ptoNum, cla);
        else {
            logger.error("pto has been registered, ptoNum={}", ptoNum);
            return;
        }
    }

    public static void parse(int ptoNum, byte[] bytes){
       // parseFromMap.get(ptoNum).process(bytes);
    }

}
