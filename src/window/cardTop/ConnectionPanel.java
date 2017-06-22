package window.cardTop;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import communication.console.TcpConnectionListener;
import communication.udp.ServerUpdateListener;
import data.communication.ServerData;
import window.main.OpeLockListener;
import window.main.OperationLock;

public class ConnectionPanel extends JPanel implements ServerUpdateListener,OpeLockListener,ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel label_connect = new JLabel("IDLE");
	private JButton btn_connect = new JButton("Connect");

	private int title_font_size = 16;
	
	private OperationLock ope_lock;
	private boolean can_connect = false;
	
	private TcpConnectionListener listener;
	
	public ConnectionPanel(OperationLock ope_lock) {
		this.ope_lock = ope_lock;
		
		this.setLayout(new GridLayout(2,1));
		this.setOpaque(false);
		
		label_connect.setHorizontalAlignment(JLabel.CENTER);
		label_connect.setFont(new Font("", Font.BOLD, title_font_size));
		label_connect.setBackground(Color.GRAY);
		label_connect.setOpaque(true);
		
		btn_connect.setEnabled(false);
		btn_connect.setFont(new Font("", Font.BOLD, title_font_size));
		
		this.add(label_connect);
		this.add(btn_connect);
		btn_connect.addActionListener(this);
	}
	
	public void set_TcpConnectionListener(TcpConnectionListener l){
		this.listener = l;
	}


	@Override
	public void server_update(ServerData state) {
		if( !label_connect.getText().equals("CONNECTED")){
			label_connect.setBackground(Color.RED);
			label_connect.setText("Disconnected");
		}
		if( ! ope_lock.is_lock() ){
			btn_connect.setEnabled(true);
		}
		can_connect = true;
	}

	@Override
	public void lock_button() {
		btn_connect.setEnabled(false);
	}

	@Override
	public void unlock_button() {
		if( can_connect == true ){
			btn_connect.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( btn_connect.getText().equals("Connect") ){
			if( listener.connection() ){
				label_connect.setText("CONNECTED");
				label_connect.setBackground(Color.GREEN);
				btn_connect.setText("DIS-connect");
			}else{
				JLabel label = new JLabel("Connection Failed");
				JOptionPane.showMessageDialog(this, label);
			}
		}else{
			listener.close();
			label_connect.setText("Disconnected");
			label_connect.setBackground(Color.RED);
			btn_connect.setText("Connect");
		}
	}
}
