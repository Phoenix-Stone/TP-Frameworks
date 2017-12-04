package framework.qyweixin.send;

/**
 * 更新部门
 * @author Administrator
 * @date2014-12-17
 */
public class PartUpdateMsg {
	public String name;
	public int parentid;
	public int order;
	public int id;
	
	public PartUpdateMsg(int id,String name,int parentid,int order) {
		this.id=id;
		this.name=name;
		this.parentid=parentid;
		this.order=order;
	}
	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		return "{\"id\": \""+id+"\",\"name\": \""+name+"\",\"parentid\": \""+parentid+"\",\"order\": \""+order+"\"}";
	}
}
