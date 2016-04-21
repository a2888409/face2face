package aioclient.aio;


import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.generate.cli2srv.login.Auth;
import thirdparty.threedes.ThreeDES;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

/**
 * AIO消息发送管理器
 * 
 * @author jacklin
 * 
 */
public class AIORouterManager {

	/**
	 * 日志
	 */
	private static final Logger Log = LoggerFactory
			.getLogger(AIORouterManager.class);

	/**
	 * 发送注册消息
	 *
	 * @param des
	 * @param socket
	 * @param userId
	 * @param token
	 */
	public static void routeRegistRequest(ThreeDES des,
										 AsynchronousSocketChannel socket, String userId, String token) {

		Auth.CRegister.Builder cb = Auth.CRegister.newBuilder();
		cb.setUserid(userId);
		cb.setPasswd(token);

		routeDirect(des, socket, cb.build(), 1001);
	}

	/**
	 * 发送登录消息
	 * 
	 * @param des
	 * @param socket
	 * @param userId
	 * @param token
	 */
	public static void routeLoginRequest(ThreeDES des,
			AsynchronousSocketChannel socket, String userId, String token) {

		Auth.CLogin.Builder loginInfo = Auth.CLogin.newBuilder();
		loginInfo.setUserid(userId);
		loginInfo.setPasswd(token);
		loginInfo.setPlatform("ios");
		loginInfo.setAppVersion("1.0.0");

		routeDirect(des, socket, loginInfo.build(), 1000);
	}

	/**
	 * 直接发送消息
	 * 
	 * @param socket
	 * @param msgSend
	 */
	public static void routeDirect(ThreeDES des,
			AsynchronousSocketChannel socket, Message msgSend, int ptoNum) {
		try {
			byte[] bareByte = msgSend.toByteArray();

			int length = bareByte.length;

			ByteBuffer buf = ByteBuffer.allocate(8 + length);
			buf.putInt(length);
			buf.putInt(ptoNum);
			buf.put(bareByte);
			buf.flip();
			Future<Integer> future = socket.write(buf);
			while (!future.isDone());


			Log.info("[SEND][total length:" + length + "][bare length:"
					+ msgSend.getSerializedSize() + "]:\r\n"
					+ msgSend.getClass());

		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
	}

}
