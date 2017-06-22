package window.cardOperation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AddScoreButton extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private GameOpePanel parent;
	private int point;
	private int side;
	
	private JButton btn;

	public AddScoreButton(GameOpePanel parent, int side, int point){
		this.parent = parent;
		this.point = point;
		this.side = side;
		
		this.btn = new JButton(String.valueOf(point));
		this.btn.setFont(new Font("", Font.PLAIN, 20));
		this.btn.addActionListener(this);
		
		this.setBackground(Color.LIGHT_GRAY);
		
		this.setBorder(new EmptyBorder(5,5,5,5));
		this.setLayout(new BorderLayout());
		this.add(btn);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		parent.add_score(side, point);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if( ((AbstractButton)e.getSource()).isSelected() ){
			this.setBackground(Color.CYAN);
		}else{
			this.setBackground(Color.LIGHT_GRAY);
		}
	}
	
}
