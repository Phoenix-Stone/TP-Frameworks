 
  
  
 
 
 

package framework.weixin.response;

import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 被动响应微信图片信息
 */
public class ResImage extends ResWeixin{
	private String MediaId; //通过素材管理接口上传多媒体文件，得到的id。
	
	public ResImage(Map<String, Object> map) {
		super(map, "image");
	}
	
	public ResImage(JSONObject json) {
		super(json, "image");
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