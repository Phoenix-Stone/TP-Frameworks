package framework.qyweixin.response;

import java.util.List;
import java.util.Map;


/**
 * 被动响应微信图文信息
 * @author Administrator
 */
public class ResNews extends ResWeixin{
	private List<ResArticle> listArticle;
	
	public ResNews(Map<String, Object> map) {
		super(map, "news");
	}
	
	public List<ResArticle> getListArticle() {
		return listArticle;
	}

	public void setListArticle(List<ResArticle> listArticle) {
		this.listArticle = listArticle;
	}

	public String buildXml() {
		StringBuffer buildXml = new StringBuffer();
		
		buildXml.append("<ArticleCount>"+listArticle.size()+"</ArticleCount>");
		buildXml.append("<Articles>");
		if(listArticle.size()>0){
			for(ResArticle article:listArticle){
				buildXml.append("<item>");
				buildXml.append("<Title><![CDATA["+article.getTitle()+"]]></Title> ");
				buildXml.append("<Description><![CDATA["+article.getDescription()+"]]></Description>");
				buildXml.append("<PicUrl><![CDATA["+article.getPicUrl()+"]]></PicUrl>");
				buildXml.append("<Url><![CDATA["+article.getUrl()+"]]></Url>");
				buildXml.append("</item>");
			}
		}
		buildXml.append("</Articles>");
		
		return super.buildXml(buildXml.toString());
	}
}