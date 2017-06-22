package window.cardSetting;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.SettingManager;
import window.main.WindowMain;
import window.subMain.SubMain;

public class CardSetting extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel main_panel = new JPanel();

	public ParamSettingPanel param_setting_panel;
	public UpdateDataPanel update_data_panel;
	
	public CardSetting(SettingManager sm) {
		this.param_setting_panel = new ParamSettingPanel(sm);
		this.update_data_panel = new UpdateDataPanel();
		
		// setup this JPanel
		this.setBackground(WindowMain.BACK_COLOR);
		this.setOpaque(true);
		
		// display panel name 
		JLabel title_label = new JLabel("Setting");
		title_label.setHorizontalAlignment(JLabel.CENTER);
		title_label.setFont( new Font("", Font.BOLD, SubMain.TITLE_SIZE) );
		title_label.setBorder(new LineBorder(Color.BLACK));
		title_label.setBackground(Color.BLACK);
		title_label.setForeground(Color.WHITE);
		title_label.setOpaque(true);
		
		// setup main panel
		main_panel.setOpaque(false);
		main_panel.setBorder(new LineBorder(Color.BLACK));
		main_panel.setLayout( new GridBagLayout() );
		// 1st components
		main_panel.add(param_setting_panel, get_gbc(0,0,1,1, 1,1));
		// 2nd components
		main_panel.add(update_data_panel, get_gbc(0,1,1,1, 0,1));

		// setup TOP card
		this.setLayout(new BorderLayout());
		this.add( title_label, BorderLayout.NORTH );
		this.add( main_panel, BorderLayout.CENTER );

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
	
}
