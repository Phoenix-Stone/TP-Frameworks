 
  
  
 
 
 

package framework.weixin.response;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 被动响应微信视频信息
 */
public class ResVideo extends ResWeixin{
	private String MediaId; //通过素材管理接口上传多媒体文件，得到的id
	private String Title; //视频消息的标题
	private String Description; //视频消息的描述
	
	public ResVideo(Map<String, Object> map) {
		super(map, "video");
	}
	
	public ResVideo(JSONObject json) {
		super(json, "text");
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