package protobuf;

import java.io.IOException;
import protobuf.GenerateCode.cli2srv.login.*;
import protobuf.analysis.ParseMap;

/**
 * Created by Qzy on 2016/1/30.
 */
public class ParseRegistryMap {
    public static void initRegistry() throws IOException {
        ParseMap.register(1000, Auth.CLogin::parseFrom);
    }
}
