package framework.qyweixin.util;

/**
 * 员工信息
 * @author Administrator
 * @date2014-12-17
 */
public class UserBean {
	
//	{
//		   "errcode": 0,
//		   "errmsg": "ok",
//		   "userid": "zhangsan",
//		   "name": "李四",
//		   "department": [1, 2],
//		   "position": "后台工程师",
//		   "mobile": "15913215421",
//		   "email": "zhangsan@gzdev.com",
//		   "weixinid": "lisifordev",  
//		   "avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
//		   "status": 1,
//		   "extattr": {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
//		}
	
	
	public String userid;
	public String name;
	public Integer[] department;
	public String position;
	public String mobile;
	public String email;
	public String weixinid;
	public String avatar;//头像url。注：如果要获取小图将url最后的"/0"改成"/64"即可
	public Integer status;//关注状态: 1=已关注，2=已冻结，4=未关注
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer[] getDepartment() {
		return department;
	}
	public void setDepartment(Integer[] department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeixinid() {
		return weixinid;
	}
	public void setWeixinid(String weixinid) {
		this.weixinid = weixinid;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
