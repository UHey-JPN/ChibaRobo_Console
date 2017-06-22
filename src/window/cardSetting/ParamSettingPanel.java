package window.cardSetting;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.SettingManager;
import window.main.OpeLockListener;

public class ParamSettingPanel extends JPanel implements ActionListener, OpeLockListener{
	private static final long serialVersionUID = 1L;
	
	private JPanel center_panel = new JPanel();
	private JButton update_button = new JButton("Update");
	
	public SettingCurrentPanel setting_current_panel;
	public SettingNewPanel setting_new_panel;
	
	public ParamSettingPanel(SettingManager sm){
		setting_current_panel = new SettingCurrentPanel(sm);
		setting_new_panel = new SettingNewPanel(sm);
		
		// setup this panel
		this.setOpaque(false);
		this.setBorder(new LineBorder(Color.BLACK));
		this.setLayout( new GridBagLayout() );
		// 1st components
		this.add(setting_current_panel, get_gbc(0,0,1,1, 1,1));
		// 2nd components
		this.add(center_panel, get_gbc(1,0,1,1, 0,1));
		// 2nd components
		this.add(setting_new_panel, get_gbc(2,0,1,1, 1,1));
		
		setup_center_panel();

		update_button.setEnabled(false);
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
	
	private void setup_center_panel(){
		JLabel arrow = new JLabel(new ImageIcon("./resource/arrow.png"));
		arrow.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		update_button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		update_button.addActionListener(this);

		JPanel center_sub = new JPanel();
		center_sub.setOpaque(false);
		center_sub.setLayout(new BoxLayout(center_sub, BoxLayout.Y_AXIS));
		center_sub.add(arrow);
		center_sub.add(update_button);
		
		center_sub.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		
		center_panel.setOpaque(false);
		center_panel.setLayout(new BoxLayout(center_panel, BoxLayout.X_AXIS));
		center_panel.add(center_sub);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == update_button){
			setting_new_panel.save_data();
			setting_current_panel.update_data();
			JFrame f = new JFrame();
			JLabel label = new JLabel("You have to restart this program, if you want to reflect a change.");
			JOptionPane.showMessageDialog(f, label);
		}
	}
	
	@Override
	public void lock_button() {
		update_button.setEnabled(false);
	}
	@Override
	public void unlock_button() {
		update_button.setEnabled(true);
	}
}
