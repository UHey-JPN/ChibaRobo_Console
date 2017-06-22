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
import communication.console.SetWinnerListener;

public class SetWinnerButton extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private int side;
	private JButton btn;
	private SetWinnerListener listener;
	
	ArrayList<SetModeListener> listener_list = new ArrayList<SetModeListener>();

	
	public SetWinnerButton(int side){
		this.side = side;
		
		this.btn = new JButton("WIN");
		this.btn.setFont(new Font("", Font.PLAIN, 20));
		
		this.btn.addActionListener(this);
		
		this.setOpaque(false);
		
		this.setBorder(new EmptyBorder(30, 5, 5, 5));
		this.setLayout(new BorderLayout());
		this.add(btn);
	}
	
	public void set_SetWinnerListener(SetWinnerListener l){
		listener = l;
	}
	
	public void add_SetModeListener(SetModeListener l){
		listener_list.add(l);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(SetModeListener l : listener_list){
			l.set_mode("interview");
		}
		if(listener != null)
			listener.set_winner(side);
	}

}
