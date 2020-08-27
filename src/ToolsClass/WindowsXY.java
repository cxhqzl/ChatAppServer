package ToolsClass;

import java.awt.Point;
import java.awt.Toolkit;

public class WindowsXY {
	
	public WindowsXY() {
		
	}
	/**
	 * ʹ���ھ���Ļ�����
	 * @param w ���ڳ���
	 * @param h ���ڸ߶�
	 * @return ����Point
	 */
	public static Point getXY(int w,int h) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int width = toolkit.getScreenSize().width;
		int height = toolkit.getScreenSize().height;
		int x = (width-w)/2;
		int y = (height-h)/2;
		Point p = new Point(x,y);
		return p;
	}
}
