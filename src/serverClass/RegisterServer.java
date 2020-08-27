package serverClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ToolsClass.DataOperate;
import ToolsClass.DataStorage;
import ToolsClass.SendCode;
import net.sf.json.JSONObject;
/**
 * ע�������
 * @author Xinhai Cao
 *
 */
public class RegisterServer implements Runnable {
	
	private Socket socket;
	
	public RegisterServer(Socket socket) {
		this.socket = socket;
		
	}
	
	/**��֤���*/
	private static HashMap<String,String> hashMap = new HashMap<String,String>();
	
	@Override
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			byte[] bytes = new byte[1024];
			int length = in.read(bytes);
			String data = new String(bytes,0,length);
			JSONObject json = JSONObject.fromObject(data);
			String type = json.getString("type");
			if(type.equals("code")) {//��֤������
				try {
					String username = json.getString("username");
					Random random = new Random();
					StringBuffer code = new StringBuffer();
					for(int i=0;i<6;i++) {
						code.append(random.nextInt(10));
					}
					hashMap.put(username, code.toString());
					SendCode.sendEmail(username, code.toString());
					out.write("{\"state\":0,\"msg\":\"��֤�뷢�ͳɹ���\"}".getBytes());
					out.flush();
				}catch(Exception e) {
					out.write("{\"state\":1,\"msg\":\"��֤�뷢��ʧ�ܣ�\"}".getBytes());
					out.flush();
				}
			}else if(type.equals("reg")) {//ע������
				String username = json.getString("username");
				String password = json.getString("password");
				String code = json.getString("code");
				String code1=hashMap.get(username);
				
				try {
					if(code1.equals(code)){
						try {
							String str = new DataOperate().regUser(username, password);
							if(str.equals("�û��Ѵ��ڣ�")) {
								out.write("{\"state\":1,\"msg\":\"�û��Ѵ��ڣ�\"}".getBytes());
								out.flush();
								return;
							}else if(str.equals("")) {
								out.write("{\"state\":0,\"msg\":\"ע��ɹ���\"}".getBytes());
								Date nowTime=new Date(); 
								SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
								String dateStr = time.format(nowTime);
								DataStorage.textArea.append(dateStr+"\t���û�ע�᣺"+username+"\n");
								DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
								out.flush();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
							in.close();
							out.close();
						}
						if(code1!=null){
							hashMap.remove(username);//ע��ɹ����Ƴ���֤��
						}
					}else{
						out.write("{\"state\":1,\"msg\":\"��֤�����,�����»�ȡ!\"}".getBytes());
						out.flush();
						return;
					}
				}catch(NullPointerException e){
					out.write("{\"state\":1,\"msg\":\"��֤�����,�����»�ȡ!\"}".getBytes());
					out.flush();
					return;
				}
			}//ע���������
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void openServer() throws Exception {
		ExecutorService service = Executors.newFixedThreadPool(1000);
		ServerSocket server = new ServerSocket(8889);
		DataStorage.registerServer = server;
		while(true) {
			Socket socket = server.accept();
			service.execute(new RegisterServer(socket));
		}
	}
}
