package serverClass;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ToolsClass.DataOperate;
import ToolsClass.DataStorage;
import ToolsClass.FriendInfo;
import ToolsClass.PersonInfo;
import ToolsClass.UserOnlineList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ��¼������
 * @author Xinhai Cao
 *
 */
public class LoginServer implements Runnable {
	
	private Socket socket = null;
	
	public LoginServer(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		//��Ӧ��¼����
		InputStream in = null;
		OutputStream out = null;
		String uid = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			byte[] bytes = new byte[1024];
			int length = in.read(bytes);
			String jsonData = new String(bytes,0,length);
			JSONObject json = JSONObject.fromObject(jsonData);
			uid = json.getString("uid");
			String password = json.getString("password");
			String returnData = new DataOperate().Login(uid,password);
			if(returnData.equals("�������")||returnData.equals("�˻������ᣡ")||returnData.equals("�û������ڣ�")) {
				out.write(("{\"state\":0,\"msg\":\""+returnData+"\"}").getBytes());
				out.flush();
				return;
			}
			out.write(("{\"state\":0,\"msg\":\""+returnData+"\"}").getBytes());
			out.flush();
			//��¼�ɹ���Ǽ�
			UserOnlineList.getUserOnlineList().registerOnline(uid, socket);
			Date nowTime=new Date(); 
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String dateStr = time.format(nowTime);
			DataStorage.textArea.append(dateStr+"\t�û���"+uid+"�ѵ�¼\n");
			DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
			//
			while(true) {
				bytes = new byte[2048];
				try {
					length = in.read(bytes);
				}catch(Exception e) {
					
				}
				
				String command = new String(bytes,0,length);
				if(command.equals("Q01")) {//���º����б�����
					Vector<FriendInfo> friendInfo = new DataOperate().getFriendInfo(uid);
					out.write(JSONArray.fromObject(friendInfo).toString().getBytes());
					out.flush();
				}else if(command.equals("Q02")) {//���º�������״̬����
					out.write(1);
					out.flush();
					//��ȡ���ѵ��б���
					try {
						length = in.read(bytes);
					}catch(Exception e) {
						
					}
					String str = new String (bytes,0,length);
					String[] ids = str.split(",");
					StringBuffer onlineFriend = new StringBuffer();
					for(String str1 : ids) {
						if(UserOnlineList.getUserOnlineList().isUserOnline(str1)) {//�˻�����
							onlineFriend.append(str1);//�����û���ӽ�ȥ
							onlineFriend.append(",");
						}
					}
					//�������������û�
					nowTime=new Date(); 
					time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t��ǰ�����û���"+UserOnlineList.getUserOnlineList().getAllUserInfo().toString()+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					//���ظ��ͻ���
					if(onlineFriend.length()==0) {
						try {
							out.write("�������ߺ���!".getBytes());
							out.flush();
						}catch(Exception e) {}
					}else {
						out.write(onlineFriend.toString().getBytes());
						out.flush();
					}
					
				}else if(command.equals("Q03")) {//���¸�����������
					PersonInfo personInfo = new DataOperate().getPersonInfo(uid);
					out.write(JSONObject.fromObject(personInfo).toString().getBytes());
					out.flush();
				}else if(command.equals("Q04")) {//�޸ĸ�����������
					out.write(1);
					out.flush();
					length = in.read(bytes); 
					String str = new String (bytes,0,length);
					System.out.println("������������"+str);
					DataOperate.updataPerson(str);
					out.write(1);
					out.flush();
				}else if(command.equals("Q05")) {//������ע���û�����
					Vector<FriendInfo> allUserInfo = new DataOperate().getAllUserInfo();
					out.write(JSONArray.fromObject(allUserInfo).toString().getBytes());
					out.flush();
				}
			}
		} catch (Exception e) {
			
		} finally {
			try {
				UserOnlineList.getUserOnlineList().logOut(uid);
				Date nowTime=new Date(); 
				SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String dateStr = time.format(nowTime);
				DataStorage.textArea.append(dateStr+"\t�û���"+uid+"������\n");
				DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
				in.close();
				out.close();
				socket.close();
			} catch (Exception e) {
				
			}
		}
	}
	public static void openServer() throws Exception {
		//�̳߳أ����Ƶ�¼����
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		//����TCP  �˿�8888
		ServerSocket server = new ServerSocket(8888);
		DataStorage.loginServer = server;
		while(true) {
			Socket socket = server.accept();
			//������Ӧʱ��
			socket.setSoTimeout(10000);
			execute.execute(new LoginServer(socket));
		}
	}
}
