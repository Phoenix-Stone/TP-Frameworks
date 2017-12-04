package framework.qyweixin.send;

/**
 * 创建标签
 * @author Administrator
 * @date2014-12-17
 */
public class LabelCreateMsg {
	public String tagname;
	
	public LabelCreateMsg(String tagname) {
		this.tagname=tagname;
	}
	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		return "{\"tagname\": \""+tagname+"\"}";
	}
}
