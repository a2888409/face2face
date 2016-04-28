package aioclient.aio;


import aioclient.AIOClient;
import client.Common;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.analysis.ParseMap;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.cli2srv.login.Auth;
import thirdparty.threedes.ThreeDES;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicInteger;

public class AIOReadHandler implements CompletionHandler {

	/**
	 * 日志
	 */
	private final Logger logger = LoggerFactory.getLogger(AIOReadHandler.class);

	// public JsonProcessorAIO jsonProcessorAIO = new JsonProcessorAIO();

	private AsynchronousSocketChannel socket;

	private ThreeDES des;

	private String userId;

	public AIOReadHandler(AsynchronousSocketChannel socket, String userId,
			ThreeDES des) {
		this.socket = socket;
		this.userId = userId;
		this.des = des;
	}

	public static AtomicInteger count = new AtomicInteger(1);

	private boolean _verify = false;
	public void cancelled(ByteBuffer attachment) {
		logger.error("cancelled");
	}

	@Override
	public void completed(Object result, Object attachment) {
		try {
			Integer i = (Integer) result;
			ByteBuffer buf = (ByteBuffer) attachment;

			if (i > 0) {
				buf.flip();
				try {
					// String msg = decoder.decode(buf).toString();
					// jsonProcessorAIO.handleMsg(socket, msg);
					buf.mark();
					// 读取传送过来的消息的长度。
					int length = buf.getInt();

					// 长度如果小于0
					if (length < 0) {// 非法数据，关闭连接
						return;
					}

					if (length > buf.remaining()) {// 读到的消息体长度如果小于传送过来的消息长度
						// 重置读取位置
						buf.reset();
						return;
					}

					int ptoNum = buf.getInt();
					byte[] msgByte = new byte[length];
					buf.get(msgByte);
					buf.compact();

					try {
						//byte[] bareByte = des.decrypt(msgByte);

						Message msg = ParseMap.getMessage(ptoNum, msgByte);

						logger.info("[RECV][total length:" + msgByte.length
								+ "][bare length:"
								+ msg.getSerializedSize() + "]:\r\n"
								+ msg.toString());

						if (msg != null) {
							handleMsg(msg);
						}
						// }
					} catch (Exception ex) {
						logger.error(
								socket.getRemoteAddress() + "decode Failure.",
								ex);
					}

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				socket.read(buf, buf, this);
			} else if (i == -1) {
				// 服务器断开连接
				logger.error("对端断线:" + socket.getRemoteAddress().toString());
				buf = null;

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * 处理消息
	 * 
	 * @param msg
	 */
	private void handleMsg(Message msg) throws InterruptedException {
		logger.info("received message: {}", msg.getClass());
		if(msg instanceof Auth.SResponse) {
			Auth.SResponse sp = (Auth.SResponse) msg;
			int code = sp.getCode();
			String desc = sp.getDesc();
			switch (code) {
				//登录成功
				case Common.VERYFY_PASSED:
					logger.info("Login succeed, description: {}", desc);
					_verify = true;
					break;
				//登录账号不存在
				case Common.ACCOUNT_INEXIST:
					logger.info("Account inexsit, description: {}", desc);
					break;
				//登录账号或密码错误
				case Common.VERYFY_ERROR:
					logger.info("Account or passwd Error, description: {}", desc);
					break;
				//账号已被注册
				case Common.ACCOUNT_DUMPLICATED:
					logger.info("Dumplicated registry, description: {}", desc);
					break;
				//注册成功
				case Common.REGISTER_OK:
					logger.info("User registerd successd, description: {}", desc);
					break;
				case Common.Msg_SendSuccess:
					logger.info("Chat Message Send Successed, description: {}", desc);
				default:
					logger.info("Unknow code: {}", code);
			}
		} else if(msg instanceof Chat.SPrivateChat) {
			logger.info("{} receiced chat message: {}.Total:{}", userId, ((Chat.SPrivateChat) msg).getContent(), count.getAndIncrement());
		}

		if(_verify) {
			sendMessage();
			Thread.sleep(AIOClient.frequency);
		}
	}

	void sendMessage() {
		String content = "Hello, I am Tom!";

		Chat.CPrivateChat.Builder cp = Chat.CPrivateChat.newBuilder();
		cp.setContent(content);
		cp.setSelf(userId);
		cp.setDest(userId);

		AIORouterManager.routeDirect(des, socket, cp.build(), 1003);
	}
	@Override
	public void failed(Throwable e, Object attachment) {
		logger.error(e.getMessage(), e);
		// 服务器断开连接
		// StatsManager.USER_ONLINE.getAndDecrement();

	}
}
