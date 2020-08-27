package serverClass;

import java.text.SimpleDateFormat;
import java.util.Date;

import ToolsClass.DataStorage;

public class StartServer {
	/**关闭服务器*/
	public void ClosServer() {
		try {
			DataStorage.loginServer.close();
			DataStorage.registerServer.close();
			DataStorage.udpServer.close();
			DataStorage.bulletServer.close();
			DataStorage.tankeLoginServer.close();
			DataStorage.tankeServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**启动服务器*/
	public void startServer() {
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		//启动LoginServer服务器
		new Thread() {
			public void run() {
				try {
					Date nowTime=new Date(); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t登录服务已启动！"+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					LoginServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//启动RegisterServer服务器
		new Thread() {
			public void run() {
				try {
					Date nowTime=new Date(); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t注册服务已启动！"+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					RegisterServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//启动UDPServer服务器
		new Thread() {
			public void run() {
				try {
					Date nowTime=new Date(); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\tUDP服务已启动！"+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					UDPServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		// 启动TankeLoginServer服务器
		new Thread() {
			public void run() {
				try {
					Date nowTime = new Date();
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr + "\tTankeLoginServer服务已启动！" + "\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					TankeLoginServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//启动TankeServer服务器
		new Thread() {
			public void run() {
				try {
					Date nowTime = new Date();
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr + "\tTankeServer服务已启动！" + "\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					TankeServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//启动BulletUDPServer服务器
		new Thread() {
			public void run() {
				try {
					Date nowTime = new Date();
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr + "\tBulletUDPServer服务已启动！" + "\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					BulletUDPServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
	}
}
