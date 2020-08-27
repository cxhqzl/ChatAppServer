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
 * UDP������
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
			//��װ��ת�������ݰ�
			DatagramPacket datagramPacket = new DatagramPacket(packet.getData(),packet.getLength(),InetAddress.getByName(toUserInfo.getUdpIP()),toUserInfo.getUdpPort());
			//����
			datagramSocket.send(datagramPacket);
			System.out.println(new String(datagramPacket.getData(),0,datagramPacket.getData().length));
			
		}catch(Exception e) {
			
		}
		
		
	}
	
	private static DatagramSocket datagramSocket = null;
	/**����
	 * @throws SocketException */
	public static void openServer() throws Exception {
		datagramSocket = new DatagramSocket(8887);
		DataStorage.udpServer = datagramSocket;
		//�̳߳�
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		
		while(true) {
			//�ȴ��ͻ��˷�������
			byte[] bytes = new byte[1024*10];
			DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length);
			datagramSocket.receive(datagramPacket);
			try {
				//��ӡ�����Ϣ
				JSONObject jsonObject = JSONObject.fromObject(new String(datagramPacket.getData(),0,datagramPacket.getData().length));
				if(jsonObject.getString("type").equals("request")) {
					String myUid = jsonObject.getString("myUid");
					String toUid = jsonObject.getString("toUid");
					Date nowTime=new Date(); 
					SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t"+myUid+"�������"+toUid+"Ϊ����\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
				}else if(jsonObject.getString("type").equals("message")){
					String myUid = jsonObject.getString("myUid");
					String toUid = jsonObject.getString("toUid");
					String message = jsonObject.getString("msg");
					Date nowTime=new Date(); 
					SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t"+myUid+"���͸�"+toUid+"��"+message+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
				}
			}catch(Exception e) {}
			//�����̴߳���
			execute.execute(new UDPServer(datagramPacket));
		}
	}

}
