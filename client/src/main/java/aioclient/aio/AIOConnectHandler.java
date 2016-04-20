package aioclient.aio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdparty.threedes.ThreeDES;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOConnectHandler implements CompletionHandler {

	/**
	 * 日志
	 */
	private final Logger Log = LoggerFactory.getLogger(getClass());

	private String userId;

	private String pwd;

	private ThreeDES des = new ThreeDES();

	public AIOConnectHandler(String userId, String pwd) {
		this.userId = userId;
		this.pwd = pwd;
	}

	public void startRead(AsynchronousSocketChannel socket) {
		try {
			ByteBuffer clientBuffer = ByteBuffer.allocate(10 * 1024);
			socket.read(clientBuffer, clientBuffer, new AIOReadHandler(socket, userId, des));
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
	}

	@Override
	public void completed(Object result, Object attachment) {
		try {
			AsynchronousSocketChannel socket = (AsynchronousSocketChannel) attachment;

			AIORouterManager.routeRegistRequest(des, socket, userId, pwd);
			AIORouterManager.routeLoginRequest(des, socket, userId, pwd);

			startRead(socket);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
	}

	@Override
	public void failed(Throwable exc, Object attachment) {
		Log.error(exc.getMessage(), exc);
	}

}
