package framework.qyweixin.response;

import java.util.Map;

/**
 * 被动响应微信文本信息
 * @author Administrator
 */
public class ResText extends ResWeixin{
	private String Content;
	
	public ResText(Map<String, Object> map) {
		super(map, "text");
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