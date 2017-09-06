package communication.console;

public interface UploadDataListener {
	public enum TYPE{
		ROBOT,
		TEAM,
		TOURNAMENT
	}
	
	public void update_data(TYPE type);
}
