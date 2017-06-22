package window.cardTournament;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import communication.udp.ServerUpdateListener;
import data.communication.ServerData;


public class UpdateTournamentButton extends JPanel implements ActionListener, ServerUpdateListener {
	private static final long serialVersionUID = 1L;
	
	private JButton btn = new JButton("Update");
	
	private ArrayList<UpdateDatabaseListener> listener_list = new ArrayList<UpdateDatabaseListener>();

	public UpdateTournamentButton() {
		// setup this panel
		this.setOpaque(false);
		this.setBorder(new EmptyBorder(5,5,5,5));
		
		// setup button
		btn.addActionListener(this);
		btn.setEnabled(false);
		
		this.add(btn);
	}
	
	public void add_UpdateDatabaseListener(UpdateDatabaseListener l){
		listener_list.add(l);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(UpdateDatabaseListener l : listener_list){
			l.update_database();
		}
	}

	@Override
	public void server_update(ServerData state) {
		btn.setEnabled(true);
	}

}
