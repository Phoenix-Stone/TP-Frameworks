 
  
  
 
 
 

package framework.weixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 接收微信文字信息
 */
public class RecText extends RecWeixin{
	private String Content; //文本消息内容
	private String MsgId; //消息id，64位整型
	
	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecText(Map<String, Object> map) {
		super(map);
		this.setContent((String)map.get("xml.Content"));
		this.setMsgId((String)map.get("xml.MsgId"));
		this.setMsgId((String)map.get("xml.MsgId"));
	}
	
	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecText(JSONObject json) {
		super(json);
		this.setContent(json.getString("Content"));
		this.setMsgId(json.getString("MsgId"));
		this.setMsgId(json.getString("MsgId"));
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}