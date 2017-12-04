package framework.weixin.send;


/**
 * 分组修改
 * @author shixiaolong
 * @date2014-7-2
 */
public class GroupModify {
	public Integer id;
	public String name;
	
	public GroupModify(Integer id,String name) {
		this.id=id;
		this.name=name;
	}

	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		return "{\"group\":{\"id\":"+id+",\"name\":\""+name+"\"}}";
	}
	
	
}