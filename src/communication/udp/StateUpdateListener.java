package communication.udp;

import data.communication.StateData;

public interface StateUpdateListener {
	public void state_update(StateData state);
}
