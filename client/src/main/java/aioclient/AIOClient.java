package aioclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.ParseRegistryMap;

import java.io.IOException;

/**
 * Created by win7 on 2016/4/20.
 */
public class AIOClient {
    private static final Logger logger = LoggerFactory.getLogger(AIOClient.class);

    public static void main(String[] args) throws IOException {
        //init Registry
        ParseRegistryMap.initRegistry();

        for(int i = 1; i < 5; i++) {
            AIOClientSingle client = new AIOClientSingle();
            logger.info("AIOClient {} has been created", i);
            //new Thread(() -> GateServer.startGateServer(gateListenPort)).start();
            client.createSocket(i);
        }

        try {
            Thread.sleep(1000000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
