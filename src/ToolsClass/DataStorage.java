package ToolsClass;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.HashMap;

import javax.swing.JTextArea;

/**
 * ������������ݼĴ�
 * @author Xinhai Cao
 *
 */
public class DataStorage {
	/**��ʾ��*/
	public static JTextArea textArea = null;
	/**udp������*/
	public static DatagramSocket udpServer = null;
	/**��¼������*/
	public static ServerSocket loginServer = null;
	/**ע�������*/
	public static ServerSocket registerServer = null;
	/**̹�˵�¼������*/
	public static ServerSocket tankeLoginServer = null;
	/**̹�˷�����*/
	public static DatagramSocket tankeServer = null;
	/**�ӵ�������*/
	public static DatagramSocket bulletServer = null;
	/**����*/
	HashMap<String,Integer> sources = new HashMap<String,Integer>();
	
}
