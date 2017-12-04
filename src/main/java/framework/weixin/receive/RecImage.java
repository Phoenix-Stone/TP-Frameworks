package framework.weixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 接收微信图片信息
 * 
 */
public class RecImage extends RecWeixin {
	private String PicUrl; //图片链接
	private String MediaId; //图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String MsgId; //消息id，64位整型

	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecImage(Map<String, Object> map) {
		super(map);
		this.setPicUrl((String)map.get("xml.PicUrl"));
		this.setMediaId((String)map.get("xml.MediaId"));
		this.setMsgId((String)map.get("xml.MsgId"));
	}

	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecImage(JSONObject json) {
		super(json);
		this.setPicUrl(json.getString("PicUrl"));
		this.setMediaId(json.getString("MediaId"));
		this.setMsgId(json.getString("MsgId"));
	}
	
	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}