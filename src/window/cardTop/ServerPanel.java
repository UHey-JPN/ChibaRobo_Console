package window.cardTop;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import communication.udp.ServerUpdateListener;
import data.communication.ServerData;
import window.main.OperationLock;

public class ServerPanel extends JPanel implements ServerUpdateListener{
	private static final long serialVersionUID = 1L;
	
	private JLabel label_ip = new JLabel("127.0.0.1");
	
	private JPanel panel_port = new JPanel();
	private JLabel[][] label_port = new JLabel[2][3];

	public ConnectionPanel connection_panel;

	private int title_font_size = 16;
	
	
	public ServerPanel(OperationLock ope_lock){
		// connect button
		connection_panel = new ConnectionPanel(ope_lock);
		
		// setup this panel
		this.setOpaque(false);
		
		// this panel border
		TitledBorder title = new TitledBorder("Server Status");
		title.setTitleFont(new Font("", 0, CardTop.TITLE_SIZE));
		CompoundBorder border = new CompoundBorder( title, new EmptyBorder(10,15,10,15) );
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(15,15,15,15), border );
		
		this.setBorder(outside);
		
		// setup components
		setup_panel_port();
		setup_label_ip();
		
		this.setLayout(new GridLayout(1,3));
		this.add(label_ip);
		this.add(panel_port);
		this.add(connection_panel);
	}
	
	private void setup_label_ip(){
		label_ip.setHorizontalAlignment(JLabel.CENTER);
		TitledBorder title = new TitledBorder("Server IP");
		title.setTitleFont(new Font("", 0, title_font_size));
		title.setTitleJustification(TitledBorder.CENTER);
		label_ip.setBorder(title);
	}
	
	private void setup_panel_port(){
		panel_port.setLayout(new GridLayout(3,2));
		panel_port.setOpaque(false);
		label_port[0][0] = new JLabel("Console");
		label_port[0][1] = new JLabel("Database");
		label_port[0][2] = new JLabel("KeepAlive");
		label_port[1][0] = new JLabel("0");
		label_port[1][1] = new JLabel("0");
		label_port[1][2] = new JLabel("0");
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 2; j++){
				label_port[j][i].setBorder( new EmptyBorder(5,5,5,5) );
				panel_port.add(label_port[j][i]);
			}
		}
		
		TitledBorder title = new TitledBorder("Server Port");
		title.setTitleFont(new Font("", 0, title_font_size));
		title.setTitleJustification(TitledBorder.CENTER);
		panel_port.setBorder(title);
	}
	
	@Override
	public void server_update(ServerData state) {
		label_ip.setText(state.get_server_ip());
		label_port[1][0].setText(String.valueOf(state.get_console_port()));
		label_port[1][1].setText(String.valueOf(state.get_db_port()));
		label_port[1][2].setText(String.valueOf(state.get_kam_port()));
	}
}

