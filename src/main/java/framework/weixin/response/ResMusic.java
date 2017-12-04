 
  
  
 
 
 

package framework.weixin.response;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 被动响应微信图片信息
 */
public class ResMusic extends ResWeixin{
	private String Title; //音乐标题
	private String Description; //音乐描述
	private String MusicUrl; //音乐链接
	private String HQMusicUrl; //高质量音乐链接，WIFI环境优先使用该链接播放音乐
	private String ThumbMediaId; //缩略图的媒体id，通过素材管理接口上传多媒体文件，得到的id
	
	public ResMusic(Map<String, Object> map) {
		super(map, "music");
	}
	
	public ResMusic(JSONObject json) {
		super(json, "music");
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

	public String getMusicUrl() {
		return MusicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		MusicUrl = musicUrl;
	}

	public String getHQMusicUrl() {
		return HQMusicUrl;
	}

	public void setHQMusicUrl(String hQMusicUrl) {
		HQMusicUrl = hQMusicUrl;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public String buildXml() {
		StringBuffer buildXml = new StringBuffer();
		
		buildXml.append("<Music>");
		buildXml.append("<Title><![CDATA["+this.getTitle()+"]]></Title>");
		buildXml.append("<Description><![CDATA["+this.getDescription()+"]]></Description>");
		buildXml.append("<MusicUrl><![CDATA["+this.getMusicUrl()+"]]></MusicUrl>");
		buildXml.append("<HQMusicUrl><![CDATA["+this.getHQMusicUrl()+"]]></HQMusicUrl>");
		buildXml.append("<ThumbMediaId><![CDATA["+this.getThumbMediaId()+"]]></ThumbMediaId>");
		buildXml.append("</Music>");
		
		return super.buildXml(buildXml.toString());
	}
}