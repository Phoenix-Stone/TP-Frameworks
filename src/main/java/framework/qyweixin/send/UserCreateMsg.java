package framework.qyweixin.send;

/**
 * 创建员工
 * @author Administrator
 * @date2014-12-17
 */
public class UserCreateMsg {
	
//		   "userid": "zhangsan",
//		   "name": "张三",
//		   "department": [1, 2],
//		   "position": "产品经理",
//		   "mobile": "15913215421",
//		   "email": "zhangsan@gzdev.com",
//		   "weixinid": "zhangsan4dev",
//		   "extattr": {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
//	
	
	
	public String userid;
	public String name;
	public Integer[] department;
	public String position;
	public String mobile;
	public String email;
	public String weixinid;
	
	public UserCreateMsg(String userid,String name,Integer[] department,String position,String mobile,String email,String weixinid) {
		this.userid = userid;
		this.name = name;
		this.department = department;
		this.position = position;
		this.mobile = mobile;
		this.email = email;
		this.weixinid = weixinid;
	}
	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		String departmentString = "[";
		for (int i = 0; i < department.length; i++) {
			if(i==0){
				departmentString+=department[i];
			}else{
				departmentString+=","+department[i];
			}
		}
		departmentString+="]";
		return "{\"userid\": \""+userid+"\",\"name\": \""+name+"\",\"department\": "+departmentString+"," +
				"\"position\": \""+position+"\",\"mobile\": \""+mobile+"\"," +
				"\"email\": \""+email+"\",\"weixinid\": \""+weixinid+"\"," +
						"\"extattr\": {\"attrs\":[{\"name\":\"爱好\",\"value\":\"旅游\"},{\"name\":\"卡号\",\"value\":\"1234567234\"}]}}";
	}
}
