package thirdparty.thrift.utils;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

/**
 * Created by Dell on 2016/2/22.
 */
public class DBOperator {
    public static <T extends TBase> T Deserialize(T obj, byte[] data) throws TException {
        (new TDeserializer()).deserialize(obj, data);
        return obj;
    }

    public static byte[] Serialize(TBase obj) throws TException {
        return (new TSerializer()).serialize(obj);
    }
}
