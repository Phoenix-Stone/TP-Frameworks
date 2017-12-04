

package framework.qyweixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 接收微信event事件
 */
public class RecEvent extends RecWeixin {
	/**
	 * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)、LOCATION(地理位置)、CLICK（点击）、VIEW（跳转）、
	 * scancode_push
	 * （扫码推事件的事件推送）、scancode_push(扫码推事件且弹出“消息接收中”提示框的事件推送)、pic_sysphoto
	 * （弹出系统拍照发图的事件推送）、pic_photo_or_album （弹出拍照或者相册发图的事件推送）、pic_weixin
	 * （弹出微信相册发图器的事件推送）、location_select
	 * （弹出地理位置选择器的事件推送）、enter_agent（成员进入应用的事件推送）、batch_job_result （异步任务完成事件推送）
	 */
	private String Event;
	private String EventKey;// 1、事件KEY值，与自定义菜单接口中KEY值对应 2、
	private String Ticket;
	private String Latitude;// 地理位置纬度
	private String Longitude;// 地理位置经度
	private String Precision;// 地理位置精度
	private String ScanCodeInfo;// 扫描信息
	private String ScanType;// 扫描类型，一般是qrcode
	private String ScanResult;// 扫描结果，即二维码对应的字符串信息
	private String SendPicsInfo;// 发送的图片信息
	private String BatchJob; // 事件处理结果

	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecEvent(Map<String, Object> map) {
		super(map);
		this.setEvent((String) map.get("xml.Event"));
		this.setEventKey((String) map.get("xml.EventKey"));
		this.setTicket((String) map.get("xml.Ticket"));
		this.setLatitude((String) map.get("xml.Latitude"));
		this.setLongitude((String) map.get("xml.Longitude"));
		this.setPrecision((String) map.get("xml.Precision"));
		this.setScanCodeInfo((String) map.get("xml.ScanCodeInfo"));
		this.setScanType((String) map.get("xml.ScanType"));
		this.setScanResult((String) map.get("xml.ScanResult"));
		this.setSendPicsInfo((String) map.get("xml.SendPicsInfo"));
		this.setBatchJob((String) map.get("xml.BatchJob"));
	}

	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecEvent(JSONObject json) {
		super(json);
		this.setEvent(json.getString("Event"));
		this.setEventKey(json.getString("EventKey"));
		this.setTicket(json.getString("Ticket"));
		this.setLatitude(json.getString("Latitude"));
		this.setLongitude(json.getString("Longitude"));
		this.setPrecision(json.getString("Precision"));
		this.setScanCodeInfo(json.getString("ScanCodeInfo"));
		this.setScanType(json.getString("ScanType"));
		this.setScanResult(json.getString("ScanResult"));
		this.setSendPicsInfo(json.getString("SendPicsInfo"));
		this.setBatchJob(json.getString("BatchJob"));
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

	public String getScanCodeInfo() {
		return ScanCodeInfo;
	}

	public void setScanCodeInfo(String scanCodeInfo) {
		ScanCodeInfo = scanCodeInfo;
	}

	public String getScanType() {
		return ScanType;
	}

	public void setScanType(String scanType) {
		ScanType = scanType;
	}

	public String getScanResult() {
		return ScanResult;
	}

	public void setScanResult(String scanResult) {
		ScanResult = scanResult;
	}

	public String getSendPicsInfo() {
		return SendPicsInfo;
	}

	public void setSendPicsInfo(String sendPicsInfo) {
		SendPicsInfo = sendPicsInfo;
	}

	public String getBatchJob() {
		return BatchJob;
	}

	public void setBatchJob(String batchJob) {
		BatchJob = batchJob;
	}
}