package framework.weixin.util;

import java.util.List;

/**
 * 微信用户信息
 */
public class WeixinUser {

	/** 用户是否订阅该公众号标识 1--已关注 0--未关注 **/
	private Integer subscribe;

	/** 用户的标识，对当前公众号唯一 **/
	private String openid;

	/** 用户的昵称 **/
	private String nickname;

	/** 用户的性别，1--男性，2--女性，0--未知 **/
	private Integer sex;

	/** 用户所在城市 **/
	private String city;

	/** 用户所在国家 **/
	private String country;

	/** 用户所在省份 **/
	private String province;

	/** 用户的语言，简体中文为zh_CN **/
	private String language;

	/** 用户头像 **/
	private String headimgurl;

	/** 用户关注时间,为时间戳 **/
	private Long subscribe_time;

	/** 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。 **/
	private String unionid;

	private String remark;

	/** 用户所在的分组ID **/
	private Integer groupid;

	/** 用户被打上的标签ID列表 **/
	private String tagid_list;
	/**用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）**/
	private List<String> privilege;

	public WeixinUser() {
	}

	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Long getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(Long subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public String getTagid_list() {
		return tagid_list;
	}

	public void setTagid_list(String tagid_list) {
		this.tagid_list = tagid_list;
	}

	public List<String> getPrivilege() {
		return privilege;
	}

	public void setPrivilege(List<String> privilege) {
		this.privilege = privilege;
	}

}