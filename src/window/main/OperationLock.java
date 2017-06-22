package window.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OperationLock extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private boolean lock = true;
	private JButton btn;

	private ArrayList<OpeLockListener> list_listener = new ArrayList<OpeLockListener>();
	
	public OperationLock() {
		btn = new JButton("OPERATION ONLY");
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setMargin( new Insets(5, 5, 5, 5) );
		btn.addActionListener(this);
		
		this.setMaximumSize(new Dimension(500,50));
		this.setLayout(new BorderLayout());
		this.add(btn, BorderLayout.CENTER);
	}
	
	public boolean is_lock(){
		return lock;
	}
	
	
	public void add_OpeLockListener(OpeLockListener l){
		list_listener.add(l);
	}
	
	public void call_listener_lock(){
		for(OpeLockListener l : list_listener){
			l.lock_button();
		}
	}

	public void call_listener_unlock(){
		for(OpeLockListener l : list_listener){
			l.unlock_button();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if( lock ){
			lock = false;
			btn.setText("CAN SETUP");
			call_listener_unlock();
		}else{
			lock = true;
			btn.setText("OPERATION ONLY");
			call_listener_lock();
		}
	}
}
