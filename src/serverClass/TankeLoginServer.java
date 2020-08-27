package serverClass;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import ToolsClass.DataOperate;
import ToolsClass.DataStorage;
import ToolsClass.TankeSourceInfo;
import net.sf.json.JSONArray;

public class TankeLoginServer extends Thread {
	
	private Socket socket = null;
	
	public TankeLoginServer(Socket socket) {
		this.socket = socket;
	}
	
	/**�洢�����û�*/
	public static HashMap<String,String> users = new HashMap<String,String>();
	
	int i = 0;
	public void run() {
		String name = "";
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			byte[] b = new byte[1024*10];
			in.read(b);
			name = new String(b).trim();
			boolean isCon = false;
			//��ֹͬһ�û�ͬʱ��¼
			synchronized (users) {
				if(users.get(name)!=null) {
					out.write("�û��Ѵ��ڣ�".getBytes());
					out.flush();
				}else {
					Vector<TankeSourceInfo> data = DataOperate.selectSource();
					String str = JSONArray.fromObject(data).toString();
					out.write(str.getBytes());
					out.flush();
					in.read(b);
					users.put(name, socket.getInetAddress().getHostAddress());
					i = users.size();
					out.write(String.valueOf(i).getBytes());
					out.flush();
					isCon = true;
				}
			}
			
			while(isCon) {
				in.read();
			}
			
		} catch (Exception e) {
			users.remove(name);
			TankeServer.infos.remove(name);
		}
	}
	
	public static void openServer() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(8886);
		DataStorage.tankeLoginServer = server;
		while(true) {
			new TankeLoginServer(server.accept()).start();
		}
	}
}
