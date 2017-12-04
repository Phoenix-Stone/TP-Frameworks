package framework.weixin.send;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 批量发送图文
 * @author shixiaolong
 * @date2014-8-11
 */
public class BatchSendArticleInit {
	public List<BatchSendArticleOne> list;

	public List<BatchSendArticleOne> getList() {
		return list;
	}

	public void setList(List<BatchSendArticleOne> list) {
		this.list = list;
	}
	
	public String buildReturn() {
//		String var ="{\"articles\": [";
//		for (int i = 0; i < list.size(); i++) {
//			BatchSendArticleOne one = list.get(i);
//			if(i==0){
//				var+="{\"thumb_media_id\":\""+one.getThumb_media_id()+"\",";
//				var+="\"author\":\""+one.getAuthor()+"\",";
//				var+="\"title\":\""+one.getTitle()+"\",";
//				var+="\"content_source_url\":\""+one.getContent_source_url()+"\",";
//				var+="\"content\":\""+one.getContent()+"\",";
//				var+="\"digest\":\""+one.getDigest()+"\",";
//				var+="\"show_cover_pic\":\""+one.getShow_cover_pic()+"\"}";
//			}else{
//				var+=",{\"thumb_media_id\":\""+one.getThumb_media_id()+"\",";
//				var+="\"author\":\""+one.getAuthor()+"\",";
//				var+="\"title\":\""+one.getTitle()+"\",";
//				var+="\"content_source_url\":\""+one.getContent_source_url()+"\",";
//				var+="\"content\":\""+one.getContent()+"\",";
//				var+="\"digest\":\""+one.getDigest()+"\",";
//				var+="\"show_cover_pic\":\""+one.getShow_cover_pic()+"\"}";
//			}
//		}
//		var+="]}";
		
		JSONObject varJSONObject = new JSONObject();
		JSONArray varJSONArray = new JSONArray();
		
		for (int i = 0; i < list.size(); i++) {
			BatchSendArticleOne one = list.get(i);
			JSONObject tempJSONObject = new JSONObject();
			tempJSONObject.put("thumb_media_id", one.getThumb_media_id());
			tempJSONObject.put("author", one.getAuthor());
			tempJSONObject.put("title", one.getTitle());
			tempJSONObject.put("content_source_url", one.getContent_source_url());
			tempJSONObject.put("content", one.getContent());
			tempJSONObject.put("digest", one.getDigest());
			tempJSONObject.put("show_cover_pic", one.getShow_cover_pic());
			varJSONArray.add(tempJSONObject);
		}
		
		varJSONObject.put("articles", varJSONArray);
		
		return varJSONObject.toString();
	}
}
