package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.BusinessBlueSteelSkin;
import org.jvnet.substance.skin.SubstanceCremeLookAndFeel;
import org.jvnet.substance.theme.SubstanceAquaTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

import ToolsClass.DataStorage;
import ToolsClass.WindowsXY;
import serverClass.StartServer;

public class Start extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea textArea;
	JButton button;

	public Start() {
		this.setSize(700, 500);
		this.setLocation(WindowsXY.getXY(this.getWidth(), this.getHeight()));//窗口居中打开
		getContentPane().setLayout(null);
		
		button = new JButton("启动服务");
		button.setBounds(277, 29, 113, 40);
		getContentPane().add(button);
		button.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 88, 654, 352);
		getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		DataStorage.textArea = textArea;
		scrollPane.setViewportView(textArea);
		textArea.setSelectionStart(textArea.getText().toString().length());
		textArea.setEditable(false);
		textArea.setSelectionEnd(textArea.getText().toString().length());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel(new SubstanceCremeLookAndFeel());
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            SubstanceLookAndFeel.setCurrentTheme(new SubstanceAquaTheme());
            SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());
            SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
            SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
            SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
        } catch (Exception e) {
            
        }
		Start s = new Start();
		s.requestFocus();
	}

	StartServer server = null;
	SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		this.requestFocus();
		if(e.getActionCommand().equals("启动服务")) {
			server = new StartServer();
			server.startServer();
			button.setText("关闭服务");
		}else if(e.getActionCommand().equals("关闭服务")) {
			Date nowTime=new Date(); 
			String dateStr = time.format(nowTime);
			DataStorage.textArea.append(dateStr+"\t服务已关闭！"+"\n");
			DataStorage.textArea.setSelectionEnd(DataStorage.textArea.getText().toString().length());
			server.ClosServer();
			button.setText("启动服务");
		}
	}
}
