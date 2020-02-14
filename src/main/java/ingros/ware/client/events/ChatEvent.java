package ingros.ware.client.events;

import net.b0at.api.event.Event;

public class ChatEvent extends Event {
	private String msg;

	public ChatEvent(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
