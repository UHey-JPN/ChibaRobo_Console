package window.subMenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import window.main.WindowMain;

class MenuTab extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel name;
	private int number;
	private MenuClickedListener menu_clicked_listener;

	public MenuTab(String text, int number) {
		this.number = number;
		
		name = new JLabel(text, JLabel.CENTER);
		name.setFont(new Font("", Font.BOLD, 20));
		
		this.setLayout( new BorderLayout() );
		this.add(name);
		this.setOpaque(true);
		this.unselected();
		this.addMouseListener(this);
	}
	
	void add_MenuClickedListener(MenuClickedListener menu_clicked_listener){
		this.menu_clicked_listener = menu_clicked_listener;
	}
	
	void unselected(){
		this.setBackground(Color.BLUE);
		name.setForeground(Color.LIGHT_GRAY);
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
	}
	
	void selected(){
		this.setBackground(WindowMain.BACK_COLOR);
		name.setForeground(Color.BLACK);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if( menu_clicked_listener != null){
			menu_clicked_listener.menu_clicked(number);
		}
	}

	@Override // nothing to do
	public void mousePressed(MouseEvent e) {;}
	@Override // nothing to do
	public void mouseReleased(MouseEvent e) {;}
	@Override // nothing to do
	public void mouseEntered(MouseEvent e) {;}
	@Override // nothing to do
	public void mouseExited(MouseEvent e) {;}
	
}
