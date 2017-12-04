package framework.qyweixin.response;

import java.util.Map;

/**
 * 被动响应微信语音消息
 * @author Administrator
 */
public class ResVoice extends ResWeixin{
	private String MediaId;
	
	public ResVoice(Map<String, Object> map) {
		super(map, "voice");
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