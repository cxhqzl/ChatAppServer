package serverClass;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import ToolsClass.BulletInfo;
import ToolsClass.DataStorage;

public class BulletUDPServer extends Thread {
	DatagramPacket data;
	public BulletUDPServer(DatagramPacket data) {
		this.data = data;
	}
	
	/**存放所有子弹信息*/
	static HashMap<String,BulletInfo> bulletInfos = new HashMap<String,BulletInfo>();
	/**存储在线用户*/
	static HashMap<String,String[]> clients = new HashMap<String,String[]>();
	
	public void run() {
		String str = new String(data.getData(),0,data.getLength());
		String[] s = str.split(",");
		String id = s[0];
		int x = Integer.parseInt(s[1]);
		int y = Integer.parseInt(s[2]);
		String fangxiang = s[3];
		BulletInfo info = bulletInfos.get(id);
		if(info==null) {
			info = new BulletInfo();
			info.setId(id);
			info.setFangxiang(fangxiang);
			info.setX(x);
			info.setY(y);
			info.setIp(data.getAddress().getHostAddress());
			info.setPort(data.getPort());
			bulletInfos.put(id, info);
			clients.put(id, new String[] {info.getIp(),info.getPort()+""});
		}else {
			info.setId(id);
			info.setFangxiang(fangxiang);
			info.setX(x);
			info.setY(y);
			info.setIp(data.getAddress().getHostAddress());
			info.setPort(data.getPort());
			clients.put(id, new String[] {info.getIp(),info.getPort()+""});
		}
	}
	/**
	 * 发送子弹
	 * @author Xinhai Cao
	 *
	 */
	static class SendBullet extends Thread{
		public void run() {
			while(true) {
				try {
					@SuppressWarnings("unchecked")
					HashMap<String,BulletInfo> map = (HashMap<String, BulletInfo>) bulletInfos.clone();
					Set<Entry<String,BulletInfo>> info1 = map.entrySet();
					/**存储子弹位置信息*/
					String d = "";
					for(Entry<String,BulletInfo> entry : info1) {
						try {
							switch(entry.getValue().getFangxiang()) {
							case "W":
								if(entry.getValue().getY()+12<=0) {
									throw new Exception();
								}
								break;
							case "S":
								if(entry.getValue().getY()-12>=527) {
									throw new Exception();
								}
								break;
							case "A":
								if(entry.getValue().getX()-12<=0) {
									throw new Exception();
								}
								break;
							case "D":
								if(entry.getValue().getX()>=582) {
									throw new Exception();
								}
								break;
							}
							
							d+=entry.getKey()+","+entry.getValue().getX()+","+entry.getValue().getY()+","+entry.getValue().getFangxiang()+"&";
							
						} catch (Exception e) {
							bulletInfos.remove(entry.getKey());
						}
						
					}
					byte[] b = d.getBytes();
					Set<Entry<String,String[]>> info2 = clients.entrySet();
					for(Entry<String,String[]> entry1 : info2) {
						try {
							DatagramPacket data1 = new DatagramPacket(b,b.length,InetAddress.getByName(entry1.getValue()[0]),Integer.parseInt(entry1.getValue()[1]));
							server.send(data1);
						} catch (Exception e) {
						}
						
					}
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
				}catch(Exception e) {}
			}
		}
	}
	static DatagramSocket server = null;
	public static void openServer() {
		try {
			new SendBullet().start();
			server = new DatagramSocket(8885);
			DataStorage.bulletServer = server;
			while(true) {
				byte[] b = new byte[1024*100];
				DatagramPacket data = new DatagramPacket(b,b.length);
				server.receive(data);
				new BulletUDPServer(data).start();
			}
		} catch (Exception e) {
		}
	}
}
