 
  
  
 
 
 

package framework.weixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 接收微信
 * @author Administrator
 *
 */
public class RecWeixin {
	private String FromUserName;
	private String ToUserName;
	private String CreateTime;
	private String MsgType;
	
	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecWeixin(Map<String, Object> map) {
		this.setFromUserName((String)map.get("xml.FromUserName"));
		this.setToUserName((String)map.get("xml.ToUserName"));
		this.setCreateTime((String)map.get("xml.CreateTime"));
		this.setMsgType((String)map.get("xml.MsgType"));
	}
	
	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecWeixin(JSONObject json) {
		this.setFromUserName(json.getString("xml.FromUserName"));
		this.setToUserName(json.getString("xml.ToUserName"));
		this.setCreateTime(json.getString("xml.CreateTime"));
		this.setMsgType(json.getString("xml.MsgType"));
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
}