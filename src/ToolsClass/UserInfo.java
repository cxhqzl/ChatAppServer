package ToolsClass;

import java.io.*;
import java.net.Socket;
/**
 * 用户信息
 * @author Xinhai Cao
 *
 */
public class UserInfo {
	private String uid;
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private String udpIP;
	private int udpPort;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public String getUdpIP() {
		return udpIP;
	}
	public void setUdpIP(String udpIP) {
		this.udpIP = udpIP;
	}
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	
	
}
