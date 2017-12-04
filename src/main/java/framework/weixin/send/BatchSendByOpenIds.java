package framework.weixin.send;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 根据openid群发
 * @author shixiaolong
 * @date2014-4-16
 */
public class BatchSendByOpenIds {
	/**
	 * openId集合
	 */
	public List<String> openIds;
	/**
	 * 用于群发的消息的media_id
	 */
	public String mediaId;
	/**
	 * 群发的消息类型，图文消息为mpnew
	 */
	public String msgtype="mpnews";
	
	
	

	public List<String> getOpenIds() {
		return openIds;
	}



	public void setOpenIds(List<String> openIds) {
		this.openIds = openIds;
	}



	public String getMediaId() {
		return mediaId;
	}



	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}



	public String getMsgtype() {
		return msgtype;
	}



	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	
	
	
	public String initStrings(){
//		String tousers = "";
//		for (int i = 0; i < this.openIds.size(); i++) {
//			if(i==0){
//				tousers="\""+this.openIds.get(i)+"\"";
//			}else{
//				tousers+=",\""+this.openIds.get(i)+"\"";
//			}
//		}
//		String result="{\"touser\":["+tousers+"],\"mpnews\":{\"media_id\":\""+this.mediaId+"\"},\"msgtype\":\""+this.msgtype+"\"}";
		
		JSONObject object = new JSONObject();
		object.put("touser", JSONArray.fromObject(this.openIds));
		
		JSONObject textObject = new JSONObject();
		textObject.put("media_id",this.mediaId);
		object.put("mpnews", textObject);
		
		object.put("msgtype",this.msgtype);
		
		return object.toString();
	}
	
}
