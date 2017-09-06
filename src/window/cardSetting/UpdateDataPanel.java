package window.cardSetting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import communication.console.ClearDataListener;
import communication.console.UploadDataListener;
import data.communication.CheckIntegrityListener;
import window.main.OpeLockListener;

public class UpdateDataPanel extends JPanel implements ActionListener, OpeLockListener {
	private static final long serialVersionUID = 1L;
	
	private ClearDataListener clear_listener;
	private CheckIntegrityListener integrity_listener;
	private UploadDataListener update_data_listener;
	
	private JButton[] btn_list = {
			new JButton("Update Robot Data"),
			new JButton("Update Team Data"),
			new JButton("Update Tournament Data"),
			new JButton("Confirm the Integrity"),
			new JButton("Reset Robot Data"),
			new JButton("Reset Team Data"),
			new JButton("no function"),
			new JButton("no function"),
	};

	public UpdateDataPanel(){
		this.setOpaque(false);
		this.setBorder(new LineBorder(Color.BLACK));
		
		this.setLayout(new GridLayout(0,4));
		
		for(int i = 0; i < btn_list.length; i++){
			JPanel panel_list = new JPanel(); // 余白のためのJPanel
			panel_list.setOpaque(false);
			panel_list.setBorder(new EmptyBorder(10,10,10,10));
			panel_list.setLayout(new BorderLayout());
			
			btn_list[i].setFont(new Font("", Font.PLAIN, 20));
			panel_list.add(btn_list[i]);
			this.add(panel_list);
		}
		
		for(int i = 0; i < btn_list.length; i++){
			btn_list[i].addActionListener(this);
			btn_list[i].setEnabled(false);
		}
		
	}
	
	public void set_ClearDataListener(ClearDataListener l){
		this.clear_listener = l;
	}
	public void set_CheckIntegrityListener(CheckIntegrityListener l){
		this.integrity_listener = l;
	}
	public void set_UploadDataListener(UploadDataListener l){
		this.update_data_listener = l;
	}
	
	@Override
	public void lock_button() {
		for(int i = 0; i < btn_list.length; i++){
			btn_list[i].setEnabled(false);
		}
	}
	@Override
	public void unlock_button() {
		for(int i = 0; i < btn_list.length; i++){
			btn_list[i].setEnabled(true);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn_list[0]){
			// update_robot
			update_data_listener.update_data(UploadDataListener.TYPE_ROBOT);
			
		}else if(e.getSource() == btn_list[1]){
			// update_team
			update_data_listener.update_data(UploadDataListener.TYPE_TEAM);
			
		}else if(e.getSource() == btn_list[2]){
			// update_tnmt
			update_data_listener.update_data(UploadDataListener.TYPE_TOURNAMENT);
			
		}else if(e.getSource() == btn_list[3]){
			// confirm a integrity
			if(integrity_listener.check_integrity()){
				show_dialog(btn_list[3], "There is no problem.");
			}else{
				show_dialog(btn_list[3], "Data has a problem.");
			}
			
		}else if(e.getSource() == btn_list[4]){
			// Reset Robot Data
			clear_listener.clear_data(ClearDataListener.TYPE_ROBOT);
			
		}else if(e.getSource() == btn_list[5]){
			// Reset Team Data
			clear_listener.clear_data(ClearDataListener.TYPE_TEAM);
			
		}
	}
	private void show_dialog(Component f, String str){
		JLabel label = new JLabel(str);
		JOptionPane.showMessageDialog(f, label);
	}


}
