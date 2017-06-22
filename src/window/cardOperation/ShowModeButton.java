package window.cardOperation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import communication.console.SetModeListener;

public class ShowModeButton extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	ArrayList<SetModeListener> listener_list = new ArrayList<SetModeListener>();
	
	private String mode;
	
	private JButton btn;

	public ShowModeButton(String mode_panel, String mode) {
		this.mode = mode;
		btn = new JButton(mode_panel);
		btn.setFont(new Font("", Font.PLAIN, 20));
		btn.addActionListener(this);
		
		this.setOpaque(false);
		this.setBorder(new EmptyBorder(5,5,5,5));
		this.setLayout(new BorderLayout());
		this.add(btn);
	}

	public ShowModeButton(String mode) {
		this(mode, mode);
	}
	
	public void add_SetModeListener(SetModeListener l){
		listener_list.add(l);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(SetModeListener l : listener_list){
			l.set_mode(mode);
		}
	}

}
