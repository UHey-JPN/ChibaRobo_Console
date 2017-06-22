package window.subMain;

import java.awt.CardLayout;
import java.util.concurrent.Executor;

import javax.swing.JPanel;

import main.SettingManager;
import window.cardOperation.CardOperation;
import window.cardSetting.CardSetting;
import window.cardTop.CardTop;
import window.cardTournament.CardTournament;
import window.main.OperationLock;
import window.main.WindowMain;
import window.subMenu.MenuClickedListener;
import window.subMenu.MenuList;

public class SubMain extends JPanel implements MenuClickedListener {
	private static final long serialVersionUID = 1L;
	
	public static final int TITLE_SIZE = 24;
	
	private CardLayout layout = new CardLayout();

	// create panel
	public CardTop card_top;
	public CardOperation card_operation;
	public CardTournament card_tournament;
	public CardKeepAlive card_keep_alive;
	public CardSetting	card_setting;
	public CardLog card_log;
	
	
	public SubMain(Executor ex, SettingManager sm, OperationLock ope_lock) {
		card_top = new CardTop(ope_lock);
		card_operation = new CardOperation(ex, sm);
		card_tournament = new CardTournament();
		card_keep_alive = new CardKeepAlive();
		card_setting = new CardSetting(sm);
		card_log = new CardLog();
		
		// set layout manager
		this.setBackground(WindowMain.BACK_COLOR);
		this.setOpaque(true);
		
		// add to this frame
		this.setLayout(layout);
		this.add(card_top, MenuList.menu_name[0]);
		this.add(card_operation, MenuList.menu_name[1]);
		this.add(card_tournament, MenuList.menu_name[2]);
		this.add(card_keep_alive, MenuList.menu_name[3]);
		this.add(card_setting, MenuList.menu_name[4]);
		this.add(card_log, MenuList.menu_name[5]);
	}
	
	@Override
	public void menu_clicked(int num) {
		layout.show(this, MenuList.menu_name[num]);
	}

}
