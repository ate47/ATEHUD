package fr.atesab.atehud.ts3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.output.WriterOutputStream;

import fr.atesab.atehud.ModMain;

public class TS3Socket {
	private static Socket socket;
	private static TS3SocketReader socketReader;
	private static TS3SocketWriter socketWriter;
	private final static BlockingQueue<String> sendQueue = new LinkedBlockingQueue<String>();
	private final static BlockingQueue<String> receiveQueue = new LinkedBlockingQueue<String>();
	public static int defineClientListPhase = 0, schandlerid = 0, currentCID = 0, currentCLID = 0;
	public static long lastDefineClient = (long) 0;
	public static boolean needReDefineGroups = true;
	public static ArrayList<TS3Client> allClients = new ArrayList<TS3Client>();
	public static ArrayList<TS3Channel> allChannel = new ArrayList<TS3Channel>();
	public static ArrayList<TS3ServerGroup> allServerGroup = new ArrayList<TS3ServerGroup>();
	public static ArrayList<TS3ChannelGroup> allChannelGroup = new ArrayList<TS3ChannelGroup>();
	public static ArrayList<TS3Client> clients = new ArrayList<TS3Client>();
	public static TS3Channel currentChannel = new TS3Channel();
	public static TS3Client currentClient = new TS3Client();

	public static boolean closeSocket() {
		try {
			socket.close();
			defineClientListPhase = 0;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String compileText(String txt) {
		return txt
				.replaceAll(" ", "\\\\s")
				.replaceAll("[\\\\]", "\\\\")
				.replaceAll("[|]", "\\\\p")
				.replaceAll("\r", "\\\\r")
				.replaceAll("[\\\\]r", "\\\\r")
				.replaceAll("[\\\\]n", "\\\\n")
				.replaceAll("\n", "\\\\n");
	}

	public static ArrayList<Map<String, String>> createMap(String line) {
		ArrayList<Map<String, String>> map = new ArrayList<Map<String, String>>();
		String[] part = line.split("\\|");
		for (int i = 0; i < part.length; i++) {
			String[] args = part[i].split(" ");
			HashMap<String, String> hmap = new HashMap<String, String>();
			for (int j = 0; j < args.length; j++) {
				String[] elem = args[j].split("=");
				if (elem.length == 1) { // no value
					hmap.put(elem[0], null);
				} else if (elem.length == 2) {
					hmap.put(elem[0], decompileText(elem[1]));
				} // error
				map.add(hmap);
			}
		}
		return map;
	}

	public static String decompileText(String txt) {
		return txt.replaceAll("[\\\\]n", "\n")
				.replaceAll("[\\\\][\\\\]", "\\\\")
				.replaceAll("[\\\\]p", "\\|")
				.replaceAll("[\\\\]s", " ")
				.replaceAll("[\\\\]r", "\r");
	}

	public static BlockingQueue<String> getReceiveQueue() {
		return receiveQueue;
	}

	public static BlockingQueue<String> getSendQueue() {
		return sendQueue;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static boolean openSocket() {
		try {
			System.out.println(
					"TS3> OpenSocket [" + ModMain.ts3ClientQueryAdress + ", " + ModMain.ts3ClientQueryPort + "]");
			socket = new Socket(ModMain.ts3ClientQueryAdress, ModMain.ts3ClientQueryPort);
			socket.setTcpNoDelay(true);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			socketReader = new TS3SocketReader();
			socketWriter = new TS3SocketWriter();
			sendCommand("auth apikey="+ModMain.ts3ClientApiKey);
			sendCommand("clientnotifyregister schandlerid=0 event=any");
			socketReader.start();
			socketWriter.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void sendCommand(String cmd) throws IOException, NullPointerException {
		if (socket != null) {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(cmd);
			if (ModMain.TS3LogQuery)
				System.out.println("TS3cmd< " + cmd);
		}
	}
}
