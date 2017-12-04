package framework.qyweixin.response;

import java.util.Map;

/**
 * 被动响应微信
 * @author Administrator
 *
 */
public class ResWeixin {
	private String FromUserName;
	private String ToUserName;
	private String CreateTime;
	private String MsgType;
	
	public ResWeixin(Map<String, Object> map, String MsgType) {
		this.setFromUserName((String)map.get("xml.ToUserName"));
		this.setToUserName((String)map.get("xml.FromUserName"));
		this.setCreateTime((String)map.get("xml.CreateTime"));
		this.setMsgType(MsgType);
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}	
	
	//构建回复xml
	public String buildXml(String xmlStr) {
		StringBuffer buildXml = new StringBuffer();
		buildXml.append("<xml>");
		buildXml.append("<ToUserName><![CDATA["+getToUserName()+"]]></ToUserName>");
		buildXml.append("<FromUserName><![CDATA["+getFromUserName()+"]]></FromUserName>");
		buildXml.append("<CreateTime>"+getCreateTime()+"</CreateTime>");
		buildXml.append("<MsgType><![CDATA["+getMsgType()+"]]></MsgType>");
		buildXml.append(xmlStr);		
		buildXml.append("</xml>");
		return buildXml.toString();
	}
}