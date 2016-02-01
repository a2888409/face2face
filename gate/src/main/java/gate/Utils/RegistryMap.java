package gate.Utils;

import java.io.IOException;
import protobuf.GenerateCode.cli2srv.login.*;
/**
 * Created by Qzy on 2016/1/30.
 */
public class RegistryMap {
    public static void initRegistry() throws IOException {
        parseFromMap.register(1000, Auth.CLogin::parseFrom);
    }
}
