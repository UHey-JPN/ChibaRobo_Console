package communication.console;

public interface ClearDataListener {
	public static final int TYPE_ROBOT = 0;
	public static final int TYPE_TEAM = 1;
	public void clear_data(int type);
}
