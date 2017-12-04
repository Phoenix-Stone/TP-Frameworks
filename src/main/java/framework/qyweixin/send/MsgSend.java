/**
 * 消息发送
 * @author zhuhongchao
 * @date 2017-03-27
 */
package framework.qyweixin.send;

import java.util.List;

import net.sf.json.JSONArray;

public class MsgSend {
	public String touser = "@all";
	public String toparty;
	public String totag;
	public Integer agentid;
	public Integer safe = 0;

	public MsgSend() {
	}

	public MsgSend(List<String> touserList, Integer agentid) {
		super();
		this.touser = dealString(touserList);
		this.agentid = agentid;
	}

	public MsgSend(List<String> touserList, List<String> topartyList,List<String> totagList, Integer agentid,Integer safe) {
		super();
		this.touser = dealString(touserList);
		this.toparty = dealString(topartyList);
		this.totag = dealString(totagList);
		this.agentid = agentid;
		this.safe = safe;
	}

	public MsgSend(String touser) {
		this.touser = touser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(List<String> touserList) {
		this.touser = dealString(touserList);
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(List<String> topartyList) {
		this.toparty = dealString(topartyList);
	}

	public String getTotag() {
		return totag;
	}

	public void setTotag(List<String> totagList) {
		this.totag = dealString(totagList);
	}

	public Integer getAgentid() {
		return agentid;
	}

	public void setAgentid(Integer agentid) {
		this.agentid = agentid;
	}

	public Integer getSafe() {
		return safe;
	}

	public void setSafe(Integer safe) {
		this.safe = safe;
	}

	/**
	 * 处理字串组
	 * 
	 * @param array
	 * @return
	 */
	private String dealString(List<String> array) {
		StringBuffer sb = new StringBuffer();

		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				if (i != 0) {
					sb.append("|");
				}

				sb.append(array.get(i));
			}
		}

		return sb.toString();
	}

	/**
	 * 文字信息
	 * 
	 * @param content
	 * @return
	 */
	public String buildTextStr(String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"" + getTouser() + "\",");
		sb.append("\"toparty\":\"" + getToparty() + "\",");
		sb.append("\"totag\":\"" + getTotag() + "\",");
		sb.append("\"msgtype\":\"text\",");
		sb.append("\"agentid\":\"" + getAgentid() + "\",");

		sb.append("\"text\":{\"content\":\"" + content + "\"},");
		sb.append("\"safe\":\"" + safe + "\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 图片信息
	 * 
	 * @param media_id
	 * @return
	 */
	public String buildImageStr(String media_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"" + getTouser() + "\",");
		sb.append("\"toparty\":\"" + getToparty() + "\",");
		sb.append("\"totag\":\"" + getTotag() + "\",");
		sb.append("\"msgtype\":\"image\",");
		sb.append("\"agentid\":\"" + getAgentid() + "\",");

		sb.append("\"image\":{\"media_id\":\"" + media_id + "\"},");
		sb.append("\"safe\":\"" + safe + "\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 声音信息
	 * 
	 * @param media_id
	 * @return
	 */
	public String buildVoiceStr(String media_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"" + getTouser() + "\",");
		sb.append("\"toparty\":\"" + getToparty() + "\",");
		sb.append("\"totag\":\"" + getTotag() + "\",");
		sb.append("\"msgtype\":\"voice\",");
		sb.append("\"agentid\":\"" + getAgentid() + "\",");

		sb.append("\"voice\":{\"media_id\":\"" + media_id + "\"},");
		sb.append("\"safe\":\"" + safe + "\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 视频信息
	 * 
	 * @param media_id
	 * @return
	 */
	public String buildVideoStr(String media_id, String title,
			String description) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"" + getTouser() + "\",");
		sb.append("\"toparty\":\"" + getToparty() + "\",");
		sb.append("\"totag\":\"" + getTotag() + "\",");
		sb.append("\"msgtype\":\"video\",");
		sb.append("\"agentid\":\"" + getAgentid() + "\",");

		sb.append("\"video\":{\"media_id\":\"" + media_id + "\",\"title\": \""
				+ title + "\",\"description\": \"" + description + "\"},");
		sb.append("\"safe\":\"" + safe + "\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 文件信息
	 * 
	 * @param title
	 * @param description
	 * @param musicurl
	 * @param hqmusicurl
	 * @param thumb_media_id
	 * @return
	 */
	public String buildFileStr(String media_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"" + getTouser() + "\",");
		sb.append("\"toparty\":\"" + getToparty() + "\",");
		sb.append("\"totag\":\"" + getTotag() + "\",");
		sb.append("\"msgtype\":\"file\",");
		sb.append("\"agentid\":\"" + getAgentid() + "\",");

		sb.append("\"file\":{\"media_id\":\"" + media_id + "\"},");
		sb.append("\"safe\":\"" + safe + "\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 图文信息
	 * 
	 * @param listArticle
	 * @return
	 */
	public String buildNewsStr(List<MsgArticle> listArticle) {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"touser\":\"" + getTouser() + "\",");
		sb.append("\"toparty\":\"" + getToparty() + "\",");
		sb.append("\"totag\":\"" + getTotag() + "\",");
		sb.append("\"msgtype\":\"news\",");
		sb.append("\"agentid\":\"" + getAgentid() + "\",");
		sb.append("\"news\":{\"articles\":"+ JSONArray.fromObject(listArticle) + "},");
		sb.append("\"safe\":\"" + safe + "\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * mpnews消息
	 * 
	 * @param listArticle
	 * @return
	 */
	public String buildMpNewsStr(List<MsgMpArticle> listArticle) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"" + getTouser() + "\",");
		sb.append("\"toparty\":\"" + getToparty() + "\",");
		sb.append("\"totag\":\"" + getTotag() + "\",");
		sb.append("\"msgtype\":\"mpnews\",");
		sb.append("\"agentid\":\"" + getAgentid() + "\",");
		sb.append("\"mpnews\":{\"articles\":"+JSONArray.fromObject(listArticle) + "},");
		sb.append("\"safe\":\"" + safe + "\"");
		sb.append("}");
		return sb.toString();
	}
}