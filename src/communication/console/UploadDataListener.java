package communication.console;

public interface UploadDataListener {
	public enum TYPE{
		ROBOT,
		TEAM,
		TOURNAMENT,
		IMAGE
	}
	
	public void update_data(TYPE type);
}
