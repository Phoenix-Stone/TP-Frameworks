package framework.weixin.send;


/**
 * 分组修改
 * @author shixiaolong
 * @date2014-7-2
 */
public class GroupInsert {
	public String groupName;
	
	public GroupInsert(String groupName) {
		this.groupName=groupName;
	}

	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		return "{\"group\":{\"name\":\""+groupName+"\"}}";
	}
}