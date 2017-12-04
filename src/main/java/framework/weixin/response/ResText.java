 
  
  
 
 
 

package framework.weixin.response;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 被动响应微信文字信息
 */
public class ResText extends ResWeixin{
	private String Content; //回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
	
	public ResText(Map<String, Object> map) {
		super(map, "text");
	}
	
	public ResText(JSONObject json) {
		super(json, "text");
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
	public String buildXml() {
		StringBuffer buildXml = new StringBuffer();
		
		buildXml.append("<Content><![CDATA["+this.getContent()+"]]></Content>");
		
		return super.buildXml(buildXml.toString());
	}
}