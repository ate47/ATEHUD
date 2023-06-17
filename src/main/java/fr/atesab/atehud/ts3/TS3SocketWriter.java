package fr.atesab.atehud.ts3;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TS3SocketWriter extends Thread {

	private final BlockingQueue<String> sendQueue;
	private final BlockingQueue<String> receiveQueue;
	private final PrintStream out;
	private volatile long lastCommand = System.currentTimeMillis();

	public TS3SocketWriter() throws UnsupportedEncodingException, IOException {
		sendQueue = TS3Socket.getSendQueue();
		receiveQueue = TS3Socket.getReceiveQueue();
		this.out = new PrintStream(TS3Socket.getSocket().getOutputStream(), true, "UTF-8");
	}

	long getIdleTime() {
		return System.currentTimeMillis() - lastCommand;
	}

	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				final String msg = sendQueue.take();

				receiveQueue.put(msg);
				out.println(msg);

				lastCommand = System.currentTimeMillis();
			}
		} catch (final InterruptedException e) {
			interrupt();
		}

		out.close();
	}
}
