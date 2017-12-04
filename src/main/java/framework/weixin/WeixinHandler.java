
package framework.weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.lanswon.entity.dto.BaseDto;
//import com.lanswon.util.JacksonUtil;

import framework.qyweixin.util.UploadMediaBean;
import framework.weixin.util.FileUtil;
import framework.weixin.util.MySecureProtocolSocketFactory;
import framework.weixin.util.WeixinMsg;
import framework.weixin.util.WeixinUser;

/**
 * 寰俊鍏紬鍙锋帴鍙ｅ皝瑁�
 * 
 * @author zhuhongchao
 * @date 2017-03-23
 */
public class WeixinHandler {
	private String WEIXIN_AppId;
	private String WEIXIN_AppSecret;
	private HttpClient client = new HttpClient();
	private WeixinMsg weixinMsg = new WeixinMsg();
	private String accessToken = null;
	public static final String mphelpOpenid = "oR5Gjjl_eiZoUpGozMo7dbBJ362A"; // 寰俊鍔╂墜openId
	public static Logger logger = LoggerFactory.getLogger("WEIXIN_LOG");

	/**
	 * 鏋勯�犳柟娉曞垵濮嬪寲寰俊appid appsecret
	 * 
	 * @param appId
	 * @param appSecret
	 */
	public WeixinHandler(String appId, String appSecret) {
		this.WEIXIN_AppId = appId;
		this.WEIXIN_AppSecret = appSecret;
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));

		initAccessToken();
	}

	/**
	 * 鏋勯�犳柟娉� 閫氳繃accessToken鏋勫缓寰俊瀵硅薄
	 * 
	 * @param accessToken
	 */
	public WeixinHandler(String accessToken) {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		this.accessToken = accessToken;
	}

	/**
	 * 鐩存帴閫氳繃appId鍜宎ppSecret鑾峰彇accessToken
	 * 
	 * @param appId
	 * @param appSecret
	 * @return 杩斿洖token鍊�
	 */
	public static String getAccessToken(String appId, String appSecret) {
		String accessToken = null;
		HttpClient client = new HttpClient();
		WeixinMsg weixinMsg = new WeixinMsg();

		try {
			ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
			Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
			GetMethod post = new GetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
					+ appId + "&secret=" + appSecret);
			post.setRequestHeader("Connection", "Keep-Alive");
			post.setRequestHeader("Cache-Control", "no-cache");
			int status;
			status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String content = post.getResponseBodyAsString();
				JSONObject json = JSONObject.fromObject(content);

				if (json.get("access_token") != null) {
					accessToken = json.get("access_token").toString();
				} else {
					weixinMsg.setMsg(String.valueOf(json.get("errcode")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return accessToken;
	}

	/**
	 * 鐢熸垚jsapi ticket
	 */
	public String getjsapiTicket() {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
		JSONObject json = excute(url);
		String ticket = null;
		if (json.get("ticket") != null) {
			ticket = json.get("ticket").toString();
		}
		return ticket;
	}

	/**
	 * 閫氳繃ticket鍜屾枃浠惰矾寰勭敓鎴愪簩缁寸爜(鏂囦欢浠icket鍛藉悕)
	 * 
	 * @param ticket
	 * @param path
	 * @return 鐢熸垚浜岀淮鐮佸浘鐗�
	 */
	public String generateQrCodewithTicketName(String ticket, String path, String code) {
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
		String picUrl = null;

		int BUFFER = 1024;
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setContentCharset("utf-8");
		try {
			client.executeMethod(httpGet);

			String newFileName = code + ".jpg";
			String newPath = path + newFileName;
			File file = new File(newPath);
			if (!file.exists()) {
				file.createNewFile();
			}

			InputStream in = httpGet.getResponseBodyAsStream();
			FileOutputStream out = new FileOutputStream(new File(newPath));
			byte[] b = new byte[BUFFER];
			int len = 0;

			StringBuffer fstr = new StringBuffer();
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
				fstr.append(new String(b, 0, len));
			}
			in.close();
			out.close();

			if (fstr.toString().contains("errcode")) {
				JSONObject json = JSONObject.fromObject(fstr.toString());
				weixinMsg.setMsg(String.valueOf(json.get("errcode")));
			} else {
				picUrl = newFileName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}

		return picUrl;
	}

	/**
	 * 閫氳繃鍦烘櫙鍐呭鑾峰緱浜岀淮鐮佸唴瀹�
	 * 
	 * @param content
	 * @return ticket
	 */
	public String getTicket(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
		JSONObject json = excute(url, content);
		String ticket = null;
		if (json.get("ticket") != null) {
			ticket = json.get("ticket").toString();
		}
		return ticket;
	}

	/**
	 * 鍒涘缓鏍囩銆備竴涓叕浼楀彿锛屾渶澶氬彲浠ュ垱寤�100涓爣绛俱��
	 * 
	 * @param tagName
	 *            鏍囩鍚嶏紙30涓瓧绗︿互鍐咃級
	 * @return { "tag":{ "id":134, "name":"骞夸笢" } }
	 */
	public JSONObject tagCreate(String tagName) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=" + accessToken;
		JSONObject tag = new JSONObject();
		tag.put("name", tagName);
		JSONObject content = new JSONObject();
		content.put("tag", tag);
		return exc(url, content.toString());
	}

	/**
	 * 鑾峰彇鍏紬鍙峰凡鍒涘缓鐨勬爣绛�
	 * 
	 * @return { "tags":[{ "id":1, "name":"姣忓ぉ涓�缃愬彲涔愭槦浜�", "count":0 //姝ゆ爣绛句笅绮変笣鏁� },{
	 *         "id":2, "name":"鏄熸爣缁�", "count":0 },{ "id":127, "name":"骞夸笢",
	 *         "count":5 } ] }
	 */
	public JSONObject tagGet() {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + accessToken;
		return excute(url);
	}

	/**
	 * 缂栬緫鏍囩
	 * 
	 * @param weixinTagId
	 * @param tagName
	 * @return { "errcode":0, "errmsg":"ok" }
	 */
	public JSONObject tagUpdate(Long weixinTagId, String tagName) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=" + accessToken;
		JSONObject tag = new JSONObject();
		tag.put("id", weixinTagId);
		tag.put("name", tagName);
		JSONObject content = new JSONObject();
		content.put("tag", tag);
		return exc(url, content.toString());
	}

	/**
	 * 鍒犻櫎鏍囩銆� 璇锋敞鎰忥紝褰撴煇涓爣绛句笅鐨勭矇涓濊秴杩�10w鏃讹紝鍚庡彴涓嶅彲鐩存帴鍒犻櫎鏍囩銆傛鏃讹紝寮�鍙戣�呭彲浠ュ璇ユ爣绛句笅鐨刼penid鍒楄〃锛屽厛杩涜鍙栨秷鏍囩鐨勬搷浣滐紝
	 * 鐩村埌绮変笣鏁颁笉瓒呰繃10w鍚庯紝鎵嶅彲鐩存帴鍒犻櫎璇ユ爣绛俱��
	 * 
	 * @param weixinTagId
	 * @return { "errcode":0, "errmsg":"ok" }
	 */
	public JSONObject tagDelete(Long weixinTagId) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=" + accessToken;
		JSONObject tag = new JSONObject();
		tag.put("id", weixinTagId);
		JSONObject content = new JSONObject();
		content.put("tag", tag);
		return exc(url, content.toString());
	}

	/**
	 * 鑾峰彇鏍囩涓嬬矇涓濆垪琛�
	 * 
	 * @param weixinTagId
	 * @param nextOpenid
	 *            绗竴涓媺鍙栫殑OPENID锛屼笉濉粯璁や粠澶村紑濮嬫媺鍙�
	 * @return { "count":2,//杩欐鑾峰彇鐨勭矇涓濇暟閲� "data":{//绮変笣鍒楄〃 "openid":[
	 *         "ocYxcuAEy30bX0NXmGn4ypqx3tI0", "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y" ]
	 *         },
	 *         "next_openid":"ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"//鎷夊彇鍒楄〃鏈�鍚庝竴涓敤鎴风殑openid }
	 */
	public JSONObject tagGetFans(Long weixinTagId, String nextOpenid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("tagid", weixinTagId);
		if (StringUtils.isNotBlank(nextOpenid)) {
			content.put("next_openid", nextOpenid);
		}
		return exc(url, content.toString());
	}

	/**
	 * 鎵归噺涓虹敤鎴锋墦鏍囩銆�
	 * 
	 * @param weixinTagId
	 * @param openidList
	 *            绮変笣鍒楄〃
	 * @return
	 */
	public JSONObject tagAddUsers(Long weixinTagId, List<String> openidList) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("openid_list", openidList);
		content.put("tagid", weixinTagId);
		return exc(url, content.toString());
	}

	/**
	 * 鎵归噺涓虹敤鎴峰彇娑堟爣绛�
	 * 
	 * @param weixinTagId
	 * @param openidList
	 *            绮変笣鍒楄〃
	 * @return
	 */
	public JSONObject tagRemoveUsers(Long weixinTagId, List<String> openidList) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("openid_list", openidList);
		content.put("tagid", weixinTagId);
		return exc(url, content.toString());
	}

	/**
	 * 鑾峰彇鐢ㄦ埛韬笂鐨勬爣绛惧垪琛�
	 * 
	 * @param openid
	 * @return { "tagid_list":[134, 2 ] }
	 */
	public JSONObject getUsertagList(String openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("openid", openid);
		return exc(url, content.toString());
	}

	/**
	 * 璁剧疆鐢ㄦ埛澶囨敞鍚�
	 * 
	 * @param openid
	 * @param remark
	 *            鏂扮殑澶囨敞鍚嶏紝闀垮害蹇呴』灏忎簬30瀛楃
	 * @return
	 */
	public JSONObject userUpdateRemark(String openid, String remark) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("openid", openid);
		content.put("remark", remark);
		return exc(url, content.toString());
	}

	/**
	 * 閫氳繃openId鑾峰緱鐢ㄦ埛openId鍒楄〃
	 * 
	 * @param next_openid
	 * @return returnJson
	 */
	public JSONObject getUserList(String next_openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken;
		if (!"".equals(next_openid) || next_openid == null) {
			url += "&next_openid=" + next_openid;
		}
		return excute(url);
	}

	/**
	 * 閫氳繃openid鑾峰彇鐢ㄦ埛淇℃伅
	 * 
	 * @param openId
	 * @return weixinUser
	 */
	public WeixinUser getUserInfo(String openId) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId;
		WeixinUser weixinUser = null;
		JSONObject json = excute(url);
		if (!json.containsKey("errcode")) {
			try {
				// weixinUser =
				// (WeixinUser)JSONObject.toBean(JSONObject.fromObject(json.toString(),
				// JSONUtil.jsonFilter(WeixinUser.class)), WeixinUser.class);
				weixinUser = new WeixinUser();
				weixinUser.setSubscribe(json.getInt("subscribe"));
				weixinUser.setOpenid(json.getString("openid"));
				weixinUser.setNickname(json.getString("nickname"));
				weixinUser.setSex(json.getInt("sex"));
				weixinUser.setLanguage(json.getString("language"));
				weixinUser.setCity(json.getString("city"));
				weixinUser.setCountry(json.getString("country"));
				weixinUser.setProvince(json.getString("province"));
				weixinUser.setHeadimgurl(json.getString("headimgurl"));
				weixinUser.setSubscribe_time(json.getLong("subscribe_time"));
				weixinUser.setGroupid(json.getInt("groupid"));
				weixinUser.setUnionid(json.getString("unionid"));
				weixinUser.setRemark(json.getString("remark"));
			} catch (Exception e) {
				weixinMsg.setMsg("80004");
			}
		}
		return weixinUser;
	}

	/**
	 * 鑾峰彇鐢ㄦ埛淇℃伅json
	 * 
	 * @param openId
	 * @return
	 */
	public JSONObject getUserInfoJSONObject(String openId) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId;
		return excute(url);
	}

	/**
	 * 鑾峰彇鍏紬鍙风殑榛戝悕鍗曞垪琛ㄣ�傝鎺ュ彛姣忔璋冪敤鏈�澶氬彲鎷夊彇 10000 涓狾penID锛屽綋鍒楄〃鏁拌緝澶氭椂锛屽彲浠ラ�氳繃澶氭鎷夊彇鐨勬柟寮忔潵婊¤冻闇�姹傘��
	 * 
	 * @param nextOpenid
	 * @return { "total":23000, "count":10000, "data":{" openid":[ "OPENID1",
	 *         "OPENID2", ..., "OPENID10000" ] }, "next_openid":"OPENID10000" }
	 */
	public JSONObject blackGet(String nextOpenid) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/members/getblacklist?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("begin_openid", nextOpenid);
		return exc(url, content.toString());
	}

	/**
	 * 鎷夐粦鐢ㄦ埛
	 * 
	 * @param openidList
	 *            闇�瑕佹媺鍏ラ粦鍚嶅崟鐨勭敤鎴风殑openid锛屼竴娆℃媺榛戞渶澶氬厑璁�20涓�
	 * @return { "errcode": 0, "errmsg": "ok" }
	 */
	public JSONObject blackAddUsers(List<String> openidList) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchblacklist?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("opened_list", openidList);
		return exc(url, content.toString());
	}

	/**
	 * 鍙栨秷鎷夐粦鐢ㄦ埛
	 * 
	 * @param openidList
	 * @return { "errcode": 0, "errmsg": "ok" }
	 */
	public JSONObject blackRemoveUsers(List<String> openidList) {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchunblacklist?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("opened_list", openidList);
		return exc(url, content.toString());
	}

	/**
	 * 鍚戠敤鎴峰彂閫佷俊鎭�
	 * 
	 * @param content
	 *            鍙戦�佸唴瀹�
	 */
	public Boolean sendMsg(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;
		return excuteParseBoolean(url, content);
	}

	///////////////////////////// 鑷畾涔夎彍鍗曞姛鑳藉紑濮�////////////////////////////////////
	/**
	 * 鐢熸垚鑷畾涔夎彍鍗�
	 * 
	 * @param menuStr
	 *            鍙戦�佸唴瀹�
	 */
	public Boolean sendCustomMenu(String menuStr) {
		String url = "http://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
		return excuteParseBoolean(url, menuStr);
	}

	/**
	 * 鑿滃崟鏌ヨ
	 */
	public JSONObject queryCustomMenu() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken;
		return excute(url);
	}

	/**
	 * 鍒犻櫎鑷畾涔夎彍鍗曪紝 濡傛灉鏈変慨鏀瑰寲鑷畾涔夎彍鍗曪紝涔熶竴璧峰垹闄わ紝鎵�浠ヤ娇鐢ㄧ殑鏃跺�欒璋ㄦ厧
	 */
	public Boolean deleteCustomMenu() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken;
		return excuteParseBoolean(url);
	}
	///////////////////////////// 鑷畾涔夎彍鍗曞姛鑳界粨鏉�////////////////////////////////////

	//////////////////////////// 涓�у寲鑷畾涔夎彍鍗曞姛鑳藉紑濮�////////////////////////////////////
	/**
	 * 鐢熸垚涓�у寲鑷畾涔夎彍鍗�
	 * 
	 * @param menuStr
	 */
	public JSONObject sendAddconditionalCustomMenu(String menuStr) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=" + accessToken;
		return excute(url, menuStr);
	}

	/**
	 * 鍒犻櫎涓�у寲鑷畾涔夎彍鍗�
	 * 
	 * @param menuid
	 */
	public JSONObject deleteAddconditionalCustomMenu(String menuid) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("menuid", menuid);
		return excute(url, content.toString());
	}

	/**
	 * 娴嬭瘯楠岃瘉涓�у寲鑷畾涔夎彍鍗�
	 * 
	 * @param openid
	 *            openid 鎴栬�呬篃鍙互鏄敤鎴风殑寰俊鍙�
	 * @return
	 */
	public JSONObject tryMathchAddconditionalCustomMenu(String openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/trymatch?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("user_id", openid);
		return excute(url, content.toString());
	}
	//////////////////////////// 涓�у寲鑷畾涔夎彍鍗曞姛鑳界粨鏉�////////////////////////////////////

	/**
	 * 妯＄増娑堟伅鎺ュ彛
	 * 
	 * @param content
	 * @return
	 */
	public JSONObject messageTemplateSend(JSONObject contentJson) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
		return excute(url, contentJson.toString());
	}

	/**
	 * 妯＄増娑堟伅鎺ュ彛
	 * 
	 * @param content
	 * @return
	 */
	public JSONObject messageTemplateSend(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
		return excute(url, content);
	}

	/**
	 * 涓婁紶鏂囦欢
	 * 
	 * @param file
	 *            濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖1锛坕mage锛夈��2璇煶锛坴oice锛夈��3瑙嗛锛坴ideo锛夊拰4缂╃暐鍥撅紙thumb锛� 杩斿洖map
	 *            message_flag error鍜宻uccess锛宮essage杩斿洖鍩烘湰淇℃伅锛宮edia_id 锛宑reated_at
	 *            鍥剧墖锛坕mage锛�: 128K锛屾敮鎸丣PG鏍煎紡 璇煶锛坴oice锛夛細256K锛屾挱鏀鹃暱搴︿笉瓒呰繃60s锛屾敮鎸丄MR\MP3鏍煎紡
	 *            瑙嗛锛坴ideo锛夛細1MB锛屾敮鎸丮P4鏍煎紡 缂╃暐鍥撅紙thumb锛夛細64KB锛屾敮鎸丣PG鏍煎紡
	 */
	public JSONObject uploadFile(File file, String type) {
		// 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
		JSONObject json = null;
		String up_url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type="
				+ type;
		if (!file.exists()) {
			weixinMsg.setMsg("80001");
			return null;
		}
		PostMethod postMethod = new PostMethod(up_url);
		try {
			FilePart fp = new FilePart("filedata", file);
			Part[] parts = { fp };
			MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mre);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 璁剧疆杩炴帴鏃堕棿
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				String result = postMethod.getResponseBodyAsString();
				json = JSONObject.fromObject(result);
				if (result.contains("errcode")) {
					weixinMsg.setMsg(String.valueOf(json.get("errcode")));
				}
			} else {
				weixinMsg.setMsg("80002");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 閲婃斁杩炴帴
			postMethod.releaseConnection();
		}
		return json;
	}

	/**
	 * 涓嬭浇鏂囦欢
	 * 
	 * @param media_id
	 * @param path
	 *            涓嬭浇鍦板潃
	 */
	public String downloadFile(String media_id, String path) {
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id="
				+ media_id;
		String picUrl = null;

		int BUFFER = 1024;
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setContentCharset("utf-8");
		try {
			client.executeMethod(httpGet);

			String newFileName = FileUtil.newFileName("test.jpg");
			String newPath = path + newFileName;
			File file = new File(newPath);
			if (!file.exists()) {
				file.createNewFile();
			}

			InputStream in = httpGet.getResponseBodyAsStream();
			FileOutputStream out = new FileOutputStream(new File(newPath));
			byte[] b = new byte[BUFFER];
			int len = 0;

			StringBuffer fstr = new StringBuffer();
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
				fstr.append(new String(b, 0, len));
			}
			in.close();
			out.close();

			if (fstr.toString().contains("errcode")) {
				JSONObject json = JSONObject.fromObject(fstr.toString());
				weixinMsg.setMsg(String.valueOf(json.get("errcode")));
			} else {
				picUrl = newFileName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}

		return picUrl;

	}

	// 鍒嗙粍绠＄悊寮�濮�
	/**
	 * 鏌ヨ鍒嗙粍锛岃繑鍥濲SONObject
	 * 
	 * @return json
	 */
	@Deprecated
	public JSONObject selectGroup() {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=" + accessToken;
		return excute(url);
	}

	/**
	 * 鍒涘缓鍒嗙粍
	 * 
	 * @param content
	 *            http璇锋眰鏂瑰紡: POST锛堣浣跨敤https鍗忚锛�
	 *            https://api.weixin.qq.com/cgi-bin/groups
	 *            /create?access_token=ACCESS_TOKEN POST鏁版嵁鏍煎紡锛歫son
	 *            POST鏁版嵁渚嬪瓙锛歿"group":{"name":"test"}}
	 */
	@Deprecated
	public Integer createGroup(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=" + accessToken;
		Integer groupId = null;
		JSONObject json = excute(url, content);
		if (json.containsKey("group")) {
			JSONObject group = (JSONObject) json.get("group");
			groupId = group.getInt("id");
		}
		return groupId;
	}

	/**
	 * 淇敼缁勫悕绉�
	 * 
	 * @param content
	 *            http璇锋眰鏂瑰紡: POST锛堣浣跨敤https鍗忚锛�
	 *            https://api.weixin.qq.com/cgi-bin/groups
	 *            /update?access_token=ACCESS_TOKEN POST鏁版嵁鏍煎紡锛歫son
	 *            POST鏁版嵁渚嬪瓙锛歿"group":{"id":108,"name":"test2_modify2"}}
	 */
	@Deprecated
	public Boolean updateGroup(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=" + accessToken;
		return excuteParseBoolean(url, content);
	}

	/**
	 * 鍒犻櫎鐢ㄦ埛鍒嗙粍
	 * 
	 * @param groupId
	 * @return
	 */
	@Deprecated
	public Boolean deleteGroup(Integer groupId) {
		Boolean success = false;
		String url = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=" + accessToken;
		String content = "{\"group\":{\"id\":" + groupId + "}}";
		JSONObject json = excute(url, content);
		if (json.get("errmsg") == null || json.get("errmsg").toString().equalsIgnoreCase("ok")) {
			success = true;
		}
		return success;
	}

	/**
	 * 绉诲姩鐢ㄦ埛鍒嗙粍
	 * 
	 * @param openid
	 *            鐢ㄦ埛openid
	 * @param groupId
	 *            鎵�瑕佺Щ鍔ㄧ殑鍒嗙粍
	 * @return
	 */
	@Deprecated
	public Boolean moveUserToGroup(String openid, Integer groupId) {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=" + accessToken;
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("openid", openid);
		jsonobject.put("to_groupid", groupId);
		return excuteParseBoolean(url, jsonobject.toString());
	}

	/**
	 * 鎵归噺绉诲姩鐢ㄦ埛鍒嗙粍
	 * 
	 * @param listOpenid
	 *            鐢ㄦ埛openid鍒楄〃
	 * @param groupId
	 *            鎵�瑕佺Щ鍔ㄧ殑鍒嗙粍
	 */
	@Deprecated
	public Boolean btnMoveUserToGroup(List<String> listOpenid, Integer groupId) {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=" + accessToken;
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("openid_list", listOpenid);
		jsonobject.put("to_groupid", groupId);
		return excuteParseBoolean(url, jsonobject.toString());
	}

	// 鍒嗙粍绠＄悊缁撴潫

	/**
	 * 閫氳繃appid 鍜宎ppsecret鑾峰緱 access token
	 */
	private void initAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WEIXIN_AppId
				+ "&secret=" + WEIXIN_AppSecret;
		JSONObject json = excute(url);
		if (json.get("access_token") != null) {
			accessToken = json.get("access_token").toString();
		} else {
			weixinMsg.setMsg(String.valueOf(json.get("errcode")));
		}
	}

	/**
	 * 
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	public String getOpenidByOauthCode(String appid, String secret, String code) {
		// 浣跨敤code鎹㈠彇access_token
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + appid + "&secret=" + secret
				+ "&code=" + code + "&grant_type=authorization_code";
		String openid = null;
		JSONObject json = null;
		try {
			GetMethod post = new GetMethod(url);
			post.setRequestHeader("Cache-Control", "no-cache");
			int status;
			status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String content = post.getResponseBodyAsString();
				System.out.println(content);
				json = JSONObject.fromObject(content);
				openid = json.get("openid").toString();
			}
		} catch (Exception e) {
			weixinMsg.setMsg(json.get("errcode").toString());
			e.printStackTrace();
		}

		return openid;
	}

	/**
	 * 閽堝绗笁鏂硅幏鍙朼ccessToken 褰搒nsapi_userinfo涓簊nsapi_userinfo鏄�氳繃code鎹㈠彇user
	 * 
	 * @param code
	 * @return
	 */
	public WeixinUser getUserByCode(String appid, String secret, String code) {

		// 浣跨敤code鎹㈠彇access_token
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code="
				+ code + "&grant_type=authorization_code";
		String openid = null;
		String access_token = null;
		JSONObject json = null;
		try {
			GetMethod post = new GetMethod(url);
			post.setRequestHeader("Cache-Control", "no-cache");
			int status;
			status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(post.getResponseBodyAsStream(), "utf-8"));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = reader.readLine()) != null) {
					stringBuffer.append(str);
				}
				json = JSONObject.fromObject(stringBuffer.toString());
				if (json.get("access_token") != null) {
					access_token = json.get("access_token").toString();
				}
				if (null != json.get("openid")) {
					openid = json.get("openid").toString();
				}
			}
		} catch (Exception e) {
			weixinMsg.setMsg(json.get("errcode").toString());
			e.printStackTrace();
		}
		WeixinUser user = null;
		if (openid != null && access_token != null) {
			// 鏈�鍚庤幏鍙栫敤鎴蜂俊鎭�
			url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
			String respStr = "";
			try {
				GetMethod post = new GetMethod(url);
				post.getParams().setContentCharset("utf-8");
				client.executeMethod(post);

				BufferedReader reader2 = new BufferedReader(
						new InputStreamReader(post.getResponseBodyAsStream(), "utf-8"));
				StringBuffer stringBuffer2 = new StringBuffer();
				String str2 = "";
				while ((str2 = reader2.readLine()) != null) {
					stringBuffer2.append(str2);
				}
				respStr = stringBuffer2.toString();
				try {
					user = (WeixinUser) JSONObject.toBean(JSONObject.fromObject(respStr), WeixinUser.class);
				} catch (Exception e) {
					JSONObject returnJson = JSONObject.fromObject(respStr);
					weixinMsg.setMsg(returnJson.get("errcode").toString());
				}
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return user;
	}

	/**
	 * 褰搒nsapi_userinfo涓簊nsapi_base鏄�氳繃code鎹㈠彇openid
	 * 
	 * @param code
	 * @return openid
	 */
	public String getOpenidByOauthCode(String code) {
		// 浣跨敤code鎹㈠彇access_token
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WEIXIN_AppId + "&secret="
				+ WEIXIN_AppSecret + "&code=" + code + "&grant_type=authorization_code";
		String openid = null;
		JSONObject json = excute(url);
		if (json.containsKey("openid") && json.get("openid") != null) {
			openid = json.get("openid").toString();
		}
		return openid;
	}

	/**
	 * 褰搒nsapi_userinfo涓簊nsapi_userinfo鏄�氳繃code鎹㈠彇user
	 * 
	 * @param code
	 * @return user
	 */
	public WeixinUser getUserByOauthCode(String code) {
		// 浣跨敤code鎹㈠彇access_token
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + WEIXIN_AppId + "&secret="
				+ WEIXIN_AppSecret + "&code=" + code + "&grant_type=authorization_code";
		String openid = null;
		String access_token = null;
		JSONObject json = excute(url);
		if (json.containsKey("openid")) {
			access_token = json.get("access_token").toString();
			openid = json.get("openid").toString();
		}
		WeixinUser weixinUser = null;
		if (openid != null && access_token != null) {
			// 鏈�鍚庤幏鍙栫敤鎴蜂俊鎭�
			url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
			JSONObject jsonWxUser = excute(url);
			if (!jsonWxUser.containsKey("errcode")) {
				try {
					weixinUser = (WeixinUser) JSONObject.toBean(json, WeixinUser.class);
				} catch (Exception e) {
					weixinMsg.setMsg("80004");
				}
			}
		}
		return weixinUser;
	}

	/**
	 * 鑾峰彇鎺堟潈閾炬帴
	 * 
	 * @param url
	 *            杩斿洖閾炬帴
	 * @param scope
	 *            浣滅敤鍩�
	 *            1.锛堜笉寮瑰嚭鎺堟潈椤甸潰锛岀洿鎺ヨ烦杞紝鍙兘鑾峰彇鐢ㄦ埛openid锛�2.锛堝脊鍑烘巿鏉冮〉闈紝鍙�氳繃openid鎷垮埌鏄电О銆佹�у埆銆佹墍鍦ㄥ湴銆傚苟涓旓紝鍗充娇鍦ㄦ湭鍏虫敞鐨勬儏鍐典笅锛屽彧瑕佺敤鎴锋巿鏉冿紝涔熻兘鑾峰彇鍏朵俊鎭級
	 * @param state
	 *            杩斿洖鎵�甯﹀弬鏁�
	 * @return returnUrl
	 */
	public String getOauthUrl(String url, Integer scope, String state) {
		String returnUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?";
		try {
			String wscope = "snsapi_userinfo";
			if (scope == 1) {
				wscope = "snsapi_base";
			} else {
				wscope = "snsapi_userinfo";
			}
			returnUrl += "appid=" + WEIXIN_AppId + "&redirect_uri=" + URLEncoder.encode(url, "utf-8")
					+ "&response_type=code&scope=" + wscope + "&state=" + state + "#wechat_redirect";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return returnUrl;
	}

	// 缇ゅ彂寮�濮�
	/**
	 * 棣栧厛瑕佷笂浼犲浘鏂囩礌鏉�,杩斿洖media_id
	 * 
	 * @param content
	 *            缇ゅ彂鍐呭
	 */
	public JSONObject upLoadMedia(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + accessToken;
		return excute(url, content);
	}

	/**
	 * 鏍规嵁鍒嗙粍ID杩涜缇ゅ彂銆愯闃呭彿涓庢湇鍔″彿璁よ瘉鍚庡潎鍙敤銆�
	 * 
	 * @param content
	 */
	public Boolean batchSendMessageByGroupId(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + accessToken;
		return excuteParseBoolean(url, content);
	}

	/**
	 * 鏍规嵁opendids缇ゅ彂淇℃伅
	 * 
	 * @param content
	 */
	public Boolean batchSendMessageByOpenIds(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + accessToken;
		return excuteParseBoolean(url, content);
	}

	/**
	 * 鏍规嵁opendids缇ゅ彂淇℃伅
	 * 
	 * @param content
	 */
	public String batchSendMessageByUserList(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + accessToken;
		String msg_id = null;
		JSONObject jsonObject = exc(url, content);
		if (jsonObject.containsKey("msg_id")) {
			msg_id = jsonObject.getString("msg_id");
		}
		return msg_id;
	}

	/**
	 * 缇ゅ彂棰勮鎺ュ彛
	 * 
	 * @param content
	 */
	public String batchSendMessagePreview(String content) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=" + accessToken;
		String msg_id = null;
		JSONObject jsonObject = exc(url, content);
		if (jsonObject.containsKey("msg_id")) {
			msg_id = jsonObject.getString("msg_id");
		}
		return msg_id;
	}

	/**
	 * 鍙栨秷缇ゅ彂锛堝彂鎴愬姛鍚庢墠鑳戒娇鐢級
	 * 
	 * @param content
	 */
	public Boolean cancelBatchSend(String content) {
		String url = "https://api.weixin.qq.com//cgi-bin/message/mass/delete?access_token=" + accessToken;
		return excuteParseBoolean(url, content);
	}

	/**
	 * 涓婁紶鏂囦欢
	 * 
	 * @param file
	 *            濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖1锛坕mage锛夈��2璇煶锛坴oice锛夈��3瑙嗛锛坴ideo锛夊拰4缂╃暐鍥撅紙thumb锛� 杩斿洖map
	 *            message_flag error鍜宻uccess锛宮essage杩斿洖鍩烘湰淇℃伅锛宮edia_id 锛宑reated_at
	 *            鍥剧墖锛坕mage锛�: 128K锛屾敮鎸丣PG鏍煎紡 璇煶锛坴oice锛夛細256K锛屾挱鏀鹃暱搴︿笉瓒呰繃60s锛屾敮鎸丄MR\MP3鏍煎紡
	 *            瑙嗛锛坴ideo锛夛細1MB锛屾敮鎸丮P4鏍煎紡 缂╃暐鍥撅紙thumb锛夛細64KB锛屾敮鎸丣PG鏍煎紡
	 */
	public UploadMediaBean uploadFileForGroup(File file, String type) {
		// 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
		JSONObject json = null;
		UploadMediaBean ub = null;
		String up_url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type="
				+ type;
		if (!file.exists()) {
			weixinMsg.setMsg("80001");
			return null;
		}
		PostMethod postMethod = new PostMethod(up_url);
		try {
			FilePart fp = new FilePart("filedata", file);
			Part[] parts = { fp };
			MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mre);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 璁剧疆杩炴帴鏃堕棿
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				String result = postMethod.getResponseBodyAsString();
				// System.out.println("===============result====="+result);
				json = JSONObject.fromObject(result);
				if (result.contains("errcode")) {
					weixinMsg.setMsg(String.valueOf(json.get("errcode")));
				} else {
					ub = (UploadMediaBean) JSONObject.toBean(JSONObject.fromObject(result), UploadMediaBean.class);
				}
			} else {
				weixinMsg.setMsg("80002");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 閲婃斁杩炴帴
			postMethod.releaseConnection();
		}

		return ub;
	}

	/**
	 * 涓嬭浇鏂囦欢
	 * 
	 * @param file_url
	 * @param media_id
	 */
	public String downloadFile(String media_id, String path, String newFileName) {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		System.setProperty("jsse.enableSNIExtension", "false");

		String url = "https://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id="
				+ media_id;
		String picUrl = null;
		int BUFFER = 1024;
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setContentCharset("utf-8");
		boolean isUrl = true;
		try {
			int status = client.executeMethod(httpGet);
			if (HttpStatus.SC_OK == status) {
				Header[] headers = httpGet.getResponseHeaders();
				for (Header header : headers) {
					if ("Content-disposition".equalsIgnoreCase(header.getName())) {
						String tmp = header.getValue().split(";")[1].split("=")[1];
						newFileName = tmp.substring(1, tmp.length() - 1);
						isUrl = false;
					}
				}
				if (!isUrl) {
					String newPath = path + File.separator + newFileName;
					File file = new File(newPath);
					if (!file.exists()) {
						file.createNewFile();
					}
					InputStream in = httpGet.getResponseBodyAsStream();
					FileOutputStream out = new FileOutputStream(new File(newPath));
					byte[] b = new byte[BUFFER];
					int len = 0;

					StringBuffer fstr = new StringBuffer();
					while ((len = in.read(b)) != -1) {
						out.write(b, 0, len);
						fstr.append(new String(b, 0, len));
					}
					in.close();
					out.close();
					if (fstr.toString().contains("errcode")) {
						JSONObject json = JSONObject.fromObject(fstr.toString());
						weixinMsg.setMsg(String.valueOf(json.get("errcode")));
					} else {
						picUrl = newFileName;
					}
				} else {
					try {
//						picUrl = JacksonUtil.deserializeAsObject(httpGet.getResponseBodyAsString(), BaseDto.class)
//								.getAsString("video_url");
						httpGet.releaseConnection();
						httpGet = new GetMethod(picUrl);
						httpGet.getParams().setContentCharset("utf-8");
						status = client.executeMethod(httpGet);
						if (HttpStatus.SC_OK == status) {
							headers = httpGet.getResponseHeaders();
							for (Header header : headers) {
								if ("Content-disposition".equalsIgnoreCase(header.getName())) {
									String tmp = header.getValue().split(";")[1].split("=")[1];
									newFileName = tmp.substring(1, tmp.length());
								}
							}

							if (StringUtils.isNotBlank(newFileName)) {
								String newPath = path + File.separator + newFileName;
								File file = new File(newPath);
								if (!file.exists()) {
									file.createNewFile();
								}
								InputStream in = httpGet.getResponseBodyAsStream();
								FileOutputStream out = new FileOutputStream(new File(newPath));
								byte[] b = new byte[BUFFER];
								int len = 0;

								StringBuffer fstr = new StringBuffer();
								while ((len = in.read(b)) != -1) {
									out.write(b, 0, len);
									fstr.append(new String(b, 0, len));
								}
								in.close();
								out.close();
								if (fstr.toString().contains("errcode")) {
									JSONObject json = JSONObject.fromObject(fstr.toString());
									weixinMsg.setMsg(String.valueOf(json.get("errcode")));
								} else {
									picUrl = newFileName;
								}
							}

						}

					} catch (Exception e) {
						picUrl = null;
						e.printStackTrace();
					} finally {
						if (null != httpGet) {
							httpGet.releaseConnection();
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != httpGet) {
				httpGet.releaseConnection();
			}
		}
		return picUrl;
	}

	/**
	 * 涓婁紶鍥炬枃娑堟伅鍐呯殑鍥剧墖鑾峰彇URL
	 * 
	 * @param file
	 * @return
	 */
	public String uploadImage(File file) {

		JSONObject json = null;
		String ub = null;
		String up_url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=" + accessToken;

		if (!file.exists()) {
			weixinMsg.setMsg("80001");
			return null;
		}

		PostMethod postMethod = new PostMethod(up_url);
		try {
			FilePart fp = new FilePart("filedata", file);
			Part[] parts = { fp };
			MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mre);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 璁剧疆杩炴帴鏃堕棿
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				String result = postMethod.getResponseBodyAsString();

				json = JSONObject.fromObject(result);
				if (result.contains("errcode")) {
					weixinMsg.setMsg(String.valueOf(json.get("errcode")));
				} else {
					ub = json.get("url").toString();
				}
			} else {
				weixinMsg.setMsg("80002");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 閲婃斁杩炴帴
			postMethod.releaseConnection();
		}

		return ub;
	}
	// 缇ゅ彂缁撴潫

	/**
	 * 鍙湁閾炬帴鎵ц 涓嶅甫鍙傛暟
	 * 
	 * @param url
	 *            閾炬帴
	 * @return true/false
	 */
	private Boolean excuteParseBoolean(String url) {
		return exc(url, null).getInt("errcode") == 0 ? true : false;
	}

	/**
	 * 閾炬帴鍙婂弬鏁伴兘瑕佹墽琛�
	 * 
	 * @param url
	 *            閾炬帴
	 * @param content
	 *            鍐呭
	 * @return true/false
	 */
	private Boolean excuteParseBoolean(String url, String content) {
		return exc(url, content).getInt("errcode") == 0 ? true : false;
	}

	/**
	 * 鍙湁閾炬帴鎵ц 涓嶅甫鍙傛暟
	 * 
	 * @param url
	 *            閾炬帴
	 * @return JSONObject
	 */
	private JSONObject excute(String url) {
		return exc(url, null);
	}

	/**
	 * 閾炬帴鍙婂弬鏁伴兘瑕佹墽琛�
	 * 
	 * @param url
	 *            閾炬帴
	 * @param content
	 *            鍐呭
	 * @return JSONObject
	 */
	private JSONObject excute(String url, String content) {
		return exc(url, content);
	}

	/**
	 * 鎵ц杩滅▼鏂归潰
	 * 
	 * @param url
	 *            閾炬帴
	 * @param content
	 *            鍐呭
	 * @return JSONObject
	 */
	private JSONObject exc(String url, String content) {
		JSONObject json = null;
		try {
			PostMethod post = new PostMethod(url);
			post.getParams().setContentCharset("utf-8");
			if (content != null) {
				post.setRequestEntity(new StringRequestEntity(content, null, "utf-8"));
			}
			int status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String respStr = post.getResponseBodyAsString();
				json = JSONObject.fromObject(respStr);
				// System.out.println("respStr : " + respStr);
				// System.out.println("json : "+ json);
				logger.info(respStr);
				if (respStr.contains("errcode")) {
					weixinMsg.setMsg(json.get("errcode").toString());
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public WeixinMsg getWeixinMsg() {
		return weixinMsg;
	}

	public String getWEIXIN_AppId() {
		return WEIXIN_AppId;
	}

	public void setWEIXIN_AppId(String wEIXIN_AppId) {
		WEIXIN_AppId = wEIXIN_AppId;
	}

	public String getWEIXIN_AppSecret() {
		return WEIXIN_AppSecret;
	}

	public void setWEIXIN_AppSecret(String wEIXIN_AppSecret) {
		WEIXIN_AppSecret = wEIXIN_AppSecret;
	}

	public void setWeixinMsg(WeixinMsg weixinMsg) {
		this.weixinMsg = weixinMsg;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}