package framework.qyweixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 接收微信
 * 
 * @author Administrator
 * 
 */
public class RecWeixin {
	private String FromUserName; // 成员UserID
	private String ToUserName; // 企业号CorpID
	private String CreateTime; // 消息创建时间（整型）
	private String MsgType; // 消息类型
	private Integer AgentID; // 企业应用的id，整型。可在应用的设置页面查看

	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecWeixin(Map<String, Object> map) {
		this.setFromUserName((String) map.get("xml.FromUserName"));
		this.setToUserName((String) map.get("xml.ToUserName"));
		this.setCreateTime((String) map.get("xml.CreateTime"));
		this.setMsgType((String) map.get("xml.MsgType"));
		this.setAgentID(Integer.parseInt((String) map.get("xml.AgentID")));
	}

	/**
	 * 以json为参数创建接受对象
	 * @param map
	 */
	public RecWeixin(JSONObject json) {
		this.setFromUserName(json.getString("FromUserName"));
		this.setToUserName(json.getString("ToUserName"));
		this.setCreateTime(json.getString("CreateTime"));
		this.setMsgType(json.getString("MsgType"));
		this.setAgentID(Integer.parseInt(json.getString("AgentID")));
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

	public Integer getAgentID() {
		return AgentID;
	}

	public void setAgentID(Integer agentID) {
		AgentID = agentID;
	}
}