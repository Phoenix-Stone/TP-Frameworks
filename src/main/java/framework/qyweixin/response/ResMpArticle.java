package framework.qyweixin.response;

/**
 * 被动响应微信MP图文信息
 * @author Administrator
 */
public class ResMpArticle {
	private String Title; //图文消息的标题  
	private String Thumb_media_id;//图文消息缩略图的media_id, 可以在上传多媒体文件接口中获得。此处thumb_media_id即上传接口返回的media_id 
	private String Author;//图文消息的作者 
	private String Content_source_url;//图文消息点击“阅读原文”之后的页面链接 
	private String Content;//图文消息的内容，支持html标签 
	private String Digest;//图文消息的描述 
	private String Show_cover_pic;//是否显示封面，1为显示，0为不显示 

	public ResMpArticle() {
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getThumb_media_id() {
		return Thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		Thumb_media_id = thumb_media_id;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getContent_source_url() {
		return Content_source_url;
	}

	public void setContent_source_url(String content_source_url) {
		Content_source_url = content_source_url;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getDigest() {
		return Digest;
	}

	public void setDigest(String digest) {
		Digest = digest;
	}

	public String getShow_cover_pic() {
		return Show_cover_pic;
	}

	public void setShow_cover_pic(String show_cover_pic) {
		Show_cover_pic = show_cover_pic;
	}

}