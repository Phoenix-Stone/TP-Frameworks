 
  
  
 
 
 

package framework.weixin.send;

import java.util.List;

import net.sf.json.JSONArray;

public class MsgSend {
	public String touser;
	
	public MsgSend() {
	}

	public MsgSend(String touser) {
		this.touser = touser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}
	
	/**
	 * 文字信息
	 * @param content
	 * @return
	 */
	public String buildTextStr(String content) {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\""+getTouser()+"\",\"msgtype\":\"text\",");
		sb.append("\"text\":{\"content\":\""+content+"\"}");
		sb.append("}");
		
		return sb.toString();
	}
	
	/**
	 * 图片信息
	 * @param media_id
	 * @return
	 */
	public String buildImageStr(String media_id) {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\""+getTouser()+"\",\"msgtype\":\"image\",");
		sb.append("\"image\":{\"media_id\":\""+media_id+"\"}");
		sb.append("}");
		
		return sb.toString();
	}
	
	/**
	 * 声音信息
	 * @param media_id
	 * @return
	 */
	public String buildVoiceStr(String media_id) {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\""+getTouser()+"\",\"msgtype\":\"voice\",");
		sb.append("\"voice\":{\"media_id\":\""+media_id+"\"}");
		sb.append("}");
		
		return sb.toString();
	}
	
	/**
	 * 视频信息
	 * @param media_id
	 * @return
	 */
	public String buildVideoStr(String media_id) {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\""+getTouser()+"\",\"msgtype\":\"video\",");
		sb.append("\"video\":{\"media_id\":\""+media_id+"\"}");
		sb.append("}");
		
		return sb.toString();
	}
	
	/**
	 * 音乐信息
	 * @param title
	 * @param description
	 * @param musicurl
	 * @param hqmusicurl
	 * @param thumb_media_id
	 * @return
	 */
	public String buildMusicStr(String title, String description, String musicurl, String hqmusicurl, String thumb_media_id) {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\""+getTouser()+"\",\"msgtype\":\"music\",");
		sb.append("\"music\":{\"title\":\""+title+"\", \"description\":\""+description+"\", \"musicurl\":\""+musicurl+"\", \"hqmusicurl\":\""+hqmusicurl+"\", \"thumb_media_id\":\""+thumb_media_id+"\"}");
		sb.append("}");
		
		return sb.toString();
	}
	
	/**
	 * 图文信息
	 * @param listArticle
	 * @return
	 */
	public String buildNewsStr(List<MsgArticle> listArticle) {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\""+getTouser()+"\",\"msgtype\":\"news\",");
		sb.append("\"news\":{\"articles\":"+JSONArray.fromObject(listArticle)+"}");
		sb.append("}");
		
		return sb.toString();
	}
}