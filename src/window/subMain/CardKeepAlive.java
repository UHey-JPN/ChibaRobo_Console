package window.subMain;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import window.main.WindowMain;

public class CardKeepAlive extends JPanel {
	private static final long serialVersionUID = 1L;

	public CardKeepAlive() {
		this.setBackground(WindowMain.BACK_COLOR);
		this.setOpaque(true);
		
		this.setLayout(new BorderLayout());
		this.add( new JLabel("CardKeepAlive") );
	}
}
