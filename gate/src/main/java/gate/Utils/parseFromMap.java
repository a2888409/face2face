package gate.Utils;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Qzy on 2016/1/29.
 */
public class parseFromMap {
    @FunctionalInterface
    public interface Parsing{
        Message  process(InputStream in) throws IOException;
    }

    private static final Logger logger = LoggerFactory.getLogger(parseFromMap.class);

    public static HashMap<Integer, parseFromMap.Parsing> parseMap = new HashMap<Integer, Parsing>();

    public static void register(int ptoNum, parseFromMap.Parsing cla) {
        if (parseMap.get(ptoNum) == null)
            parseMap.put(ptoNum, cla);
        else {
            logger.error("pto has been registered, ptoNum={}", ptoNum);
            return;
        }
    }

    public static Message parse(int ptoNum, InputStream in) throws IOException {
        Parsing parser = (Parsing) parseMap.get(ptoNum);
        if(parser == null) {
            logger.error("UnKnown pto Num:{}", ptoNum);
        }

        return parser.process(in);
    }

}
