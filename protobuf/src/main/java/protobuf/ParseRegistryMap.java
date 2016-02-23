package protobuf;

import protobuf.analysis.ParseMap;
import protobuf.generate.cli2srv.login.Auth;

import java.io.IOException;

/**
 * Created by Qzy on 2016/1/30.
 */
public class ParseRegistryMap {
    public static void initRegistry() throws IOException {
        ParseMap.register(1000, Auth.CLogin::parseFrom, Auth.CLogin.class);
        ParseMap.register(1001, Auth.CRegister::parseFrom, Auth.CRegister.class);
        ParseMap.register(1002, Auth.SResponse::parseFrom, Auth.SResponse.class);
    }
}
