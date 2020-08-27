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
 * 注册服务器
 * @author Xinhai Cao
 *
 */
public class RegisterServer implements Runnable {
	
	private Socket socket;
	
	public RegisterServer(Socket socket) {
		this.socket = socket;
		
	}
	
	/**验证码池*/
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
			if(type.equals("code")) {//验证码请求
				try {
					String username = json.getString("username");
					Random random = new Random();
					StringBuffer code = new StringBuffer();
					for(int i=0;i<6;i++) {
						code.append(random.nextInt(10));
					}
					hashMap.put(username, code.toString());
					SendCode.sendEmail(username, code.toString());
					out.write("{\"state\":0,\"msg\":\"验证码发送成功！\"}".getBytes());
					out.flush();
				}catch(Exception e) {
					out.write("{\"state\":1,\"msg\":\"验证码发送失败！\"}".getBytes());
					out.flush();
				}
			}else if(type.equals("reg")) {//注册请求
				String username = json.getString("username");
				String password = json.getString("password");
				String code = json.getString("code");
				String code1=hashMap.get(username);
				
				try {
					if(code1.equals(code)){
						try {
							String str = new DataOperate().regUser(username, password);
							if(str.equals("用户已存在！")) {
								out.write("{\"state\":1,\"msg\":\"用户已存在！\"}".getBytes());
								out.flush();
								return;
							}else if(str.equals("")) {
								out.write("{\"state\":0,\"msg\":\"注册成功！\"}".getBytes());
								Date nowTime=new Date(); 
								SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
								String dateStr = time.format(nowTime);
								DataStorage.textArea.append(dateStr+"\t新用户注册："+username+"\n");
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
							hashMap.remove(username);//注册成功后移除验证码
						}
					}else{
						out.write("{\"state\":1,\"msg\":\"验证码错误,请重新获取!\"}".getBytes());
						out.flush();
						return;
					}
				}catch(NullPointerException e){
					out.write("{\"state\":1,\"msg\":\"验证码错误,请重新获取!\"}".getBytes());
					out.flush();
					return;
				}
			}//注册请求结束
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
