package window.subMenu;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class SubMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public MenuList menu_list = new MenuList();

	public SubMenu() {
		this.setBackground(Color.WHITE);
		this.setOpaque(true);
		
		this.setLayout( new GridLayout(MenuList.NUM_OF_MENU ,1) );
		for(int i = 0; i < MenuList.NUM_OF_MENU; i++){
			this.add(menu_list.get_MenuTab(i));
		}
	}


}
