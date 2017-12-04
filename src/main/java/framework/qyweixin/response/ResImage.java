package framework.qyweixin.response;

import java.util.Map;


/**
 * 被动响应微信图片信息
 * @author Administrator
 */
public class ResImage extends ResWeixin{
	private String MediaId;
	
	public ResImage(Map<String, Object> map) {
		super(map, "image");
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String buildXml() {
		StringBuffer buildXml = new StringBuffer();
		
		buildXml.append("<Image>");
		buildXml.append("<MediaId><![CDATA["+this.getMediaId()+"]]></MediaId>");
		buildXml.append("</Image>");
		
		return super.buildXml(buildXml.toString());
	}
}