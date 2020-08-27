package ToolsClass;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.HashMap;

import javax.swing.JTextArea;

/**
 * 服务器相关数据寄存
 * @author Xinhai Cao
 *
 */
public class DataStorage {
	/**显示器*/
	public static JTextArea textArea = null;
	/**udp服务器*/
	public static DatagramSocket udpServer = null;
	/**登录服务器*/
	public static ServerSocket loginServer = null;
	/**注册服务器*/
	public static ServerSocket registerServer = null;
	/**坦克登录服务器*/
	public static ServerSocket tankeLoginServer = null;
	/**坦克服务器*/
	public static DatagramSocket tankeServer = null;
	/**子弹服务器*/
	public static DatagramSocket bulletServer = null;
	/**分数*/
	HashMap<String,Integer> sources = new HashMap<String,Integer>();
	
}
