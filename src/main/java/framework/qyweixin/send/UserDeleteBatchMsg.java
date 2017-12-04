package framework.qyweixin.send;

/**
 * 批量删除
 * @author Administrator
 * @date2014-12-17
 */
public class UserDeleteBatchMsg {

//	{
//		   "useridlist": ["zhangsan", "lisi"]
//		}	
	
	public String[] useridlist;
	
	public UserDeleteBatchMsg(String[] useridlist) {
		this.useridlist = useridlist;
	}
	
	/**
	 * @param content
	 * @return
	 */
	public String buildReturn() {
		String listString = "[";
		for (int i = 0; i < useridlist.length; i++) {
			if(i==0){
				listString+="\""+useridlist[i]+"\"";
			}else{
				listString+=",\""+useridlist[i]+"\"";
			}
		}
		listString+="]";
		return "{\"useridlist\": "+listString+"}";
	}
}
