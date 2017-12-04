package framework.qyweixin.send;

/**
 * 向标签添加人员
 * 
 * @author Administrator
 * @date2014-12-17
 */
public class LabelUserMsg {

	// {
	// "tagid": "1",
	// "userlist":[ "user1","user2"],
	// "partylist": [4]
	// }

	public Integer tagid;
	/* 注意！！！！！！！！！！！！！！以下两个参数必须填写一个，可以填写一个 */
	public String[] userlist;
	public Integer[] partylist;
	public int useType;// 1是用用户列表，2是用部门列表

	public LabelUserMsg(Integer tagid, String[] userlist, Integer[] partylist,
			int useType) {
		this.tagid = tagid;
		this.userlist = userlist;
		this.partylist = partylist;
		this.useType = useType;
	}

	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {

		if (useType == 1) {
			String userlistString = "[";
			for (int i = 0; i < userlist.length; i++) {
				if (i == 0) {
					userlistString += "\"" + userlist[i] + "\"";
				} else {
					userlistString += ",\"" + userlist[i] + "\"";
				}
			}
			userlistString += "]";
			return "{\"tagid\": \"" + tagid + "\",\"userlist\":"
					+ userlistString + ",\"partylist\": []}";
		} else {
			String partylistString = "[";
			for (int i = 0; i < partylist.length; i++) {
				if (i == 0) {
					partylistString += partylist[i];
				} else {
					partylistString += "," + partylist[i];
				}
			}
			partylistString += "]";
			return "{\"tagid\": \"" + tagid
					+ "\",\"userlist\":[ ],\"partylist\":" + partylistString
					+ "}";
		}

	}
}
