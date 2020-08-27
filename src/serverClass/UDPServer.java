package serverClass;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ToolsClass.DataStorage;
import ToolsClass.UserInfo;
import ToolsClass.UserOnlineList;
import net.sf.json.JSONObject;

/**
 * UDP服务器
 * @author Xinhai Cao
 *
 */
public class UDPServer implements Runnable {

	private DatagramPacket packet = null;
	public UDPServer(DatagramPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		try {
			String jsonData = new String(packet.getData(),0,packet.getLength());
			JSONObject json = JSONObject.fromObject(jsonData);
			
			String myUid = json.getString("myUid");
			String toUid = json.getString("toUid");
			UserOnlineList.getUserOnlineList().updateOnlineUDP(myUid, packet.getAddress().getHostAddress(), packet.getPort());
			System.out.println( packet.getAddress().getHostAddress());
			UserInfo toUserInfo = UserOnlineList.getUserOnlineList().getOnlineUserInfo(toUid);
			//封装待转发的数据包
			DatagramPacket datagramPacket = new DatagramPacket(packet.getData(),packet.getLength(),InetAddress.getByName(toUserInfo.getUdpIP()),toUserInfo.getUdpPort());
			//发送
			datagramSocket.send(datagramPacket);
			System.out.println(new String(datagramPacket.getData(),0,datagramPacket.getData().length));
			
		}catch(Exception e) {
			
		}
		
		
	}
	
	private static DatagramSocket datagramSocket = null;
	/**启动
	 * @throws SocketException */
	public static void openServer() throws Exception {
		datagramSocket = new DatagramSocket(8887);
		DataStorage.udpServer = datagramSocket;
		//线程池
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		
		while(true) {
			//等待客户端发送数据
			byte[] bytes = new byte[1024*10];
			DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length);
			datagramSocket.receive(datagramPacket);
			try {
				//打印相关信息
				JSONObject jsonObject = JSONObject.fromObject(new String(datagramPacket.getData(),0,datagramPacket.getData().length));
				if(jsonObject.getString("type").equals("request")) {
					String myUid = jsonObject.getString("myUid");
					String toUid = jsonObject.getString("toUid");
					Date nowTime=new Date(); 
					SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t"+myUid+"请求添加"+toUid+"为好友\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
				}else if(jsonObject.getString("type").equals("message")){
					String myUid = jsonObject.getString("myUid");
					String toUid = jsonObject.getString("toUid");
					String message = jsonObject.getString("msg");
					Date nowTime=new Date(); 
					SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t"+myUid+"发送给"+toUid+"："+message+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
				}
			}catch(Exception e) {}
			//交给线程处理
			execute.execute(new UDPServer(datagramPacket));
		}
	}

}
