package ToolsClass;
/**
 * 坦克信息
 * @author Xinhai Cao
 *
 */
public class TankeInfo {
	/**用户名*/
	private String username;
	/**x位置*/
	private String x;
	/**y位置*/
	private String y;
	/**坦克方向*/
	private String fangxiang;
	/**坦克生命值*/
	private int life;
	/**分数*/
	private int source;
	/**编号*/
	private int id;
	/**IP*/
	private String IP;
	/**端口*/
	private int port;
	
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getFangxiang() {
		return fangxiang;
	}
	public void setFangxiang(String fangxiang) {
		this.fangxiang = fangxiang;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
