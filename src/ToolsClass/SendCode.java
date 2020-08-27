package ToolsClass;

import org.apache.commons.mail.HtmlEmail;

public class SendCode {
	public SendCode() {
		
	}
	/**
	 * 发送验证码
	 * @param emailaddress 接受方邮箱
	 * @param code 验证码
	 * @return 返回是否发送成功
	 */
	public static boolean sendEmail(String emailaddress, String code) {

		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.163.com");
			email.setCharset("UTF-8");
			email.addTo(emailaddress);// 收件地址

			email.setFrom("18555502667@163.com", "大海的聊天室");

			email.setAuthentication("18555502667@163.com", "caoxinhai0227");

			email.setSubject("大海的聊天室");
			email.setMsg("您的注册账号验证码是:" + code);

			email.send();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
