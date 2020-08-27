package ToolsClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import DatabaseOperate.DatabaseLinkSetting;
import net.sf.json.JSONObject;
/**
 * �����ݿ�������ݽ���
 * @author Xinhai Cao
 *
 */
public class DataOperate {
	public DataOperate(){
		
	}
	/**��¼��Ϣƥ��*/
	public String Login(String uid,String password) throws SQLException{
		Connection con = null;
		String returnData = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM t_user WHERE uid=?");
			ps.setString(1, uid);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt("state")==0) {//0��¼�ɹ�
					if(rs.getString("password").equals(password)) {
						returnData = rs.getString(1);
					}else {//1���벻ƥ��
						returnData = "�������";
					}
				}else {//3�˻�������
					returnData = "�˻������ᣡ";
				}
			}else {//2�û�������
				returnData = "�û������ڣ�";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			con.close();
		}
		return returnData;
	}
	
	/**��ȡ�����б�*/
	public Vector<FriendInfo> getFriendInfo(String uid) throws SQLException{
		Vector<FriendInfo> v = new Vector<FriendInfo>();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			ps = con.prepareStatement("SELECT u.uid,u.image,u.netName,u.info FROM t_friend f INNER JOIN t_user u ON u.uid=f.firendID AND f.uid=?");
			ps.setString(1, uid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				FriendInfo f = new FriendInfo();
				f.setUid(rs.getString("uid"));
				f.setNetName(rs.getString("netName"));
				f.setImage(rs.getString("image"));
				f.setInfo(rs.getString("info"));
				v.add(f);
			}
		} catch (SQLException e) {
		}finally {
			con.close();
			ps.close();
		}
		return v;
	}
	
	/**���¸�������*/
	public PersonInfo getPersonInfo(String uid) throws SQLException {
		PersonInfo personInfo = new PersonInfo();
		Connection con = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM t_user WHERE uid=?");
			ps.setString(1, uid);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				personInfo.setUid(rs.getString("uid"));
				personInfo.setNetName(rs.getString("netName"));
				personInfo.setSex(rs.getString("sex"));
				personInfo.setDd(rs.getInt("dd"));
				personInfo.setImage(rs.getString("image"));
				personInfo.setInfo(rs.getString("info"));
				personInfo.setMm(rs.getInt("mm"));
				personInfo.setYy(rs.getInt("yy"));
				personInfo.setName(rs.getString("name"));
				personInfo.setPhoneNumber(rs.getString("phoneNumber"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			con.close();
		}
		return personInfo;
	}
	
	/**ע��*/
	public String regUser(String uid,String password) throws SQLException{
		String str = "";
		Connection con = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM t_user WHERE uid=?");
			ps.setString(1, uid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				str = "�û��Ѵ��ڣ�";
			}
			
			ps = con.prepareStatement("INSERT INTO t_user(uid,password,createTime) VALUES(?,?,NOW())");
			ps.setString(1, uid);
			ps.setString(2, password);
			if(ps.executeUpdate()<=0) {
				throw new SQLException();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			con.close();
		}
		return str;
	}
	
	/**����ע���û�*/
	public Vector<FriendInfo> getAllUserInfo() throws SQLException{
		Vector<FriendInfo> v = new Vector<FriendInfo>();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			ps = con.prepareStatement("SELECT uid,netName,image,info FROM t_user");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				FriendInfo f = new FriendInfo();
				f.setUid(rs.getString("uid"));
				f.setNetName(rs.getString("netName"));
				f.setImage(rs.getString("image"));
				f.setInfo(rs.getString("info"));
				v.add(f);
			}
		} catch (SQLException e) {
		}finally {
			con.close();
			ps.close();
		}
		return v;
	} 
	
	/**�޸ĸ�������*/
	public static void updataPerson(String str) {
		JSONObject jsonO = JSONObject.fromObject(str);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			String sql = "UPDATE t_user SET netName=?,sex=?,phoneNumber=?,info=?,yy=?,mm=?,dd=?,name=?,image=? WHERE uid=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, jsonO.getString("netName"));
			ps.setString(2, jsonO.getString("sex"));
			ps.setString(3, jsonO.getString("phoneNumber"));
			ps.setString(4, jsonO.getString("info"));
			ps.setInt(5, jsonO.getInt("yy"));
			ps.setInt(6, jsonO.getInt("mm"));
			ps.setInt(7, jsonO.getInt("dd"));
			ps.setString(8, jsonO.getString("name"));
			ps.setString(9, jsonO.getString("image"));
			ps.setString(10, jsonO.getString("uid"));
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
		}finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**��ѯ����*/
	public static Vector<TankeSourceInfo> selectSource() {
		Vector<TankeSourceInfo> v = new Vector<TankeSourceInfo>();
		Connection con = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT uid,source,netName FROM t_user ORDER BY source DESC");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				TankeSourceInfo t = new TankeSourceInfo();
				t.setUid(rs.getString("uid"));
				t.setSource(rs.getInt("source"));
				t.setName(rs.getString("netName"));
				v.add(t);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
		return v;
	}
	/**�������ݿ����*/
	public static void updateSource(String uid,int source) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseLinkSetting.getConnection();
			String sql = "UPDATE t_user SET source=? WHERE uid=?";
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, source);
			ps.setString(2, uid);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
		}finally {
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
