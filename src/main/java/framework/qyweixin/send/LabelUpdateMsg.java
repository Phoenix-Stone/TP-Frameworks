package framework.qyweixin.send;

/**
 * 更新标签
 * @author Administrator
 * @date2014-12-17
 */
public class LabelUpdateMsg {
	public String tagname;
	public Integer tagid;
	
	public LabelUpdateMsg(String tagname,Integer tagid) {
		this.tagname=tagname;
		this.tagid=tagid;
	}
	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		return "{\"tagid\": \""+tagid+"\",\"tagname\": \""+tagname+"\"}";
	}
}
