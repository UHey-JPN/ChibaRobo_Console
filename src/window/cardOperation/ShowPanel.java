package window.cardOperation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.concurrent.Executor;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import communication.udp.StateUpdateListener;
import data.communication.StateData;
import window.cardTop.CardTop;

public class ShowPanel extends JPanel implements StateUpdateListener, Runnable{
	private static final long serialVersionUID = 1L;

	private static final int TITLE_SIZE = 15;
	
	private StateData show_state;
	
	private JLabel label_mode = new JLabel("none");
	private JLabel label_s_time = new JLabel("none");
	private JLabel label_time = new JLabel("none");


	public ShowPanel(Executor ex) {
		// setup this panel
		this.setOpaque(false);
		
		// this panel border
		TitledBorder title = new TitledBorder("Show Status");
		title.setTitleFont(new Font("", 0, CardTop.TITLE_SIZE));
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(0,15,15,15), title );
		this.setBorder(outside);
		
		setup_label_mode();
		setup_label_time();
		setup_label_s_time();
		
		this.setLayout(new GridLayout(1,3));
		this.add(label_mode);
		this.add(label_time);
		this.add(label_s_time);
		
		ex.execute(this);
	}

	private void setup_label_mode() {
		TitledBorder title = new TitledBorder("Show Mode");
		title.setTitleFont(new Font("", 0, ShowPanel.TITLE_SIZE));
		title.setTitleJustification(TitledBorder.CENTER);
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(0,10,10,10), title );
		label_mode.setBorder(outside);
		label_mode.setHorizontalAlignment(JLabel.CENTER);
		label_mode.setFont(new Font("", Font.BOLD, 25));
		label_mode.setBackground(Color.YELLOW);
		label_mode.setOpaque(true);
	}
	private void setup_label_time() {
		TitledBorder title = new TitledBorder("Time");
		title.setTitleFont(new Font("", 0, ShowPanel.TITLE_SIZE));
		title.setTitleJustification(TitledBorder.CENTER);
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(0,10,10,10), title );
		label_time.setBorder(outside);
		label_time.setHorizontalAlignment(JLabel.CENTER);
		label_time.setFont(new Font("", Font.BOLD, 25));
	}
	private void setup_label_s_time() {
		TitledBorder title = new TitledBorder("Start Time");
		title.setTitleFont(new Font("", 0, ShowPanel.TITLE_SIZE));
		title.setTitleJustification(TitledBorder.CENTER);
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(0,10,10,10), title );
		label_s_time.setBorder(outside);
		label_s_time.setHorizontalAlignment(JLabel.CENTER);
		label_s_time.setFont(new Font("", Font.BOLD, 25));
	}

	
	@Override
	public void state_update(StateData state) {
		show_state = state;
		label_mode.setText(state.get_mode());
		String s_time = 
				""     + state.get_c_start_time().get(Calendar.HOUR_OF_DAY) +
				":"  + state.get_c_start_time().get(Calendar.MINUTE) +
				"'"  + state.get_c_start_time().get(Calendar.SECOND) +
				" \"" + state.get_c_start_time().get(Calendar.MILLISECOND);
		label_s_time.setText(s_time);
	}

	@Override
	public void run() {
		while(true){
			long time_diff = 0;
			if( show_state != null ){
				time_diff = Calendar.getInstance().getTimeInMillis() - show_state.get_c_start_time().getTimeInMillis();
			}
			label_time.setText(String.valueOf(time_diff));
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
