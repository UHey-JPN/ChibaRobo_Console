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
	
	private enum BTN_LIST{
		UPDATE_ROBOT("Update Robot Data"),
		UPDATE_TEAM("Update Team Data"),
		UPDATE_TOURNAMENT("Update Tournament Data"),
		NO_FUNCTION0("no function"),
		CHECK_INTEGRITY("Confirm the Integrity"),
		RESET_ROBOT("Reset Robot Data"),
		RESET_TEAM("Reset Team Data"),
		NO_FUNCTION1("no function");
		
		private String label;
		
		BTN_LIST(String label){
			this.label = label;
		}
		
		public String get_label(){
			return label;
		}
	}
	private JButton[] btn_list;

	public UpdateDataPanel(){
		for(int i=0; i < BTN_LIST.values().length; i++){
			btn_list[i] = new JButton( BTN_LIST.values()[i].get_label() );
		}
		
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
		if(e.getSource() == btn_list[BTN_LIST.UPDATE_ROBOT.ordinal()]){
			update_data_listener.update_data(UploadDataListener.TYPE_ROBOT);
			
		}else if(e.getSource() == btn_list[BTN_LIST.UPDATE_TEAM.ordinal()]){
			update_data_listener.update_data(UploadDataListener.TYPE_TEAM);
			
		}else if(e.getSource() == btn_list[BTN_LIST.UPDATE_TOURNAMENT.ordinal()]){
			update_data_listener.update_data(UploadDataListener.TYPE_TOURNAMENT);
			
		}else if(e.getSource() == btn_list[BTN_LIST.CHECK_INTEGRITY.ordinal()]){
			if(integrity_listener.check_integrity()){
				show_dialog(btn_list[BTN_LIST.CHECK_INTEGRITY.ordinal()], "There is no problem.");
			}else{
				show_dialog(btn_list[BTN_LIST.CHECK_INTEGRITY.ordinal()], "Data has a problem.");
			}
			
		}else if(e.getSource() == btn_list[BTN_LIST.RESET_ROBOT.ordinal()]){
			clear_listener.clear_data(ClearDataListener.TYPE_ROBOT);
			
		}else if(e.getSource() == btn_list[BTN_LIST.RESET_TEAM.ordinal()]){
			clear_listener.clear_data(ClearDataListener.TYPE_TEAM);
			
		}
	}
	private void show_dialog(Component f, String str){
		JLabel label = new JLabel(str);
		JOptionPane.showMessageDialog(f, label);
	}


}
