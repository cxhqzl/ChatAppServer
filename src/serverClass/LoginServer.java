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
 * 登录服务器
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
		//响应登录操作
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
			if(returnData.equals("密码错误！")||returnData.equals("账户被冻结！")||returnData.equals("用户不存在！")) {
				out.write(("{\"state\":0,\"msg\":\""+returnData+"\"}").getBytes());
				out.flush();
				return;
			}
			out.write(("{\"state\":0,\"msg\":\""+returnData+"\"}").getBytes());
			out.flush();
			//登录成功后登记
			UserOnlineList.getUserOnlineList().registerOnline(uid, socket);
			Date nowTime=new Date(); 
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String dateStr = time.format(nowTime);
			DataStorage.textArea.append(dateStr+"\t用户："+uid+"已登录\n");
			DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
			//
			while(true) {
				bytes = new byte[2048];
				try {
					length = in.read(bytes);
				}catch(Exception e) {
					
				}
				
				String command = new String(bytes,0,length);
				if(command.equals("Q01")) {//更新好友列表请求
					Vector<FriendInfo> friendInfo = new DataOperate().getFriendInfo(uid);
					out.write(JSONArray.fromObject(friendInfo).toString().getBytes());
					out.flush();
				}else if(command.equals("Q02")) {//更新好友在线状态请求
					out.write(1);
					out.flush();
					//获取好友的列表编号
					try {
						length = in.read(bytes);
					}catch(Exception e) {
						
					}
					String str = new String (bytes,0,length);
					String[] ids = str.split(",");
					StringBuffer onlineFriend = new StringBuffer();
					for(String str1 : ids) {
						if(UserOnlineList.getUserOnlineList().isUserOnline(str1)) {//账户在线
							onlineFriend.append(str1);//在线用户添加进去
							onlineFriend.append(",");
						}
					}
					//保存所有在线用户
					nowTime=new Date(); 
					time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					dateStr = time.format(nowTime);
					DataStorage.textArea.append(dateStr+"\t当前在线用户："+UserOnlineList.getUserOnlineList().getAllUserInfo().toString()+"\n");
					DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
					//返回给客户端
					if(onlineFriend.length()==0) {
						try {
							out.write("暂无在线好友!".getBytes());
							out.flush();
						}catch(Exception e) {}
					}else {
						out.write(onlineFriend.toString().getBytes());
						out.flush();
					}
					
				}else if(command.equals("Q03")) {//更新个人资料请求
					PersonInfo personInfo = new DataOperate().getPersonInfo(uid);
					out.write(JSONObject.fromObject(personInfo).toString().getBytes());
					out.flush();
				}else if(command.equals("Q04")) {//修改个人资料请求
					out.write(1);
					out.flush();
					length = in.read(bytes); 
					String str = new String (bytes,0,length);
					System.out.println("更改资料请求"+str);
					DataOperate.updataPerson(str);
					out.write(1);
					out.flush();
				}else if(command.equals("Q05")) {//更新已注册用户请求
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
				DataStorage.textArea.append(dateStr+"\t用户："+uid+"已下线\n");
				DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
				in.close();
				out.close();
				socket.close();
			} catch (Exception e) {
				
			}
		}
	}
	public static void openServer() throws Exception {
		//线程池，限制登录人数
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		//开启TCP  端口8888
		ServerSocket server = new ServerSocket(8888);
		DataStorage.loginServer = server;
		while(true) {
			Socket socket = server.accept();
			//设置响应时间
			socket.setSoTimeout(10000);
			execute.execute(new LoginServer(socket));
		}
	}
}
