package protobuf.analysis;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Qzy on 2016/1/29.
 */
public class ParseMap {
    @FunctionalInterface
    public interface Parsing{
        Message  process(byte[] bytes) throws IOException;
    }

    private static final Logger logger = LoggerFactory.getLogger(ParseMap.class);

    public static HashMap<Integer, ParseMap.Parsing> parseMap = new HashMap<Integer, Parsing>();

    public static void register(int ptoNum, ParseMap.Parsing cla) {
        if (parseMap.get(ptoNum) == null)
            parseMap.put(ptoNum, cla);
        else {
            logger.error("pto has been registered, ptoNum: {}", ptoNum);
            return;
        }
    }

    public static Message parse(int ptoNum, byte[] bytes) throws IOException {
        Parsing parser = parseMap.get(ptoNum);
        if(parser == null) {
            logger.error("UnKnown Protocol Num: {}", ptoNum);
        }

        return parser.process(bytes);
    }

}
