package ToolsClass;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
/**
 * 在线用户
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
	
	/**保存在线用户,String为用户编号*/
	private HashMap<String,UserInfo> hashMap = new HashMap<String,UserInfo>();
	
	/**注册在线用户*/
	public void registerOnline(String uid,Socket socket) {
		UserInfo userInfo = hashMap.get(uid);
		if(userInfo!=null) {
			try {
				try {
					userInfo.getSocket().getOutputStream().write(4);
				} catch (Exception e) {
				}
				//强制关闭已在线用户
				userInfo.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//开始在线登记
		userInfo = new UserInfo();
		userInfo.setUid(uid);
		userInfo.setSocket(socket);
		hashMap.put(uid, userInfo);
	}
	/**
	 * 更新UDP信息
	 * @param uid 用户编号
	 * @param ip UDP ip
	 * @param port UDP端口
	 */
	public void updateOnlineUDP(String uid,String ip,int port) throws NullPointerException {
		UserInfo userInfo = hashMap.get(uid);
		userInfo.setUdpIP(ip);
		userInfo.setUdpPort(port);
	}
	/**判断用户是否在线*/
	public boolean isUserOnline(String uid) {
		return hashMap.containsKey(uid);
	}
	/**获取在线用户信息*/
	public UserInfo getOnlineUserInfo(String uid) {
		return hashMap.get(uid);
	}
	/**下线*/
	public void logOut(String uid) {
		hashMap.remove(uid);
	}
	/**
	 * 获取所有在线用户
	 * @return
	 */
	public Set<String> getAllUserInfo(){
		//只返回在线用户的id
		return hashMap.keySet();
	}
}
