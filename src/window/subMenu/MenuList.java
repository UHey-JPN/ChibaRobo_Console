package window.subMenu;

public class MenuList implements MenuClickedListener {
	public static final int NUM_OF_MENU = 6;
	private MenuTab[] menu_list = new MenuTab[NUM_OF_MENU];
	
	private int sel_num = 0;
	
	private MenuClickedListener listener;
	
	public final static String[] menu_name = {
			"TOP",
			"Operation",
			"Tournament",
			"Keep Alive",
			"Settings",
			"Log"
		};

	MenuList() {
		// create instance
		for(int i = 0; i < NUM_OF_MENU; i++ ){
			menu_list[i] = new MenuTab( menu_name[i], i);
			menu_list[i].add_MenuClickedListener(this);
		}
		select(0);
	}
	
	public void add_MenuClickedListener(MenuClickedListener listener){
		this.listener = listener;
	}
	
	int get_selected(){
		return sel_num;
	}
	
	MenuTab get_MenuTab(int num){
		return menu_list[num];
	}
	
	private void select(int num){
		menu_list[sel_num].unselected();
		menu_list[num].selected();
		sel_num = num;
	}

	@Override
	public void menu_clicked(int num) {
		select(num);
		if(listener != null){
			listener.menu_clicked(num);
		}
	}

}
