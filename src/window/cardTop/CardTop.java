package window.cardTop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import window.main.OperationLock;
import window.main.WindowMain;
import window.subMain.SubMain;

public class CardTop extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JPanel main_panel = new JPanel();
	
	public static final int TITLE_SIZE = 20;
	
	public ServerPanel server_panel;
	public ShowPanel show_panel;

	public CardTop(OperationLock ope_lock) {
		server_panel = new ServerPanel(ope_lock);
		show_panel = new ShowPanel();
		
		// setup this JPanel
		this.setBackground(WindowMain.BACK_COLOR);
		this.setOpaque(true);
		
		// display panel name 
		JLabel title_label = new JLabel("TOP");
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
		main_panel.add(server_panel, get_gbc(0,0,2,1,1, 0.25));
		// 2nd components
		main_panel.add(show_panel, get_gbc(0,1,1,2, 1,1));
		// 3rd components
		main_panel.add(new ShowPanel(), get_gbc(1,1,1,2, 1,1));
		
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
