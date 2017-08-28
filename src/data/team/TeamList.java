package data.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import communication.dataGetter.DatabaseGetterListener;
import communication.dataGetter.TcpDataGetter;
import communication.udp.ServerUpdateListener;
import data.communication.ServerData;
import data.exception.DataNotFoundException;
import window.cardTournament.UpdateDatabaseListener;
import window.cardTournament.UpdateTourViewListener;
import window.logger.LogMessageAdapter;

public class TeamList implements ServerUpdateListener, DatabaseGetterListener, UpdateDatabaseListener {
	private List<Team> list;
	private Executor ex;
	private ServerData state = null;
	private LogMessageAdapter log_mes;
	private UpdateTourViewListener update_view_listener;
	private boolean initialize = false;

	public TeamList(Executor ex, LogMessageAdapter log_mes) {
		list = Collections.synchronizedList(new ArrayList<Team>());
		this.ex = ex;
		this.log_mes = log_mes;
	}
	
	public boolean add(int id, String name, int id1, int id2, String desc){
		synchronized(list){
			for(Team t : list){
				if(t.get_id() == id){
					list.remove(t);
				}
			}
			list.add(new Team(id, name, id1, id2, desc));
			return list.get(list.size()-1).get_valid();
		}
	}
	
	public void set_UpdateTourViewListener(UpdateTourViewListener l){
		update_view_listener = l;
	}
	
	public void clear(){
		synchronized(list){
			list.clear();
		}
	}
	
	public int[] get_robot_id(int team_id) throws DataNotFoundException {
		synchronized(list){
			for(Team t : list){
				if(t.get_id() == team_id){
					return t.get_robot_id();
				}
			}
			throw new DataNotFoundException("Team(id=" + team_id + ") is not found.");
		}
	}
	
	public String get_team_summary(int team_id) throws DataNotFoundException{
		synchronized(list){
			for(Team t : list){
				if(t.get_id() == team_id){
					return t.get_summary();
				}
			}
			throw new DataNotFoundException("Team(id=" + team_id + ") is not found.");
		}
	}

	public String get_team_name(int team_id) throws DataNotFoundException{
		synchronized(list){
			for(Team t : list){
				if(t.get_id() == team_id){
					return t.get_name();
				}
			}
			throw new DataNotFoundException("Team(id=" + team_id + ") is not found.");
		}
	}

	public void update_team_list(){
		log_mes.log_println("----------------- update team list -----------------");
		ex.execute( new TcpDataGetter(TcpDataGetter.TYPE.TEAM, this, this.state, log_mes) );
	}
	
	@Override
	public void update_database() {
		update_team_list();
	}

	@Override
	public void set_new_database(String data) {
		// XML Process
		TeamXmlParser xml_process = new TeamXmlParser(data);	
		list = xml_process.get_list();
		
		// update Tour View
		update_view_listener.update_tour_view();
	}

	@Override
	public void server_update(ServerData state) {
		this.state = state;
		if( initialize == false ){
			update_team_list();
			initialize = true;
		}
	}
	

}
