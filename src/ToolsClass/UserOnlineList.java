package ToolsClass;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
/**
 * �����û�
 * @author Xinhai Cao
 *
 */
public class UserOnlineList {
	
	private UserOnlineList() {
		
	}
	private static UserOnlineList userOnlineList = new UserOnlineList();
	
	public static UserOnlineList getUserOnlineList() {
		return userOnlineList;
	}
	
	/**���������û�,StringΪ�û����*/
	private HashMap<String,UserInfo> hashMap = new HashMap<String,UserInfo>();
	
	/**ע�������û�*/
	public void registerOnline(String uid,Socket socket) {
		UserInfo userInfo = hashMap.get(uid);
		if(userInfo!=null) {
			try {
				try {
					userInfo.getSocket().getOutputStream().write(4);
				} catch (Exception e) {
				}
				//ǿ�ƹر��������û�
				userInfo.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//��ʼ���ߵǼ�
		userInfo = new UserInfo();
		userInfo.setUid(uid);
		userInfo.setSocket(socket);
		hashMap.put(uid, userInfo);
	}
	/**
	 * ����UDP��Ϣ
	 * @param uid �û����
	 * @param ip UDP ip
	 * @param port UDP�˿�
	 */
	public void updateOnlineUDP(String uid,String ip,int port) throws NullPointerException {
		UserInfo userInfo = hashMap.get(uid);
		userInfo.setUdpIP(ip);
		userInfo.setUdpPort(port);
	}
	/**�ж��û��Ƿ�����*/
	public boolean isUserOnline(String uid) {
		return hashMap.containsKey(uid);
	}
	/**��ȡ�����û���Ϣ*/
	public UserInfo getOnlineUserInfo(String uid) {
		return hashMap.get(uid);
	}
	/**����*/
	public void logOut(String uid) {
		hashMap.remove(uid);
	}
	/**
	 * ��ȡ���������û�
	 * @return
	 */
	public Set<String> getAllUserInfo(){
		//ֻ���������û���id
		return hashMap.keySet();
	}
}
