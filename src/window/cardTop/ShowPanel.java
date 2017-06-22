package window.cardTop;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import communication.udp.StateUpdateListener;
import data.communication.StateData;

public class ShowPanel extends JPanel implements StateUpdateListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel[][][] label_status = {
			{	
				{new JLabel("Mode"), new JLabel("none")},
				{new JLabel("Start Time"), new JLabel("none")},
				{new JLabel("Game Number"), new JLabel("none")},
				{new JLabel("Score"), new JLabel("none")}
			},
			{	
				{new JLabel("Team ID"), new JLabel("none")},
				{new JLabel("Robot ID"), new JLabel("none")},
				{new JLabel("Robot ID"), new JLabel("none")}
			},
			{
				{new JLabel("Team ID"), new JLabel("none")},
				{new JLabel("Robot ID"), new JLabel("none")},
				{new JLabel("Robot ID"), new JLabel("none")}
			}
	};
	private JPanel[] panel_status = {
			new JPanel(),
			new JPanel(),
			new JPanel()
	};
	
	public ShowPanel(){
		// setup this panel
		this.setOpaque(false);
		
		// this panel border
		TitledBorder title = new TitledBorder("Show Status");
		title.setTitleFont(new Font("", 0, CardTop.TITLE_SIZE));
		CompoundBorder border = new CompoundBorder( title, new EmptyBorder(10,15,15,15) );
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(0,15,15,15), border );
		this.setBorder(outside);
		
		// setup components
		setup_panel_status();
		
		this.setLayout(new GridLayout(3,1));
		for(int i = 0; i < 3; i++){
			this.add(panel_status[i]);
		}
		
	}

	private void setup_panel_status() {
		for(int k = 0; k < label_status.length; k++){
			panel_status[k].setLayout(new GridLayout(label_status[k].length, 2));
			panel_status[k].setBorder(new LineBorder(Color.BLACK));
			panel_status[k].setOpaque(false);
			for(int j = 0; j < label_status[k].length; j++){
				for(int i = 0; i < label_status[k][j].length; i++){
					label_status[k][j][i].setHorizontalAlignment(JLabel.CENTER);
					panel_status[k].add(label_status[k][j][i]);
				}
			}
		}
	}

	@Override
	public void state_update(StateData state) {
		String s_time = 
				""     + state.get_c_start_time().get(Calendar.HOUR_OF_DAY) +
				":"  + state.get_c_start_time().get(Calendar.MINUTE) +
				"'"  + state.get_c_start_time().get(Calendar.SECOND) +
				" \"" + state.get_c_start_time().get(Calendar.MILLISECOND);
		label_status[0][0][1].setText( state.get_mode() ); 
		label_status[0][1][1].setText( s_time ); 
		label_status[0][2][1].setText( String.valueOf(state.get_game_id()) ); 
		label_status[0][3][1].setText( state.get_score()[0] + " - " + state.get_score()[1] ); 
		
		label_status[1][0][1].setText( state.get_team_desc()[0].split(",")[0] ); 
		label_status[1][1][1].setText( state.get_robot_desc()[0].split(",")[0] );
		label_status[1][2][1].setText( state.get_robot_desc()[1].split(",")[0] );
		
		label_status[2][0][1].setText( state.get_team_desc()[1].split(",")[0] ); 
		label_status[2][1][1].setText( state.get_robot_desc()[2].split(",")[0] );
		label_status[2][2][1].setText( state.get_robot_desc()[3].split(",")[0] );
	}
}
