package framework.qyweixin.send;

/**
 * 创建部门
 * @author Administrator
 * @date2014-12-17
 */
public class PartCreateMsg {
	public String name;
	public int parentid;
	public int order;
	
	public PartCreateMsg(String name,int parentid,int order) {
		this.name=name;
		this.parentid=parentid;
		this.order=order;
	}
	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		return "{\"name\": \""+name+"\",\"parentid\": \""+parentid+"\",\"order\": \""+order+"\"}";
	}
}
