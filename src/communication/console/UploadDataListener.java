package communication.console;

public interface UploadDataListener {
	public static final int TYPE_ROBOT = 0;
	public static final int TYPE_TEAM = 1;
	public static final int TYPE_TOURNAMENT = 2;
	public void update_data(int type);
}
