package window.cardSetting;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.SettingManager;
import window.cardTop.CardTop;
import window.main.OpeLockListener;

public class SettingNewPanel extends JPanel implements OpeLockListener {
	private static final long serialVersionUID = 1L;
	
	private SettingManager sm;
	
	JLabel[] label = {
			new JLabel("number of team"),
			new JLabel("point list"),
			new JLabel("user show name"),
			new JLabel("robots file"),
			new JLabel("teams file"),
			new JLabel("tournament file")
	};
	
	JTextField[] tfield = {
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JTextField()
	};

	SettingNewPanel(SettingManager sm){
		this.sm = sm;
		
		// setup this panel
		this.setOpaque(false);
		
		// label setup
		update_state();
		
		// this panel border
		TitledBorder title = new TitledBorder("New Setting");
		title.setTitleFont(new Font("", 0, CardTop.TITLE_SIZE));
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(10,15,15,15), title );
		this.setBorder(outside);

		this.setLayout(new GridBagLayout());
		for(int i = 0; i < label.length; i++){
			label[i].setHorizontalAlignment(JLabel.CENTER);
			label[i].setFont(new Font("", Font.PLAIN, 15));
			tfield[i].setHorizontalAlignment(JLabel.CENTER);
			tfield[i].setFont(new Font("", Font.PLAIN, 15));

			
			this.add(label[i], get_gbc(0,i,1,1, 0.5,1));
			this.add(tfield[i], get_gbc(1,i,1,1, 1,1));
		}
		
		for(int i = 0; i < tfield.length; i++){
			tfield[i].setEnabled(false);
		}
		
	}
	private GridBagConstraints get_gbc(int x, int y, int width, int height, double wx, double wy){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.fill = GridBagConstraints.BOTH;
		return gbc;
	}
	
	void update_state(){
		tfield[0].setText(String.valueOf(sm.get_num_of_teams()));
		
		String str_point_list = "" + sm.get_point_list()[0];
		for(int i = 1; i < sm.get_point_list().length; i++){
			str_point_list += "," + sm.get_point_list()[i];
		}
		tfield[1].setText(str_point_list);
		
		String str_user_show = "" + sm.get_user_show_name()[0];
		for(int i = 1; i < sm.get_user_show_name().length; i++){
			str_user_show += "," + sm.get_user_show_name()[i];
		}
		tfield[2].setText(str_user_show);
		
		tfield[3].setText(String.valueOf(sm.get_robots_file_name()));
		tfield[4].setText(String.valueOf(sm.get_teams_file_name()));
		tfield[5].setText(String.valueOf(sm.get_tnmt_file_name()));
	}
	
	void save_data(){
		sm.set_num_of_teams( Integer.parseInt(tfield[0].getText()) );
		
		int[] point_list;
		String[] str_point_list = tfield[1].getText().split(",");
		point_list = new int[str_point_list.length];
		for(int i = 0; i < point_list.length; i++){
			point_list[i] = Integer.parseInt(str_point_list[i]);
		}
		sm.set_point_list(point_list);
		
		String[] user_show_name = tfield[2].getText().split(",");
		if( user_show_name.length == 3 ){
			sm.set_user_show_name(user_show_name);
		}else{
			dialog("user show has to have 3 names");
		}
		
		sm.set_robots_file_name(tfield[3].getText());
		sm.set_teams_file_name(tfield[4].getText());
		sm.set_tnmt_file_name(tfield[5].getText());
	}
	
	void dialog(String mes){
		JFrame f = new JFrame();
		JLabel label = new JLabel(mes);
		JOptionPane.showMessageDialog(f, label);
	}
	@Override
	public void lock_button() {
		for(int i = 0; i < tfield.length; i++){
			tfield[i].setEnabled(false);
		}
	}
	@Override
	public void unlock_button() {
		for(int i = 0; i < tfield.length; i++){
			tfield[i].setEnabled(true);
		}
	}
	
}
