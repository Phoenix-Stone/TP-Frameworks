
package framework.weixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 接收微信event事件
 * 
 */
public class RecEvent extends RecWeixin {
	private String Event;
	private String EventKey;
	private String Ticket;
	private String Latitude;
	private String Longitude;
	private String Precision;

	public RecEvent(Map<String, Object> map) {
		super(map);
		this.setEvent((String) map.get("xml.Event"));
		this.setEventKey((String) map.get("xml.EventKey"));
		this.setTicket((String) map.get("xml.Ticket"));
		this.setLatitude((String) map.get("xml.Latitude"));
		this.setLongitude((String) map.get("xml.Longitude"));
		this.setPrecision((String) map.get("xml.Precision"));
	}

	public RecEvent(JSONObject json) {
		super(json);
		this.setEvent(json.getString("Event"));
		this.setEventKey(json.getString("EventKey"));
		this.setTicket(json.getString("Ticket"));
		this.setLatitude(json.getString("Latitude"));
		this.setLongitude(json.getString("Longitude"));
		this.setPrecision(json.getString("Precision"));
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}
}