 
  
  
 
 
 

package framework.weixin.response;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 被动响应微信语音消息
 */
public class ResVoice extends ResWeixin{
	private String MediaId; //通过素材管理接口上传多媒体文件，得到的id
	
	public ResVoice(Map<String, Object> map) {
		super(map, "voice");
	}
	
	public ResVoice(JSONObject json) {
		super(json, "voice");
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String buildXml() {
		StringBuffer buildXml = new StringBuffer();
		
		buildXml.append("<Voice>");
		buildXml.append("<MediaId><![CDATA["+this.getMediaId()+"]]></MediaId>");
		buildXml.append("</Voice>");
		
		return super.buildXml(buildXml.toString());
	}
}