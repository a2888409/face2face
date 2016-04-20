package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.ParseRegistryMap;
import protobuf.code.PacketDecoder;
import protobuf.code.PacketEncoder;

/**
 * Created by Dell on 2016/2/15.
 * Simple client for module test
 */
public class Client {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "9090"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws Exception {

//        for(int i = 0; i < 1; i++) {
//            new Thread(() -> {
//                try {
//                    startConnect();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
        startConnect();
    }

    public static void startConnect() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast("MessageDecoder", new PacketDecoder());
                            p.addLast("MessageEncoder", new PacketEncoder());
                            p.addLast(new ClientHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT)
                    .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future)
                        throws Exception {
                    if (future.isSuccess()) {
                        //init registry
                        ParseRegistryMap.initRegistry();
                        logger.info("Client connected Gate Successed...");
                    } else {
                        logger.error("Client connected Gate Failed");
                    }
                }
            });

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }
}

