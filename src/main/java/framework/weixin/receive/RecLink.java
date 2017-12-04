
package framework.weixin.receive;

import java.util.Map;

import net.sf.json.JSONObject;


/**
 * 接收微信链接消息
 */
public class RecLink extends RecWeixin{
	private String Title; //消息标题
	private String Description; //消息描述
	private String Url; //消息链接
	private String MsgId; //消息id，64位整型
	
	/**
	 * 以map为参数创建接受对象
	 * @param map
	 */
	public RecLink(Map<String, Object> map) {
		super(map);
		this.setTitle((String)map.get("xml.Title"));
		this.setDescription((String)map.get("xml.Description"));
		this.setUrl((String)map.get("xml.Url"));
		this.setMsgId((String)map.get("xml.MsgId"));
	}
	
	/**
	 * 以json为参数创建接受对象
	 * @param JSONObject
	 */
	public RecLink(JSONObject json) {
		super(json);
		this.setTitle(json.getString("Title"));
		this.setDescription(json.getString("Description"));
		this.setUrl(json.getString("Url"));
		this.setMsgId(json.getString("MsgId"));
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

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}