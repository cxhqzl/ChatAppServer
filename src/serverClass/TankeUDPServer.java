package serverClass;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import ToolsClass.TankeInfo;

public class TankeUDPServer extends Thread {
	
	
	public TankeUDPServer() {
		
	}
	
	public void run() {
		while(true) {
			@SuppressWarnings("unchecked")
			HashMap<String,TankeInfo> map = (HashMap<String, TankeInfo>) TankeServer.infos.clone();
			String data = "";
			//坦克信息存储格式：name,x,y,fangxiang,life,source&name,x,y,fangxiang,life,source....
			Set<Entry<String,TankeInfo>> sets = map.entrySet();
			for(Entry<String,TankeInfo> entry : sets) {
//				if(entry.getValue().getLife()>0) {
//					data+=(entry.getKey()+","+entry.getValue().getX()+","+entry.getValue().getY()+","+entry.getValue().getFangxiang()+","+entry.getValue().getLife()+"&");
//				}
				data+=(entry.getKey()+","+entry.getValue().getX()+","+entry.getValue().getY()+","+entry.getValue().getFangxiang()+","+entry.getValue().getLife()+","+entry.getValue().getSource()+","+entry.getValue().getId()+"&");

			}
			
			byte[] b = data.getBytes();
			for(Entry<String,TankeInfo> entry : sets) {
				try {
					DatagramPacket data1 = new DatagramPacket(b,b.length,InetAddress.getByName(entry.getValue().getIP()),entry.getValue().getPort());
					TankeServer.server.send(data1);
				} catch (IOException e) {
				}
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
