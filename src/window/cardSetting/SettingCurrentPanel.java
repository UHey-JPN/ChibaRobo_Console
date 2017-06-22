package window.cardSetting;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import main.SettingManager;
import window.cardTop.CardTop;

public class SettingCurrentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private SettingManager sm;
	
	JLabel[][] label = {
			{ new JLabel("number of team"), new JLabel() },
			{ new JLabel("point list"), new JLabel() },
			{ new JLabel("user show name"), new JLabel() },
			{ new JLabel("robots file"), new JLabel() },
			{ new JLabel("teams file"), new JLabel() },
			{ new JLabel("tournament file"), new JLabel() },
	};

	SettingCurrentPanel(SettingManager sm){
		this.sm = sm;
		
		// setup this panel
		this.setOpaque(false);
		
		// label setup
		update_data();
		
		// this panel border
		TitledBorder title = new TitledBorder("Current Setting");
		title.setTitleFont(new Font("", 0, CardTop.TITLE_SIZE));
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(10,15,15,15), title );
		this.setBorder(outside);

		this.setLayout(new GridBagLayout());
		for(int i = 0; i < label.length; i++){
			for(int j = 0; j < label[i].length; j++){
				label[i][j].setHorizontalAlignment(JLabel.CENTER);
				label[i][j].setFont(new Font("", Font.PLAIN, 15));
			}

			CompoundBorder bdr = new CompoundBorder(new EmptyBorder(5,5,5,5), new LineBorder(Color.BLACK) );
			label[i][1].setBorder(bdr);
			
			this.add(label[i][0], get_gbc(0,i,1,1, 0.5,1));
			this.add(label[i][1], get_gbc(1,i,1,1, 1,1));
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
	
	void update_data(){
		label[0][1].setText(String.valueOf(sm.get_num_of_teams()));
		
		String str_point_list = "" + sm.get_point_list()[0];
		for(int i = 1; i < sm.get_point_list().length; i++){
			str_point_list += "," + sm.get_point_list()[i];
		}
		label[1][1].setText(str_point_list);
		
		String str_user_show = "" + sm.get_user_show_name()[0];
		for(int i = 1; i < sm.get_user_show_name().length; i++){
			str_user_show += "," + sm.get_user_show_name()[i];
		}
		label[2][1].setText(str_user_show);
		
		label[3][1].setText(String.valueOf(sm.get_robots_file_name()));
		label[4][1].setText(String.valueOf(sm.get_teams_file_name()));
		label[5][1].setText(String.valueOf(sm.get_tnmt_file_name()));
		
	}
	
}
