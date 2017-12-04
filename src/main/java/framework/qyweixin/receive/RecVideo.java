package framework.qyweixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 接收微信视频信息
 */
public class RecVideo extends RecWeixin{
	private String MediaId; //视频媒体文件id，可以调用获取媒体文件接口拉取数据
	private String ThumbMediaId; //视频消息缩略图的媒体id，可以调用获取媒体文件接口拉取数据
	private String MsgId; //消息id，64位整型
	
	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecVideo(Map<String, Object> map) {
		super(map);
		this.setMediaId((String)map.get("xml.MediaId"));
		this.setThumbMediaId((String)map.get("xml.ThumbMediaId"));
		this.setMsgId((String)map.get("xml.MsgId"));
	}

	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecVideo(JSONObject json) {
		super(json);
		this.setMediaId(json.getString("MediaId"));
		this.setThumbMediaId(json.getString("ThumbMediaId"));
		this.setMsgId(json.getString("MsgId"));
	}
	
	public String getMediaId() {
		return MediaId;
	}


	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}


	public String getThumbMediaId() {
		return ThumbMediaId;
	}


	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}


	public String getMsgId() {
		return MsgId;
	}


	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

}