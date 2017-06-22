package window.cardOperation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import communication.console.SetScoreListener;
import main.SettingManager;
import window.cardTop.CardTop;

public class GameOpePanel extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private int[] score = {0, 0};
	private JTextField[] t_score = {new JTextField("0"), new JTextField("0")};
	private JPanel[] panel = {new JPanel(), new JPanel(), new JPanel()};
	
	private JButton update_button = new JButton("Update");
	private JButton reset_button = new JButton("Reset");
	
	public SetWinnerButton[] winner_button = { new SetWinnerButton(0), new SetWinnerButton(1) };
	
	private SetScoreListener score_listener;
	private JToggleButton plus_minus_button = new JToggleButton("minus");
	
	private int[] point_list;
	
	public GameOpePanel(SettingManager sm) {
		point_list = sm.get_point_list();
		// setup this panel
		this.setOpaque(false);

		// this panel border
		TitledBorder title = new TitledBorder("Operate GAME");
		title.setTitleFont(new Font("", 0, CardTop.TITLE_SIZE));
		CompoundBorder outside = new CompoundBorder( new EmptyBorder(0,15,15,15), title );
		this.setBorder(outside);
		
		// setup button
		update_button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		update_button.setMaximumSize(new Dimension(300, 80));
		update_button.addActionListener(this);
		reset_button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		reset_button.setMaximumSize(new Dimension(300, 80));
		reset_button.addActionListener(this);
		plus_minus_button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		plus_minus_button.setMaximumSize(new Dimension(300, 80));
		plus_minus_button.addChangeListener(this);
		plus_minus_button.setOpaque(true);
		
		// setup panel
		for(int i = 0; i < panel.length; i++ ){
			panel[i].setOpaque(false);
			if( i == 2 ){
				panel[i].setLayout(new BoxLayout(panel[i], BoxLayout.Y_AXIS));
				panel[i].add(update_button);
				panel[i].add(Box.createRigidArea(new Dimension(10,20)));
				panel[i].add(reset_button);
				panel[i].add(Box.createRigidArea(new Dimension(10,20)));
				panel[i].add(plus_minus_button);
			}else{
				t_score[i].setFont(new Font("", Font.PLAIN, 20));
				t_score[i].setHorizontalAlignment(JTextField.CENTER);
				
				panel[i].setLayout(new BoxLayout(panel[i], BoxLayout.Y_AXIS));
				panel[i].add(t_score[i]);
				for(int j = 0; j < point_list.length; j++){
					AddScoreButton btn = new AddScoreButton(this, i, point_list[j]);
					panel[i].add(btn);
					plus_minus_button.addChangeListener(btn);
				}
				panel[i].add( winner_button[i] );
			}
		}
		
		this.setLayout(new GridBagLayout());
		this.add(panel[0], get_gbc(0,0,1,1, 1,1));
		this.add(panel[2], get_gbc(1,0,1,1, 0.2,1));
		this.add(panel[1], get_gbc(2,0,1,1, 1,1));
		
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

	public void set_SetScoreListener(SetScoreListener listener){
		this.score_listener = listener;
	}
	
	private void socre_update(){
		for(int i = 0; i < score.length; i++){
			t_score[i].setText(String.valueOf(score[i]));
		}
		score_listener.set_score(score[0], score[1]);
	}

	public void add_score(int side, int point) {
		if( plus_minus_button.isSelected() ){
			score[side] -= point;
		}else{
			score[side] += point;
		}
		socre_update();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == update_button ){
			for(int i = 0; i < score.length; i++){
				score[i] = Integer.parseInt(t_score[i].getText());
			}
			socre_update();
		}else if( e.getSource() == reset_button ){
			score_reset();
		}
	}

	private void score_reset() {
		for(int i = 0; i < score.length; i++){
			score[i] = 0;
			socre_update();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		AbstractButton btn = (AbstractButton) e.getSource();
		if( btn.isSelected() ){
			btn.setBackground(Color.CYAN);
			btn.setFont(new Font("", Font.BOLD, 12));
		}else{
			btn.setBackground(Color.WHITE);
			btn.setFont(new Font("", Font.PLAIN, 12));
		}
	}

}
