package framework.weixin.send;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 根据组id群发
 * @author shixiaolong
 * @date2014-4-16
 */
public class BatchSendByGroup {
	/**
	 * 群发到的分组的group_id
	 */
	public String groupId;
	private String nameList;
	public String getNameList() {
		return nameList;
	}



	public void setNameList(String nameList) {
		this.nameList = nameList;
	}

	/**
	 * 用于群发的消息的media_id
	 */
	public String mediaId;
	/**
	 * 群发的消息类型，图文消息为mpnew
	 */
	public String msgtype="mpnews";
	
	public String content;
	
	
	
	public String getGroupId() {
		return groupId;
	}



	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	
	
	
	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



//	public String initStringsForMedia(){
//		String result="{\"filter\":{\"group_id\":\""+this.groupId+"\"},\"mpnews\":{\"media_id\":\""+this.mediaId+"\"},\"msgtype\":\"mpnews\"}";
//		return result;
//	}
	
	/**
	 * 群发图文
	 */
	public String initStringsForMedia(){
		JSONObject object = new JSONObject();
		
		JSONObject jsonFilter = new JSONObject();
		jsonFilter.put("group_id",this.groupId);
		object.put("filter", jsonFilter);
		
		JSONObject jsonText = new JSONObject();
		jsonText.put("media_id",this.mediaId);
		object.put("mpnews", jsonText);
		
		object.put("msgtype","mpnews");
		
		return object.toString();
	}
	
//	public String initStringsForText(){
//		String result="{\"filter\":{\"group_id\":\""+this.groupId+"\"},\"text\":{\"content\":\""+this.content+"\"},\"msgtype\":\"text\"}";
//		return result;
//	}
	
	/**
	 * 群发文字
	 * @param userList
	 * @return
	 */
	public String initStringsForText(){
		JSONObject object = new JSONObject();
		
		JSONObject jsonFilter = new JSONObject();
		jsonFilter.put("group_id",this.groupId);
		object.put("filter", jsonFilter);
		
		JSONObject jsonText = new JSONObject();
		jsonText.put("content",this.content);
		object.put("text", jsonText);
		
		object.put("msgtype","text");
		
		return object.toString();
	}
	//=========================================================================================================================
	
	/**
	 * 群发图文
	 * @param userList openid str list
	 * @return
	 */
	public String initStringForMediaByUserList(String userList){
		String result="{\"touser\":["+userList+"],\"mpnews\":{\"media_id\":\""+this.mediaId+"\"},\"msgtype\":\"mpnews\"}";
		return result;
	}

	/**
	 * 群发图文
	 * @param userList
	 * @return
	 */
	public String initStringForMediaByUserList(List<String> userList){
		JSONObject object = new JSONObject();
		object.put("touser", JSONArray.fromObject(userList));
		
		JSONObject textObject = new JSONObject();
		textObject.put("media_id",this.mediaId);
		
		object.put("mpnews", textObject);
		object.put("msgtype","mpnews");
		
		return object.toString();
	}
	//=========================================================================================================================
	
	/**
	 * 获得群发text内容
	 * @param userList openid str list
	 * @return
	 */
	public String initStringForTextByUserList(String userList){
		return "{\"touser\":["+userList+"],\"text\":{\"content\":\""+this.content+"\"},\"msgtype\":\"text\"}";
	}
	
	/**
	 * 获得群发text内容
	 * @param userList 列表
	 * @return
	 */
	public String initStringForTextByUserList(List<String> userList){
		JSONObject object = new JSONObject();
		object.put("touser", JSONArray.fromObject(userList));
		
		JSONObject textObject = new JSONObject();
		textObject.put("content",this.content);
		
		object.put("text", textObject);
		object.put("msgtype","text");
		
		return object.toString();
	}
	
	//=========================================================================================================================
	/**
	 * 按人员列表群发，群发图片
	 * @param userList openid str list
	 * @return
	 */
	public String initStringForPictureByUserList(String userList){
		return "{\"touser\":["+userList+"],\"image\":{\"media_id\":\""+this.mediaId+"\"},\"msgtype\":\"image\"}";
	}
	
	/**
	 * 按人员列表群发，群发图片
	 * @param userList
	 * @return
	 */
	public String initStringForPictureByUserList(List<String> userList){
		JSONObject object = new JSONObject();
		object.put("touser", userList);
		
		JSONObject mediaObject = new JSONObject();
		mediaObject.put("media_id",this.mediaId);
		
		object.put("image", mediaObject);
		object.put("msgtype","image");
		
		return object.toString();
	}
	
	
	//=========================================================================================================================
	/**
	 * 群发多媒体文件
	 * @param userList openid str list
	 * @param fileType 分别有图片image、语音（voice）、视频（video）
	 * @return
	 */
	public String initStringForFileByUserList(String userList,String fileType){
		return "{\"touser\":["+userList+"],\""+fileType+"\":{\"media_id\":\""+this.mediaId+"\"},\"msgtype\":\"image\"}";
	}
	
	/**
	 * 群发多媒体文件
	 * @param userList
	 * @param fileType 分别有图片image、语音（voice）、视频（video）
	 * @return
	 */
	public String initStringForFileByUserList(List<String> userList,String fileType){
		JSONObject object = new JSONObject();
		object.put("touser", JSONArray.fromObject(userList));
		
		JSONObject mediaObject = new JSONObject();
		mediaObject.put("media_id",this.mediaId);
		
		object.put(fileType, mediaObject);
		object.put("msgtype",fileType);
		return object.toString();
	}
}
