package window.cardTournament;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import window.main.WindowMain;
import window.subMain.SubMain;

public class CardTournament extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JPanel main_panel = new JPanel();

	public TournamentViewer main_view;
	public UpdateTournamentButton update_button = new UpdateTournamentButton();

	
	public CardTournament() {
		main_view = new TournamentViewer();
		
		this.setBackground(WindowMain.BACK_COLOR);
		this.setOpaque(true);
		
		// display panel name 
		JLabel title_label = new JLabel("Tournament");
		title_label.setHorizontalAlignment(JLabel.CENTER);
		title_label.setFont( new Font("", Font.BOLD, SubMain.TITLE_SIZE) );
		title_label.setBorder(new LineBorder(Color.BLACK));
		title_label.setBackground(Color.BLACK);
		title_label.setForeground(Color.WHITE);
		title_label.setOpaque(true);
		
		// setup main panel
		main_panel.setOpaque(false);
		main_panel.setBorder(new LineBorder(Color.BLACK));
		main_panel.setLayout( new BorderLayout() );
		main_panel.add(main_view);
		
		// setup TOP card
		this.setLayout(new BorderLayout());
		this.add( title_label, BorderLayout.NORTH );
		this.add( main_panel, BorderLayout.CENTER );
		this.add( update_button , BorderLayout.SOUTH);
	}
	
	public UpdateTourViewListener get_UpdateTourViewListener(){
		return main_view;
	}
}

