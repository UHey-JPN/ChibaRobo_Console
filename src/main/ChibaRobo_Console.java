package main;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import communication.console.TcpSocket;
import communication.udp.UdpSocket;
import data.communication.FileDataManager;
import data.robot.RoboList;
import data.team.TeamList;
import data.tournament.Tournament;
import window.main.WindowMain;

public class ChibaRobo_Console {

	public ChibaRobo_Console() {
		Executor ex = Executors.newCachedThreadPool();
		SettingManager sm = new SettingManager();
		WindowMain window_main = new WindowMain(ex, sm);
		FileDataManager fdm = new FileDataManager(window_main.get_LogMessageAdapter(), sm);
		UdpSocket udp = new UdpSocket(ex, window_main.get_LogMessageAdapter());
		TcpSocket tcp = new TcpSocket(udp, window_main.get_LogMessageAdapter(), sm, fdm);
		RoboList robo_list = new RoboList(ex, window_main.get_LogMessageAdapter());
		TeamList team_list = new TeamList(ex, window_main.get_LogMessageAdapter());
		Tournament tour = new Tournament(ex, window_main.get_LogMessageAdapter());

		// ---------------------------------------
		// set instance
		// ---------------------------------------
		window_main.sub_main.card_tournament.main_view.set_robo_list(robo_list);
		window_main.sub_main.card_tournament.main_view.set_team_list(team_list);
		window_main.sub_main.card_tournament.main_view.set_tournament(tour);
		
		// ---------------------------------------
		// set listener
		// ---------------------------------------
		// UDP Packet Listener
		udp.add_ServerUpdateListener(window_main.sub_main.card_top.server_panel);
		udp.add_ServerUpdateListener(window_main.sub_main.card_top.server_panel.connection_panel);
		udp.add_ServerUpdateListener(window_main.sub_main.card_tournament.update_button);
		udp.add_ServerUpdateListener(robo_list);
		udp.add_ServerUpdateListener(team_list);
		udp.add_ServerUpdateListener(tour.get_game_as_SUL());
		
		udp.add_StateUpdateListener(window_main.sub_main.card_top.show_panel);
		udp.add_StateUpdateListener(window_main.sub_main.card_operation.show_panel);
		
		udp.add_TournamentUpdateListener(tour);
		
		// TCP connection listener
		window_main.sub_main.card_top.server_panel.connection_panel.set_TcpConnectionListener(tcp);
		
		// mode listener
		window_main.sub_main.card_operation.show_ope_panel.b_home.add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_newgame.add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_game.add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_result.add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_interview.add_SetModeListener(tcp);
		
		window_main.sub_main.card_operation.show_ope_panel.b_left.add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_right.add_SetModeListener(tcp);

		window_main.sub_main.card_operation.show_ope_panel.b_preshow.add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_opening.add_SetModeListener(tcp);

		window_main.sub_main.card_operation.show_ope_panel.b_show[0].add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_show[1].add_SetModeListener(tcp);
		window_main.sub_main.card_operation.show_ope_panel.b_show[2].add_SetModeListener(tcp);

		
		window_main.sub_main.card_operation.game_ope_panel.winner_button[0].add_SetModeListener(tcp);
		window_main.sub_main.card_operation.game_ope_panel.winner_button[1].add_SetModeListener(tcp);

		// score listener
		window_main.sub_main.card_operation.game_ope_panel.set_SetScoreListener(tcp);
		
		// winner listener
		window_main.sub_main.card_operation.game_ope_panel.winner_button[0].set_SetWinnerListener(tcp);
		window_main.sub_main.card_operation.game_ope_panel.winner_button[1].set_SetWinnerListener(tcp);

		// clear data listener
		window_main.sub_main.card_setting.update_data_panel.set_ClearDataListener(tcp);
		// check integrity listener
		window_main.sub_main.card_setting.update_data_panel.set_CheckIntegrityListener(fdm);
		// upload data listener
		window_main.sub_main.card_setting.update_data_panel.set_UploadDataListener(tcp);
		// Update Database Listener
		window_main.sub_main.card_tournament.update_button.add_UpdateDatabaseListener(robo_list);
		window_main.sub_main.card_tournament.update_button.add_UpdateDatabaseListener(team_list);
		window_main.sub_main.card_tournament.update_button.add_UpdateDatabaseListener(tour.get_game_as_UDL());
		
		// update tournament view listener
		robo_list.set_UpdateTourViewListener(window_main.sub_main.card_tournament.get_UpdateTourViewListener());
		team_list.set_UpdateTourViewListener(window_main.sub_main.card_tournament.get_UpdateTourViewListener());
		tour.set_UpdateTourViewListener(window_main.sub_main.card_tournament.get_UpdateTourViewListener());
	}
	
	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8");
		new ChibaRobo_Console();
	}

}
