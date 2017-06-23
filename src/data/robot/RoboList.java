package data.robot;

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
import window.main.LogMessageAdapter;

public class RoboList implements ServerUpdateListener, DatabaseGetterListener, UpdateDatabaseListener{
	private List<Robot> list;
	private final Object lock_obj = new Object();
	private Executor ex;
	private ServerData state = null;
	private LogMessageAdapter log_mes;
	private UpdateTourViewListener update_view_listener;
	private boolean initialize = false;
	
	
	public RoboList(Executor ex, LogMessageAdapter log_mes) {
		this.list = Collections.synchronizedList(new ArrayList<Robot>());
		this.ex = ex;
		this.log_mes = log_mes;
	}
	
	public boolean add(int id, String data){
		synchronized(lock_obj){
			for(Robot r : list){
				if( r.get_id() == id ){
					list.remove(r);
				}
			}
			list.add(new Robot(id, data));
			return list.get(list.size()-1).get_valid();
		}
	}
	
	public void set_UpdateTourViewListener(UpdateTourViewListener l){
		update_view_listener = l;
	}
	
	public void clear(){
		synchronized(lock_obj){
			list.clear();
		}
	}

	public String get_robot_summary(int id) throws DataNotFoundException {
		synchronized(lock_obj){
			for(Robot r : list){
				if(r.get_id() == id){
					return r.get_summary();
				}
			}
			throw new DataNotFoundException("Robot(id=" + id + ") is not found.");
		}
	}
	
	public void update_robot_list(){
		log_mes.log_println("----------------- update robot list -----------------");
		ex.execute( new TcpDataGetter(TcpDataGetter.TYPE.ROBOT, this, this.state, log_mes) );
	}

	@Override
	public void server_update(ServerData state) {
		this.state = state;
		if( initialize == false ){
			update_robot_list();
			initialize = true;
		}
	}

	@Override
	public void set_new_database(String data) {
		// XML Process
		RobotXmlParser xml_process = new RobotXmlParser(data);	
		list = xml_process.get_list();
		
		// update TourView
		if( update_view_listener != null) update_view_listener.update_tour_view();
	}

	@Override
	public void update_database() {
		this.update_robot_list();
	}

}
