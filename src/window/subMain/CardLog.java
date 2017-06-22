package window.subMain;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import window.main.WindowMain;

public class CardLog extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextArea area = new JTextArea();
	private JCheckBox scroll;
	
	public CardLog() {
		this.setBackground(WindowMain.BACK_COLOR);
		this.setOpaque(true);

		// setup text area
		area.setEditable(false);
		area.setBorder( new BevelBorder(BevelBorder.LOWERED) );
		area.setLineWrap(true);

		// setup scroll(text area)
		JScrollPane log_scroll = new JScrollPane(area);
		log_scroll.setBorder( new TitledBorder("LOG") );
		
		// setup clear button
		JButton clear_btn = new JButton("clear");
		clear_btn.setAlignmentX(0.9f);
		clear_btn.addActionListener(this);
		
		// setup check box(auto scroll)
		scroll = new JCheckBox("AutoScroll");
		scroll.setSelected(true);
		
		// setup low_panel
		JPanel low_panel = new JPanel();
		low_panel.setOpaque(false);
		low_panel.setLayout(new BoxLayout(low_panel,BoxLayout.X_AXIS));
		low_panel.add(scroll);
		low_panel.add( Box.createRigidArea(new Dimension(50,1)) );
		low_panel.add(clear_btn);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add( log_scroll );
		this.add( low_panel );
		
		this.log_println("Start Logging");
	}
	
	public void log_println(String str){
		String time = ( new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss") ).format(new Date());
		str = time  + " : " + str;
		area.append(str + "\n");
		if( scroll.isSelected() ) area.setCaretPosition(area.getDocument().getLength());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		area.setText("");
	}

}
