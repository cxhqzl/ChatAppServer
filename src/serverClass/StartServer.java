package serverClass;

import java.text.SimpleDateFormat;
import java.util.Date;

import ToolsClass.DataStorage;

public class StartServer {
	/**�رշ�����*/
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
	/**����������*/
	public void startServer() {
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		//����LoginServer������
		new Thread() {
			public void run() {
				try {
					Date nowTime=new Date(); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t��¼������������"+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					LoginServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//����RegisterServer������
		new Thread() {
			public void run() {
				try {
					Date nowTime=new Date(); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\tע�������������"+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					RegisterServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//����UDPServer������
		new Thread() {
			public void run() {
				try {
					Date nowTime=new Date(); 
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\tUDP������������"+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					UDPServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		// ����TankeLoginServer������
		new Thread() {
			public void run() {
				try {
					Date nowTime = new Date();
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr + "\tTankeLoginServer������������" + "\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					TankeLoginServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//����TankeServer������
		new Thread() {
			public void run() {
				try {
					Date nowTime = new Date();
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr + "\tTankeServer������������" + "\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					TankeServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
		
		//����BulletUDPServer������
		new Thread() {
			public void run() {
				try {
					Date nowTime = new Date();
					String dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr + "\tBulletUDPServer������������" + "\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					BulletUDPServer.openServer();
				} catch (Exception e) {
				}
			}
		}.start();
	}
}
