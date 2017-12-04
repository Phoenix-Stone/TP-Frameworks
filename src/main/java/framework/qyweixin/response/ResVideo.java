package framework.qyweixin.response;

import java.util.Map;


/**
 * 被动响应微信视频信息
 * @author Administrator
 */
public class ResVideo extends ResWeixin{
	private String MediaId;
	private String Title;
	private String Description;
	
	public ResVideo(Map<String, Object> map) {
		super(map, "video");
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	
	public String buildXml() {
		StringBuffer buildXml = new StringBuffer();
		
		buildXml.append("<Video>");
		buildXml.append("<MediaId><![CDATA["+this.getMediaId()+"]]></MediaId>");
		buildXml.append("<Title><![CDATA["+this.getTitle()+"]]></Title>");
		buildXml.append("<Description><![CDATA["+this.getDescription()+"]]></Description>");
		buildXml.append("</Video>");
		
		return super.buildXml(buildXml.toString());
	}
}