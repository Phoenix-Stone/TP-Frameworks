 
  
  
 
 
 

package framework.weixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 接收微信地理位置消息
 */
public class RecLocation extends RecWeixin{
	private String Location_X; //地理位置维度
	private String Location_Y; //地理位置经度
	private String Scale; //地图缩放大小
	private String Label; //地理位置信息
	private String MsgId; //消息id，64位整型
	
	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecLocation(Map<String, Object> map) {
		super(map);
		this.setLocation_X((String)map.get("xml.MediaId"));
		this.setLocation_Y((String)map.get("xml.Format"));
		this.setScale((String)map.get("xml.Format"));
		this.setLabel((String)map.get("xml.Label"));
		this.setMsgId((String)map.get("xml.MsgId"));
	}
	
	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecLocation(JSONObject json) {
		super(json);
		this.setLocation_X(json.getString("MediaId"));
		this.setLocation_Y(json.getString("Format"));
		this.setScale(json.getString("Format"));
		this.setLabel(json.getString("Label"));
		this.setMsgId(json.getString("MsgId"));
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(String scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}