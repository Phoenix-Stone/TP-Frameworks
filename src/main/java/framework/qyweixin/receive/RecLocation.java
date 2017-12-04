package framework.qyweixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 接收微信地理位置消息
 */
public class RecLocation extends RecWeixin {
	private String Location_X; //X坐标信息
	private String Location_Y; //Y坐标信息
	private String Scale; //精度，可理解为精度或者比例尺、越精细的话 scale越高
	private String Label; //地理位置的字符串信息
	private String Poiname;//朋友圈POI的名字，可能为空

	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecLocation(Map<String, Object> map) {
		super(map);
		this.setLocation_X((String) map.get("xml.Location_X"));
		this.setLocation_Y((String) map.get("xml.Location_Y"));
		this.setScale((String) map.get("xml.Scale"));
		this.setLabel((String) map.get("xml.Label"));
		this.setPoiname((String) map.get("xml.Poiname"));
	}

	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecLocation(JSONObject json) {
		super(json);
		this.setLocation_X(json.getString("Location_X"));
		this.setLocation_Y(json.getString("Location_Y"));
		this.setScale(json.getString("Scale"));
		this.setLabel(json.getString("Label"));
		this.setPoiname(json.getString("Poiname"));
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

	public String getPoiname() {
		return Poiname;
	}

	public void setPoiname(String poiname) {
		Poiname = poiname;
	}

}