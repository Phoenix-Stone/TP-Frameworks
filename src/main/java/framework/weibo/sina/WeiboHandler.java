package framework.weibo.sina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import framework.weibo.sina.util.ImageItem;
import framework.weibo.sina.util.PostParameter;
import framework.weibo.sina.util.URLEncodeUtils;
/**
 * @Description 新浪微博接口封装类
 * @author Linfeng
 * @date 2016-7-5 上午10:28:49
 * 
 **/
public class WeiboHandler implements Serializable{
	private static final long serialVersionUID = -4810201022574993770L;
	/** 调用微博接口的地址 */
	private static final String API_BASE_URL = "https://api.weibo.com/2/";
	/** 微博OAuth2地址 */
	private static final String OAUTH2_URL = "https://api.weibo.com/oauth2/";
	private static HttpClient client = new HttpClient();
	private String accessToken;//采用OAuth授权方式为必填参数，OAuth授权后获得。
	
	public WeiboHandler(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * OAuth授权之后，获取授权用户的UID
	 * 
	 * 必填：accessToken
	 * 
	 * @return
	 */
	public JSONObject accountGetUId(){
		String url = API_BASE_URL + "account/get_uid.json";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("access_token", accessToken));
		return excPost(url, postParameterList);
	}
	
	
	
	/**
	 * 根据accessToken获取用户信息
	 * 必填：accessToken。uid与screen_name二者必选其一，且只能选其一
	 * 
	 * @param uId 需要查询的用户ID
	 * @param screenName 需要查询的用户昵称
	 * @return
	 */
	public JSONObject usersShow(String uId, String screenName){
		String url = API_BASE_URL + "users/show.json";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("access_token", accessToken));
		if(StringUtils.isNotBlank(uId)){
			postParameterList.add(new PostParameter("uid", uId));
		}
		if(StringUtils.isNotBlank(screenName)){
			postParameterList.add(new PostParameter("screen_name", screenName));
		}
		return excGet(url, postParameterList);
	}
	
	/**
	 * 根据uid获取用户信息
	 * 必填：uId
	 * 
	 * @param uId
	 * @return
	 */
	public JSONObject usersShow(String uId){
		String url = API_BASE_URL + "users/show.json";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("uid", uId));
		return excPost(url, postParameterList);
	}

	/**
	 * 发布一条新微博。必填：accessToken、status
	 * 
	 * @param status 要发布的微博文本内容，必须做URLencode，内容不超过140个汉字。
	 * @param visible 微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0。
	 * @param listId 微博的保护投递指定分组ID，只有当visible参数为3时生效且必选。
	 * @param latitude 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
	 * @param longitude 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
	 * @param annotations 元数据，主要是为了方便第三方应用记录一些适合于自己使用的信息，每条微博可以包含一个或者多个元数据，必须以json字串的形式提交，字串长度不超过512个字符，具体内容可以自定。
	 * @param rip 开发者上报的操作用户真实IP，形如：211.156.0.1。
	 * @return
	 */
	public JSONObject statusesUpdate(String status, Integer visible, String listId, Float latitude, Float longitude, String annotations, String rip){
		String url = API_BASE_URL + "statuses/update.json";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("access_token", accessToken));
		postParameterList.add(new PostParameter("status", status));
		if(visible != null){
			postParameterList.add(new PostParameter("visible", visible));
		}
		if(StringUtils.isNotBlank(listId)){
			postParameterList.add(new PostParameter("list_id", listId));
		}
		if(latitude != null){
			postParameterList.add(new PostParameter("lat", latitude));
		}
		if(longitude != null){
			postParameterList.add(new PostParameter("long", longitude));
		}
		if(StringUtils.isNotBlank(annotations)){
			postParameterList.add(new PostParameter("annotations", annotations));
		}
		if(StringUtils.isNotBlank(rip)){
			postParameterList.add(new PostParameter("rip", rip));
		}
		return excPost(url, postParameterList);
	}
	
	/**
	 * 上传图片并发布一条新微博
	 * 
	 * 必填：accessToken
	 * 
	 * @param status 要发布的微博文本内容，必须做URLencode，内容不超过140个汉字。
	 * @param pic 要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M。
	 * @return
	 */
	public JSONObject statusesUpload(String status, byte[] pic){
		String url = API_BASE_URL + "statuses/upload.json";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("access_token", accessToken));
		postParameterList.add(new PostParameter("status", status));
		ImageItem imageItem = new ImageItem("pic", pic);
		
		return excPost(url, postParameterList, imageItem);
	}
	
	/**
	 * 指定个图片URL地址抓取后上传并同时发布一条新微博。必填：accessToken
	 * 
	 * @param status 要发布的微博文本内容，必须做URLencode，内容不超过140个汉字。
	 * @param visible 微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0。
	 * @param listId 微博的保护投递指定分组ID，只有当visible参数为3时生效且必选。
	 * @param picUrl 图片的URL地址，必须以http开头。
	 * @param picId 已经上传的图片pid，多个时使用英文半角逗号符分隔，最多不超过9个。
	 * @param latitude 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
	 * @param longitude 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
	 * @param annotations 元数据，主要是为了方便第三方应用记录一些适合于自己使用的信息，每条微博可以包含一个或者多个元数据，必须以json字串的形式提交，字串长度不超过512个字符，具体内容可以自定。
	 * @param rip 开发者上报的操作用户真实IP，形如：211.156.0.1。
	 * @return
	 */
	public JSONObject statusesUploadUrlText(String status, Integer visible, String listId, String picUrl, String picId, Float latitude, Float longitude, String annotations, String rip){
		String url = API_BASE_URL + "statuses/upload_url_text.json";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("access_token", accessToken));
		postParameterList.add(new PostParameter("status", status));
		if(visible != null){
			postParameterList.add(new PostParameter("visible", visible));
		}
		if(StringUtils.isNotBlank(listId)){
			postParameterList.add(new PostParameter("list_id", listId));
		}
		if(StringUtils.isNotBlank(picUrl)){
			postParameterList.add(new PostParameter("url", picUrl));
		}
		if(StringUtils.isNotBlank(picId)){
			postParameterList.add(new PostParameter("pic_id", picId));
		}
		if(latitude != null){
			postParameterList.add(new PostParameter("lat", latitude));
		}
		if(longitude != null){
			postParameterList.add(new PostParameter("long", longitude));
		}
		if(StringUtils.isNotBlank(annotations)){
			postParameterList.add(new PostParameter("annotations", annotations));
		}
		if(StringUtils.isNotBlank(rip)){
			postParameterList.add(new PostParameter("rip", rip));
		}
		return excPost(url, postParameterList);
	}
	/**
	 * 授权第一步。获取授权地址
	 * OAuth2的authorize接口。必填：appKey、redirectURI
	 * 
	 * @param appKey 申请应用时分配的AppKey。
	 * @param redirectURI 授权回调地址，站外应用需与设置的回调地址一致，站内应用需填写canvas page的地址。
	 * @param scope 申请scope权限所需参数，可一次申请多个scope权限，用逗号分隔。
	 * @param state 用于保持请求和回调的状态，在回调时，会在Query Parameter中回传该参数。开发者可以用这个参数验证请求有效性，也可以记录用户请求授权页前的位置。这个参数可用于防止跨站请求伪造（CSRF）攻击
	 * @param display 授权页面的终端类型，default-默认的授权页面，适用于web浏览器；mobile-移动终端的授权页面，适用于支持html5的手机；wap-wap版授权页面，适用于非智能手机；client-客户端版本授权页面，适用于PC桌面应用；apponweibo-默认的站内应用授权页，授权后不返回access_token，只刷新站内应用父框架。
	 * @param forcelogin 是否强制用户重新登录，true：是，false：否。默认false。
	 * @return
	 */
	public static String oauth2Authorize(String appKey, String redirectURI, String scope, String state, String display, Boolean forcelogin){
		StringBuilder oauthURL = new StringBuilder();
		oauthURL.append(OAUTH2_URL + "authorize");
		oauthURL.append("?client_id=").append(appKey);
		oauthURL.append("&redirect_uri=").append(redirectURI);
		if(StringUtils.isNotBlank(scope)){
			oauthURL.append("&scope=").append(scope);
		}
		if(StringUtils.isNotBlank(state)){
			oauthURL.append("&state=").append(state);
		}
		if(forcelogin != null){
			oauthURL.append("&forcelogin=").append(forcelogin);
		}
		return oauthURL.toString();
	}
	
	/**
	 * 授权第二步。根据code获取accessToken
	 * @param appKey 申请应用时分配的AppKey。
	 * @param appSecret 申请应用时分配的AppSecret。
	 * @param code 调用authorize获得的code值。
	 * @param redirectURI 回调地址，需需与注册应用里的回调地址一致。
	 * @return
	 */
	public static JSONObject oauth2AccessToken(String appKey, String appSecret, String code, String redirectURI){
		String url = OAUTH2_URL + "access_token";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("client_id", appKey));
		postParameterList.add(new PostParameter("client_secret", appSecret));
		postParameterList.add(new PostParameter("grant_type", "authorization_code"));
		postParameterList.add(new PostParameter("code", code));
		postParameterList.add(new PostParameter("redirect_uri", redirectURI));
		return excPost(url, postParameterList);
	}
	
	/**
	 * 微博JSSDK的js_ticket
	 * 
	 * @param appKey 申请应用时分配的AppKey。
	 * @param appSecret 申请应用时分配的AppSecret。
	 * @return
	 */
	public static JSONObject oauth2GetJsTicket(String appKey, String appSecret){
		String url = OAUTH2_URL + "js_ticket/generate";
		List<PostParameter> postParameterList = new ArrayList<PostParameter>();
		postParameterList.add(new PostParameter("client_id", appKey));
		postParameterList.add(new PostParameter("client_secret", appSecret));
		return excPost(url, postParameterList);
	}
	
	/**
	 * GET请求提交
	 * 
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public JSONObject excGet(String url, List<PostParameter> params){
		if (null != params && params.size() > 0) {
			String encodedParams = encodeParameters(params.toArray(new PostParameter[params.size()]));
			if (-1 == url.indexOf("?")) {
				url += "?" + encodedParams;
			} else {
				url += "&" + encodedParams;
			}
		}
		GetMethod getmethod = new GetMethod(url);
		return httpRequest(getmethod);
	}
	
	private JSONObject httpRequest(HttpMethod method){
		JSONObject json = new JSONObject();
		try {
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(3, false));
			int status = client.executeMethod(method);
			BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),"utf-8"));  
			StringBuffer sb = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
				sb.append(str);  
			}
			String respStr = sb.toString();
			//System.out.println("微博API返回："+respStr);
			if (status == HttpStatus.SC_OK) {
				json = JSONObject.fromObject(respStr);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return json;
	}
	
	private static JSONObject excPost(String url, List<PostParameter> params) {
		JSONObject json = new JSONObject();
		try {
			PostMethod postMethod = new PostMethod(url);
			for (int i = 0; i < params.size(); i++) {
				postMethod.addParameter(params.get(i).getName(), params.get(i).getValue());
			}
			
			postMethod.getParams().setContentCharset("UTF-8");
			//System.out.println("微博API请求："+url+"\n参数："+ params);
			int status = client.executeMethod(postMethod);
			BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"utf-8"));  
			StringBuffer sb = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
				sb.append(str);  
			}
			String respStr = sb.toString();
			//System.out.println("微博API返回："+respStr);
			if (status == HttpStatus.SC_OK) {
				json = JSONObject.fromObject(respStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * post请求。带图片上传
	 * 
	 * @param url
	 * @param params
	 * @param imageItem
	 * @return
	 */
	private static JSONObject excPost(String url, List<PostParameter> params, ImageItem imageItem){
		JSONObject json = new JSONObject();
		try {
			PostMethod postMethod = new PostMethod(url);
			Part[] parts = null;
			if (params == null) {
				parts = new Part[1];
			} else {
				parts = new Part[params.size() + 1];
			}
			if (params != null) {
				int i = 0;
				for (PostParameter entry : params) {
					parts[i++] = new StringPart(entry.getName(), (String) entry.getValue());
				}
			}
			parts[parts.length - 1] = new ByteArrayPart(imageItem.getContent(), imageItem.getName(), imageItem.getContentType());
			postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
			
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(3, false));
			int status = client.executeMethod(postMethod);
			BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"utf-8"));  
			StringBuffer sb = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
				sb.append(str);  
			}
			String respStr = sb.toString();
			//System.out.println("微博API返回：{}"+respStr);
			if (status == HttpStatus.SC_OK) {
				json = JSONObject.fromObject(respStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return json;
	}
	
	
	/**
	 * 对parameters进行encode处理
	 * 
	 * @param postParams
	 * @return
	 */
	private static String encodeParameters(PostParameter[] postParams) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < postParams.length; j++) {
			if (j != 0) {
				buf.append("&");
			}
			buf.append(URLEncodeUtils.encodeURL(postParams[j].getName()))
			.append("=")
			.append(URLEncodeUtils.encodeURL(postParams[j].getValue()));
		}
		return buf.toString();
	}
	
	private static class ByteArrayPart extends PartBase {
		private byte[] mData;
		private String mName;

		public ByteArrayPart(byte[] data, String name, String type)
				throws IOException {
			super(name, type, "UTF-8", "binary");
			mName = name;
			mData = data;
		}

		protected void sendData(OutputStream out) throws IOException {
			out.write(mData);
		}

		protected long lengthOfData() throws IOException {
			return mData.length;
		}

		protected void sendDispositionHeader(OutputStream out)
				throws IOException {
			super.sendDispositionHeader(out);
			StringBuilder buf = new StringBuilder();
			buf.append("; filename=\"").append(mName).append("\"");
			out.write(buf.toString().getBytes());
		}

	}

}
