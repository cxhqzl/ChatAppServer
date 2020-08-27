package ToolsClass;

import java.awt.Point;
import java.awt.Toolkit;

public class WindowsXY {
	
	public WindowsXY() {
		
	}
	/**
	 * 使窗口居屏幕中央打开
	 * @param w 窗口长度
	 * @param h 窗口高度
	 * @return 返回Point
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
