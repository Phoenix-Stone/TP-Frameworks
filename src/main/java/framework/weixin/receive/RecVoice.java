 
  
  
 
 
 

package framework.weixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 接收微信语音消息
 */
public class RecVoice extends RecWeixin {
	private String MediaId; // 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String Format; // 语音格式，如amr，speex等
	private String Recognition; // 语音识别结果
	private String MsgId; // 消息id，64位整型

	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecVoice(Map<String, Object> map) {
		super(map);
		this.setMediaId((String) map.get("xml.MediaId"));
		this.setFormat((String) map.get("xml.Format"));
		this.setRecognition((String) map.get("xml.Recognition"));
		this.setMsgId((String) map.get("xml.MsgId"));
	}

	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecVoice(JSONObject json) {
		super(json);
		this.setMediaId(json.getString("MediaId"));
		this.setFormat(json.getString("Format"));
		this.setRecognition(json.getString("Recognition"));
		this.setMsgId(json.getString("MsgId"));
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}