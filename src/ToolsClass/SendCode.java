package ToolsClass;

import org.apache.commons.mail.HtmlEmail;

public class SendCode {
	public SendCode() {
		
	}
	/**
	 * ������֤��
	 * @param emailaddress ���ܷ�����
	 * @param code ��֤��
	 * @return �����Ƿ��ͳɹ�
	 */
	public static boolean sendEmail(String emailaddress, String code) {

		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.163.com");
			email.setCharset("UTF-8");
			email.addTo(emailaddress);// �ռ���ַ

			email.setFrom("18555502667@163.com", "�󺣵�������");

			email.setAuthentication("18555502667@163.com", "caoxinhai0227");

			email.setSubject("�󺣵�������");
			email.setMsg("����ע���˺���֤����:" + code);

			email.send();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
