package window.cardOperation;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.SettingManager;
import window.cardTop.CardTop;

public class ShowOpePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public ShowModeButton b_home = new ShowModeButton("Home", "home");
	public ShowModeButton b_newgame = new ShowModeButton("New Game", "newgame");
	public ShowModeButton b_game = new ShowModeButton("Game Start", "game");
	public ShowModeButton b_result = new ShowModeButton("Result", "result");
	public ShowModeButton b_interview = new ShowModeButton("V Goal", "vgoal");
	
	public ShowModeButton b_left = new ShowModeButton("Left Interview", "l_interview");
	public ShowModeButton b_right = new ShowModeButton("Right Interview", "r_interview");

	public ShowModeButton b_preshow = new ShowModeButton("Pre Show", "preshow");
	public ShowModeButton b_opening = new ShowModeButton("Opening", "opening");

	public ShowModeButton[] b_show = {
			new ShowModeButton("show 0", "show0"),
			new ShowModeButton("show 1", "show1"),
			new ShowModeButton("show 2", "show2")
	};

	
	public ShowOpePanel(SettingManager sm) {
		for(int i = 0; i < b_show.length; i++){
			b_show[i] = new ShowModeButton(sm.get_user_show_name()[i], "show"+i);
		}
		
		// setup this panel
		this.setOpaque(false);
		
		// this panel border
		TitledBorder title = new TitledBorder("Set SHOW MODE");
		title.setTitleFont(new Font("", 0, CardTop.TITLE_SIZE));
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(0,15,15,15), title );
		this.setBorder(outside);
		
		this.setLayout(new GridLayout(4,3));
		this.add(b_game);
		this.add(b_result);
		this.add(b_interview);
		
		this.add(b_newgame);
		this.add(b_left);
		this.add(b_right);
		
		this.add(b_home);
		this.add(b_preshow);
		this.add(b_opening);
		
		this.add(b_show[0]);
		this.add(b_show[1]);
		this.add(b_show[2]);
	}

}
