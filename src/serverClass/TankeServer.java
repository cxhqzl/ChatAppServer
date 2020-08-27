package serverClass;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

import ToolsClass.DataOperate;
import ToolsClass.DataStorage;
import ToolsClass.TankeInfo;
/**
 * 坦克服务器
 * @author Xinhai Cao
 *
 */
public class TankeServer extends Thread {

	private DatagramPacket data = null;
	/**存储坦克信息*/
	public static HashMap<String,TankeInfo> infos = new HashMap<String,TankeInfo>();
	
	public TankeServer(DatagramPacket data) {
		this.data = data;
	}
	
	@SuppressWarnings({ "null" })
	public void run() {
		
		String command = new String(data.getData()).trim();
		if(command.startsWith("LIFE")) {
			String[] s = command.split(",");
			Object info = infos.get(s[1]);
			if(info!=null) {
				TankeInfo info1 = (TankeInfo) info;
				info1.setLife(info1.getLife()-5);
			}
			return;
		}
		String[] str = command.split(",");
		TankeInfo info = infos.get(str[0]);
		if(info == null) {
			TankeInfo info1 = new TankeInfo();
			info1.setUsername(str[0]);
			info1.setX(str[1]);
			info1.setY(str[2]);
			info1.setFangxiang(str[3]);
			info1.setLife(Integer.parseInt(str[4]));
			info1.setSource(Integer.parseInt(str[5]));
			info1.setId(Integer.parseInt(str[6]));
			info1.setIP(data.getAddress().getHostAddress());
			info1.setPort(data.getPort());
			DataOperate.updateSource(info1.getUsername(), info1.getSource());
			infos.put(str[0], info1);
		}else {
			info.setX(str[1]);
			info.setY(str[2]);
			info.setFangxiang(str[3]);
			info.setLife(Integer.parseInt(str[4]));
			info.setSource(Integer.parseInt(str[5]));
			info.setId(Integer.parseInt(str[6]));
			DataOperate.updateSource(info.getUsername(), info.getSource());
		}
		
	}
	static DatagramSocket server = null;
	public static void openServer() throws Exception {
		server = new DatagramSocket(8886);
		DataStorage.tankeServer = server;
		new TankeUDPServer().start();
		while(true) {
			byte[] b = new byte[1000];
			DatagramPacket data = new DatagramPacket(b,b.length);
			server.receive(data);
			new TankeServer(data).start();
		}
	}
}
