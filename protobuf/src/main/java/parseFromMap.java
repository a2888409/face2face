import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/1/29.
 */
public class parseFromMap {
    private static final Logger logger = LoggerFactory.getLogger(parseFromMap.class);

    static HashMap<Integer, Class<?>> parseFromMap = new HashMap<Integer, Class<?>>();

    public static void register(int ptoNum, Class<?> cla) {
        if (parseFromMap.get(ptoNum) == null)
            parseFromMap.put(ptoNum, cla);
        else {
            logger.error("pto has been registered, ptoNum={}", ptoNum);
            return;
        }
    }

    public static void parser(int ptoNum){

    }

}
