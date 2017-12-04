package framework.qyweixin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
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

import framework.qyweixin.send.MsgArticle;
import framework.qyweixin.send.MsgMpArticle;
import framework.qyweixin.send.MsgSend;
import framework.qyweixin.util.QyUploadMedia;
import framework.qyweixin.util.QyWeixinMsg;
import framework.weixin.util.MySecureProtocolSocketFactory;

/**
 * 浼佷笟鍙峰井淇★紝鍥犺幏鍙栬皟鐢ㄦ帴鍙ｅ嚟璇佸拰寰俊鍏紬鍙蜂笉涓�鏍凤紝 澶氭璋冪敤浠ュ強澶辨晥閲嶆柊鑾峰彇鍔熻兘鍦ㄥ井淇″钩鍙颁笂锛� 鎴戜滑寮�鍙戝彧闇�瑕佺鑾峰彇灏辫浜嗭紝 鑾峰彇鍒扮殑锛岄偅灏辨槸鏈夋晥鐨�
 * 
 * @author Administrator @date2014-12-17
 */
public class QyWeixinHandler {
	private String qy_corpid;
	private String qy_secret;
	private HttpClient client = new HttpClient();
	private QyWeixinMsg qyWeixinMsg = new QyWeixinMsg();
	private String accessToken = null;
	public static Logger logger = LoggerFactory.getLogger("QYWEIXIN_LOG");

	public QyWeixinHandler() {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		System.setProperty("jsse.enableSNIExtension", "false");
	}

	/**
	 * 鏋勯�犳柟娉� 閫氳繃qy_corpid鍜宷y_secret鏋勫缓寰俊瀵硅薄
	 * 
	 * @param String
	 *            qy_corpid
	 * @param String
	 *            qy_secret
	 */
	public QyWeixinHandler(String qy_corpid, String qy_secret) {
		this.qy_corpid = qy_corpid;
		this.qy_secret = qy_secret;
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		System.setProperty("jsse.enableSNIExtension", "false");
		initAccessToken();
	}

	/**
	 * 鏋勯�犳柟娉� 閫氳繃accessToken鏋勫缓寰俊瀵硅薄
	 * 
	 * @param String
	 *            accessToken
	 */
	public QyWeixinHandler(String accessToken) {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		System.setProperty("jsse.enableSNIExtension", "false");
		this.accessToken = accessToken;
	}

	/**
	 * 鍒濆鍖朼ccessToken
	 */
	private void initAccessToken() {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + qy_corpid + "&corpsecret=" + qy_secret;
		JSONObject json = excute(url);
		if (json.containsKey("access_token") && json.get("access_token") != null) {
			accessToken = json.get("access_token").toString();
		} else {
			qyWeixinMsg.setMsg(json.get("errcode").toString());
		}
	}

	/**
	 * 鍒濆鍖朼ccessToken
	 * 
	 * @param String
	 *            qy_corpid
	 * @param String
	 *            qy_secret
	 * @return String accessToken
	 */
	public String getAccessToken(String qy_corpid, String qy_secret) {
		String accessToken = null;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + qy_corpid + "&corpsecret=" + qy_secret;
		JSONObject json = excute(url);
		if (json.containsKey("access_token") && json.get("access_token") != null) {
			accessToken = excute(url).get("access_token").toString();
		}
		return accessToken;
	}

	/**
	 * 鑾峰彇浼佷笟鍙穞icket
	 * 
	 * @return
	 */
	public String getQyJsapiTicket() {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + accessToken;
		String ticket = null;
		JSONObject json = excute(url);
		if (json.containsKey("ticket") && json.get("ticket") != null) {
			ticket = json.get("ticket").toString();
		}
		return ticket;
	}

	/**
	 * 杩斿洖鐨勬槸涓垪琛紝骞舵帓鎺掑垪,涓嶆槸鎸夊眰绾ф帓鐨勶紝瑕佸眰绾ф帓鐨勬湁涓浆鎹㈠伐鍏� ChangePartBeanType
	 * 
	 * @return JSONArray
	 */
	public JSONArray getAgentsList() {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/agent/list?access_token=" + accessToken;
		return excute(url).getJSONArray("agentlist");
	}

	/**
	 * 鑾峰彇浼佷笟搴旂敤
	 * 
	 * @param agentid
	 * @return
	 */
	public JSONObject getAgent(Integer agentid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token=" + accessToken + "&agentid="
				+ agentid;
		return excute(url);
	}

	/**
	 * 璇PI鐢ㄤ簬璁剧疆浼佷笟搴旂敤鐨勯�夐」璁剧疆淇℃伅锛屽锛氬湴鐞嗕綅缃笂鎶ョ瓑銆�
	 * 
	 * @param agentid
	 *            浼佷笟搴旂敤鐨刬d
	 * @param name
	 *            浼佷笟搴旂敤鍚嶇О
	 * @param report_location_flag
	 *            浼佷笟搴旂敤鏄惁鎵撳紑鍦扮悊浣嶇疆涓婃姤 0锛氫笉涓婃姤锛�1锛氳繘鍏ヤ細璇濅笂鎶ワ紱2锛氭寔缁笂鎶�
	 * @param logo_mediaid
	 *            浼佷笟搴旂敤澶村儚鐨刴ediaid锛岄�氳繃澶氬獟浣撴帴鍙ｄ笂浼犲浘鐗囪幏寰梞ediaid锛屼笂浼犲悗浼氳嚜鍔ㄨ鍓垚鏂瑰舰鍜屽渾褰袱涓ご鍍�
	 * @param description
	 *            浼佷笟搴旂敤璇︽儏
	 * @param redirect_domain
	 *            浼佷笟搴旂敤鍙俊鍩熷悕
	 * @param isreportuser
	 *            鏄惁鎺ユ敹鐢ㄦ埛鍙樻洿閫氱煡銆�0锛氫笉鎺ユ敹锛�1锛氭帴鏀�
	 * @param isreportenter
	 *            鏄惁涓婃姤鐢ㄦ埛杩涘叆搴旂敤浜嬩欢銆�0锛氫笉鎺ユ敹锛�1锛氭帴鏀�
	 * @return true/false
	 */
	public JSONObject agentUpadte(JSONObject content) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/agent/set?access_token=" + accessToken;
		return excute(url, content);
	}

	/**
	 * 璇PI鐢ㄤ簬璁剧疆浼佷笟搴旂敤鐨勯�夐」璁剧疆淇℃伅锛屽锛氬湴鐞嗕綅缃笂鎶ョ瓑銆�
	 * 
	 * @param agentid
	 *            浼佷笟搴旂敤鐨刬d
	 * @param name
	 *            浼佷笟搴旂敤鍚嶇О
	 * @param report_location_flag
	 *            浼佷笟搴旂敤鏄惁鎵撳紑鍦扮悊浣嶇疆涓婃姤 0锛氫笉涓婃姤锛�1锛氳繘鍏ヤ細璇濅笂鎶ワ紱2锛氭寔缁笂鎶�
	 * @param logo_mediaid
	 *            浼佷笟搴旂敤澶村儚鐨刴ediaid锛岄�氳繃澶氬獟浣撴帴鍙ｄ笂浼犲浘鐗囪幏寰梞ediaid锛屼笂浼犲悗浼氳嚜鍔ㄨ鍓垚鏂瑰舰鍜屽渾褰袱涓ご鍍�
	 * @param description
	 *            浼佷笟搴旂敤璇︽儏
	 * @param redirect_domain
	 *            浼佷笟搴旂敤鍙俊鍩熷悕
	 * @param isreportuser
	 *            鏄惁鎺ユ敹鐢ㄦ埛鍙樻洿閫氱煡銆�0锛氫笉鎺ユ敹锛�1锛氭帴鏀�
	 * @param isreportenter
	 *            鏄惁涓婃姤鐢ㄦ埛杩涘叆搴旂敤浜嬩欢銆�0锛氫笉鎺ユ敹锛�1锛氭帴鏀�
	 * @return true/false
	 */
	public Boolean updateAgent(Integer agentid, String name, String report_location_flag, String logo_mediaid,
			String description, String redirect_domain, Integer isreportuser, Integer isreportenter) {
		Boolean success = true;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/agent/set?access_token=" + accessToken;

		JSONObject content = new JSONObject();
		content.put("agentid", agentid + "");
		if (!StringUtils.isEmpty(name)) {
			content.put("name", name);
		}

		if (!StringUtils.isEmpty(report_location_flag)) {
			content.put("report_location_flag", report_location_flag);
		}

		if (!StringUtils.isEmpty(logo_mediaid)) {
			content.put("logo_mediaid", logo_mediaid);
		}

		if (!StringUtils.isEmpty(description)) {
			content.put("description", description);
		}

		if (!StringUtils.isEmpty(redirect_domain)) {
			content.put("redirect_domain", redirect_domain);
		}

		if (isreportuser != null) {
			content.put("isreportuser", 0);
		}

		if (isreportenter != null) {
			content.put("isreportenter", 0);
		}

		if (success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 杩斿洖鐨勬槸涓垪琛紝骞舵帓鎺掑垪,涓嶆槸鎸夊眰绾ф帓鐨勶紝瑕佸眰绾ф帓鐨勬湁涓浆鎹㈠伐鍏� ChangePartBeanType
	 * 
	 * @return JSONArray
	 */
	public JSONArray getDepartmentsList() {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + accessToken;
		JSONObject json = excute(url);
		return json.getJSONArray("department");
	}

	/**
	 * 閮ㄩ棬鍒涘缓name parentid涓哄繀濉�
	 * 
	 * @param String
	 *            name (蹇呭～)
	 * @param Integer
	 *            parentid (蹇呭～)
	 * @param Integer
	 *            order
	 * @return status 0.澶辫触 鍚﹀垯杩斿洖鏂板閮ㄩ棬鐨処D
	 */
	public Integer DepartmentCreate(String name, Integer parentid, Integer order) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + accessToken;
		Integer status = 1;

		JSONObject content = new JSONObject();
		if (StringUtils.isEmpty(name)) {
			status = 0;
			qyWeixinMsg.setMsg("80005");
		} else {
			content.put("name", name);
		}

		if (parentid == null) {
			status = 0;
			qyWeixinMsg.setMsg("80006");
		} else {
			content.put("parentid", parentid + "");
		}

		if (order != null) {
			content.put("order", order + "");
		}

		if (status == 1) {
			status = excute(url, content).getInt("id");
		}

		return status;
	}

	/**
	 * 閮ㄩ棬鏇存柊 id涓哄繀椤�
	 * 
	 * @param Integer
	 *            id (蹇呭～)
	 * @param String
	 *            name
	 * @param Integer
	 *            parentid
	 * @param Integer
	 *            order
	 * @return true / false
	 */
	public Boolean DepartmentUpdate(Integer id, String name, Integer parentid, Integer order) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=" + accessToken;
		Boolean success = false;

		JSONObject content = new JSONObject();
		if (id == null) {
			success = false;
			qyWeixinMsg.setMsg("80004");
		} else {
			content.put("id", id + "");
		}

		if (!StringUtils.isEmpty(name)) {
			content.put("name", name);
		}

		if (parentid != null) {
			content.put("parentid", parentid + "");
		}

		if (order != null) {
			content.put("order", order + "");
		}

		if (!success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 鍒犻櫎涓�涓儴闂�
	 * 
	 * @param id
	 */
	public Boolean partDelete(Integer id) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=" + accessToken + "&id=" + id;
		return excuteParseBoolean(url);
	}

	// ////////////////////////閮ㄩ棬鍔熻兘缁撴潫//////////////////////////////////

	// ///////////////////////////鍛樺伐鍔熻兘寮�濮�////////////////////////////////////
	/**
	 * 鑾峰彇閮ㄩ棬鎴愬憳 department_id 鑾峰彇鐨勯儴闂╥d 涓嶆槸蹇呭～ fetch_child 1/0锛氭槸鍚﹂�掑綊鑾峰彇瀛愰儴闂ㄤ笅闈㈢殑鎴愬憳,涓嶆槸蹇呭～
	 * 涓嶆槸蹇呭～ status 0鑾峰彇鍏ㄩ儴鍛樺伐锛�1鑾峰彇宸插叧娉ㄦ垚鍛樺垪琛紝2鑾峰彇绂佺敤鎴愬憳鍒楄〃锛�4鑾峰彇鏈叧娉ㄦ垚鍛樺垪琛ㄣ�俿tatus鍙彔鍔�
	 * 
	 * @param department_id
	 * @param fetch_child
	 * @param status
	 * @return JSONArray
	 */
	public JSONArray getUsersByDepartment(Integer department_id, Integer fetch_child, Integer status) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + this.accessToken
				+ "&department_id=" + department_id + "&fetch_child=" + fetch_child + "&status=" + status;
		return excute(url).getJSONArray("userlist");
	}

	/**
	 * 鐢ㄦ埛娣诲姞
	 * 
	 * @param userid
	 * @param name
	 * @param department
	 * @param position
	 * @param mobile
	 * @param email
	 * @param weixinid
	 * @return true / false
	 */
	public Boolean userCreate(String userid, String name, Integer[] department, String position, String mobile,
			String email, String weixinid) {
		Boolean success = false;
		JSONObject content = new JSONObject();

		if (StringUtils.isEmpty(userid)) {
			success = false;
			qyWeixinMsg.setMsg("80007");
		} else {
			content.put("userid", userid);
		}

		if (StringUtils.isEmpty(name)) {
			success = false;
			qyWeixinMsg.setMsg("80008");
		} else {
			content.put("name", name);
		}

		if (department != null) {
			content.put("department", JSONArray.fromObject(department));
		}

		if (position != null) {
			content.put("position", position);
		}

		if (mobile != null) {
			content.put("mobile", mobile);
		}

		if (email != null) {
			content.put("email", email);
		}

		if (weixinid != null) {
			content.put("weixinid", weixinid);
		}

		if (!success) {
			success = userCreateOrUpdate(content, "create");
		}

		return success;
	}

	/**
	 * 鐢ㄦ埛鏇存柊
	 * 
	 * @param userid
	 * @param name
	 * @param department
	 * @param position
	 * @param mobile
	 * @param email
	 * @param weixinid
	 * @param enable
	 * @return true / false
	 */
	public Boolean userUpdate(String userid, String name, Integer[] department, String position, String mobile,
			String email, String weixinid, Integer enable) {
		Boolean success = false;

		JSONObject content = new JSONObject();
		if (StringUtils.isEmpty(userid)) {
			success = false;
			qyWeixinMsg.setMsg("80007");
		} else {
			content.put("userid", userid);
		}

		if (name != null) {
			content.put("name", name);
		}

		if (department != null) {
			content.put("department", JSONArray.fromObject(department));
		}

		if (position != null) {
			content.put("position", position);
		}

		if (mobile != null) {
			content.put("mobile", mobile);
		}

		if (email != null) {
			content.put("email", email);
		}

		if (weixinid != null) {
			content.put("weixinid", weixinid);
		}

		if (enable != null) {
			content.put("enable", enable);
		}

		if (!success) {
			success = userCreateOrUpdate(content, "update");
		}

		return success;
	}

	/**
	 * 娣诲姞鎴栬�呬慨鏀圭敤鎴� 绉佹湁鏂规硶,渚涘唴閮ㄤ娇鐢�
	 * 
	 * @param String
	 *            content 璇锋眰鍖�
	 * @param String
	 *            action 鎵ц鍔ㄤ綔 create or update
	 * @return true / false
	 */
	public Boolean userCreateOrUpdate(JSONObject jsonObject, String action) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/" + action + "?access_token=" + accessToken;
		return excuteParseBoolean(url, jsonObject);
	}

	/**
	 * 鍒犻櫎鍗曚釜鐢ㄦ埛
	 * 
	 * @param userid
	 *            (蹇呭～)
	 * @return true / false
	 */
	public Boolean userDeleteOne(String userid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=" + accessToken + "&userid="
				+ userid;
		return excuteParseBoolean(url);
	}

	/**
	 * 鎵归噺鍒犻櫎
	 * 
	 * @param content
	 */
	public void userDeleteBatch(String[] userArray) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("useridlist", JSONArray.fromObject(userArray));
		excute(url, content);
	}

	/**
	 * 鑾峰彇涓�涓敤鎴蜂俊鎭�
	 * 
	 * @param userid
	 */
	public JSONObject userGetOneInfo(String userid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&userid=" + userid;
		return excute(url);
	}

	/**
	 * 璁╂垚鍛樺叧娉ㄦ垚鍔�
	 * 
	 * @param userid
	 * @return
	 */
	public Boolean authsucc(String userid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/authsucc?access_token=" + accessToken + "&userid="
				+ userid;
		return excuteParseBoolean(url);
	}

	/**
	 * 閭�璇峰叧娉�
	 * 
	 * @param userid
	 * @return
	 */
	public Boolean inviteUser(String userid) {
		Boolean success = true;
		JSONObject content = new JSONObject();
		if (StringUtils.isEmpty(userid)) {
			success = false;
			qyWeixinMsg.setMsg("80007");
		} else {
			content.put("userid", userid);
		}
		String url = "https://qyapi.weixin.qq.com/cgi-bin/invite/send?access_token=" + accessToken;
		if (success) {
			success = excuteParseBoolean(url, content);
		}
		return success;
	}

	/**
	 * userid杞崲鎴恛penid
	 * 
	 * @param userid
	 *            -- 浼佷笟鍙峰唴鐨勬垚鍛榠d
	 * @param agentid
	 *            -- 鏁村瀷锛岄渶瑕佸彂閫佺孩鍖呯殑搴旂敤ID锛岃嫢鍙槸浣跨敤寰俊鏀粯鍜屼紒涓氳浆璐︼紝鍒欐棤闇�璇ュ弬鏁�
	 * @description 鎴愬憳蹇呴』澶勪簬搴旂敤鐨勫彲瑙佽寖鍥村唴锛屽苟涓旂鐞嗙粍瀵瑰簲鐢ㄦ湁浣跨敤鏉冮檺銆佸鎴愬憳鏈夋煡鐪嬫潈闄愩��
	 * @return openid -- oDOGms-6yCnGrRovBj2yHij5JL6E appid --
	 *         wxf874e15f78cc84a7 (if 璇锋眰鍙傛暟鍖呭惈agentid 鍒欒繑鍥� appid)
	 */
	public JSONObject getOpenidByUserid(String userid, Integer agentid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		content.put("userid", userid);
		if (null != agentid) {
			content.put("agentid", agentid);
		}
		JSONObject object = excute(url, content);
		try {
			if (object.getInt("errcode") != 0) {
				qyWeixinMsg.setMsg(object.get("errcode").toString());
			}
		} catch (Exception e) {
			qyWeixinMsg.setMsg(object.get("errcode").toString());
			e.printStackTrace();
		}
		return object;
	}
	// ///////////////////////////鍛樺伐鍔熻兘缁撴潫////////////////////////////////////

	// ///////////////////////////鏍囩鍔熻兘寮�濮�////////////////////////////////////
	/**
	 * 鑾峰彇鎵�鏈夋爣绛�
	 * 
	 * @return
	 */
	public JSONArray getTagsList() {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token=" + accessToken;
		return excute(url).getJSONArray("taglist");
	}

	/**
	 * 娣诲姞鏍囩
	 * 
	 * @param tagname
	 * @return
	 */
	public Integer tagCreate(String tagname) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/create?access_token=" + accessToken;
		Boolean success = true;

		JSONObject content = new JSONObject();
		if (StringUtils.isEmpty(tagname)) {
			success = false;
			qyWeixinMsg.setMsg("80009");
		} else {
			content.put("tagname", tagname);
		}

		Integer tagid = null;

		if (success) {
			JSONObject object = excute(url, content);
			try {
				if (object.getInt("errcode") == 0) {
					tagid = object.getInt("tagid");
				} else {
					qyWeixinMsg.setMsg(object.get("errcode").toString());
				}
			} catch (Exception e) {
				qyWeixinMsg.setMsg(object.get("errcode").toString());
				e.printStackTrace();
			}
		}

		return tagid;
	}

	/**
	 * 鏇存柊鏍囩
	 * 
	 * @param tagid
	 * @param tagname
	 * @return
	 */
	public Boolean tagUpdate(Integer tagid, String tagname) {
		Boolean success = true;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/update?access_token=" + accessToken;
		JSONObject content = new JSONObject();

		if (tagid == null) {
			success = false;
			qyWeixinMsg.setMsg("41017");
		} else {
			content.put("tagid", tagid + "");
		}
		if (StringUtils.isEmpty(tagname)) {
			success = false;
			qyWeixinMsg.setMsg("80009");
		} else {
			content.put("tagname", tagname);
		}

		if (success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 鍒犻櫎涓�涓爣绛�
	 * 
	 * @param tagid
	 * @return
	 */
	public Boolean tagDeleteOne(Integer tagid) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/delete?access_token=" + accessToken + "&tagid=" + tagid;
		success = excuteParseBoolean(url);
		return success;
	}

	/**
	 * 鑾峰緱鍗曚釜鏍囩涓嬬殑鍛樺伐
	 * 
	 * @param tagid
	 * @return
	 */
	public JSONObject getUserByTag(Integer tagid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=" + accessToken + "&tagid=" + tagid;
		return excute(url);
	}

	/**
	 * 鍒犻櫎鏍囩鐢ㄦ埛
	 * 
	 * @param tagid
	 * @param userlist
	 *            娣诲姞鐢ㄦ埛鍒楄〃
	 * @param partylist
	 *            娣诲姞閮ㄩ棬鍒楄〃
	 * @return
	 */
	public Boolean addUserTag(Integer tagid, String[] userlist, String[] partylist) {
		JSONObject content = new JSONObject();
		content.put("tagid", tagid + "");
		content.put("userlist", JSONArray.fromObject(userlist));
		content.put("partylist", JSONArray.fromObject(partylist));
		return delUserTag(content, "addtagusers");
	}

	/**
	 * 鍒犻櫎鏍囩鐢ㄦ埛
	 * 
	 * @param tagid
	 * @param userlist
	 *            鍒犻櫎鐢ㄦ埛鍒楄〃
	 * @param partylist
	 *            鍒犻櫎閮ㄩ棬鍒楄〃
	 * @return
	 */
	public Boolean deleteUserTag(Integer tagid, String[] userlist, String[] partylist) {
		JSONObject content = new JSONObject();
		content.put("tagid", tagid + "");
		content.put("userlist", JSONArray.fromObject(userlist));
		content.put("partylist", JSONArray.fromObject(partylist));
		return delUserTag(content, "deltagusers");
	}

	public Boolean delUserTag(JSONObject content, String action) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/tag/" + action + "?access_token=" + accessToken;
		success = excuteParseBoolean(url, content);
		return success;
	}

	// ///////////////////////////鏍囩鍔熻兘缁撴潫////////////////////////////////////

	// ///////////////////////////鏂囦欢涓婁紶寮�濮�////////////////////////////////////

	/**
	 * 涓婁紶濯掍綋鏂囦欢
	 * 
	 * @param File
	 *            file 鏂囦欢
	 * @param type
	 *            濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夛紝鏅�氭枃浠�(file)
	 *            鍥剧墖锛坕mage锛�:1MB锛屾敮鎸丣PG鏍煎紡 璇煶锛坴oice锛夛細2MB锛屾挱鏀鹃暱搴︿笉瓒呰繃60s锛屾敮鎸丄MR鏍煎紡
	 *            瑙嗛锛坴ideo锛夛細10MB锛屾敮鎸丮P4鏍煎紡 鏅�氭枃浠讹紙file锛夛細10MB
	 * @return
	 * @throws Exception
	 * 
	 */
	public JSONObject mediaUpload(File file, String type) {
		// 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
		JSONObject json = null;
		String up_url = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type="
				+ type;
		String result = null;
		BufferedReader reader = null;
		try {
			/**
			 * 绗竴閮ㄥ垎
			 */
			URL urlObj = new URL(up_url);
			// 杩炴帴
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

			/**
			 * 璁剧疆鍏抽敭鍊�
			 */
			con.setRequestMethod("POST"); // 浠ost鏂瑰紡鎻愪氦琛ㄥ崟锛岄粯璁et鏂瑰紡
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false); // post鏂瑰紡涓嶈兘浣跨敤缂撳瓨

			// 璁剧疆璇锋眰澶翠俊鎭�
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");

			// 璁剧疆杈圭晫
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			// 绗竴閮ㄥ垎锛�
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // 蹇呴』澶氫袱閬撶嚎
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			byte[] head = sb.toString().getBytes("utf-8");

			// 鑾峰緱杈撳嚭娴�
			OutputStream out = new DataOutputStream(con.getOutputStream());
			// 杈撳嚭琛ㄥご
			out.write(head);

			// 鏂囦欢姝ｆ枃閮ㄥ垎
			// 鎶婃枃浠跺凡娴佹枃浠剁殑鏂瑰紡 鎺ㄥ叆鍒皍rl涓�
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();

			// 缁撳熬閮ㄥ垎
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 瀹氫箟鏈�鍚庢暟鎹垎闅旂嚎

			out.write(foot);

			out.flush();
			out.close();

			StringBuffer buffer = new StringBuffer();

			// 瀹氫箟BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
				json = JSONObject.fromObject(result);
			}
		} catch (IOException e) {
			qyWeixinMsg.setMsg("80002");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					qyWeixinMsg.setMsg("80002");
					e.printStackTrace();
				}
			}
		}

		return json;
	}

	/**
	 * 涓婁紶濯掍綋鏂囦欢
	 * 
	 * @param File
	 *            file 鏂囦欢
	 * @param type
	 *            濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夛紝鏅�氭枃浠�(file)
	 *            鍥剧墖锛坕mage锛�:1MB锛屾敮鎸丣PG鏍煎紡 璇煶锛坴oice锛夛細2MB锛屾挱鏀鹃暱搴︿笉瓒呰繃60s锛屾敮鎸丄MR鏍煎紡
	 *            瑙嗛锛坴ideo锛夛細10MB锛屾敮鎸丮P4鏍煎紡 鏅�氭枃浠讹紙file锛夛細10MB
	 * @return
	 * @throws Exception
	 * 
	 */
	public QyUploadMedia uploadFileReturnBean(File file, String type) throws Exception {
		// 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
		JSONObject json = null;
		QyUploadMedia ub = null;
		String up_url = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type="
				+ type;
		if (!file.exists()) {
			qyWeixinMsg.setMsg("80001");
			return null;
		}

		String result = null;

		/**
		 * 绗竴閮ㄥ垎
		 */
		URL urlObj = new URL(up_url);
		// 杩炴帴
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		/**
		 * 璁剧疆鍏抽敭鍊�
		 */
		con.setRequestMethod("POST"); // 浠ost鏂瑰紡鎻愪氦琛ㄥ崟锛岄粯璁et鏂瑰紡
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post鏂瑰紡涓嶈兘浣跨敤缂撳瓨

		// 璁剧疆璇锋眰澶翠俊鎭�
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		// 璁剧疆杈圭晫
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		// 绗竴閮ㄥ垎锛�
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 蹇呴』澶氫袱閬撶嚎
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		// 鑾峰緱杈撳嚭娴�
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// 杈撳嚭琛ㄥご
		out.write(head);

		// 鏂囦欢姝ｆ枃閮ㄥ垎
		// 鎶婃枃浠跺凡娴佹枃浠剁殑鏂瑰紡 鎺ㄥ叆鍒皍rl涓�
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		// 缁撳熬閮ㄥ垎
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 瀹氫箟鏈�鍚庢暟鎹垎闅旂嚎

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 瀹氫箟BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
				// json = JSONObject.fromObject(result,
				// JSONUtil.jsonFilter(QyUploadMedia.class));
				json = JSONObject.fromObject(result);
				if (result.contains("errcode")) {
					qyWeixinMsg.setMsg(json.get("errcode").toString());
				} else {
					ub = (QyUploadMedia) JSONObject.toBean(json, QyUploadMedia.class);
				}
			}
		} catch (IOException e) {
			qyWeixinMsg.setMsg("80002");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					qyWeixinMsg.setMsg("80002");
					e.printStackTrace();
				}
			}
		}

		return ub;
	}

	/**
	 * 涓婁紶姘镐箙濯掍綋鏂囦欢
	 * 
	 * @param File
	 *            file 鏂囦欢
	 * @param type
	 *            濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夛紝鏅�氭枃浠�(file)
	 *            鍥剧墖锛坕mage锛�:1MB锛屾敮鎸丣PG鏍煎紡 璇煶锛坴oice锛夛細2MB锛屾挱鏀鹃暱搴︿笉瓒呰繃60s锛屾敮鎸丄MR鏍煎紡
	 *            瑙嗛锛坴ideo锛夛細10MB锛屾敮鎸丮P4鏍煎紡 鏅�氭枃浠讹紙file锛夛細10MB
	 * @return
	 * 
	 */
	public QyUploadMedia uploadPermanentMedia(File file, Integer agentid, String type) {
		// 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
		JSONObject json = null;
		QyUploadMedia ub = null;
		String up_url = "https://qyapi.weixin.qq.com/cgi-bin/material/add_material?agentid=" + agentid + "&type=" + type
				+ "&access_token=" + accessToken;
		if (!file.exists()) {
			qyWeixinMsg.setMsg("80001");
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
				logger.info("涓婁紶姘镐箙鏂囦欢鑾峰彇mediaId:" + result);
				// json = JSONObject.fromObject(result,
				// JSONUtil.jsonFilter(QyUploadMedia.class));
				json = JSONObject.fromObject(result);
				if (result.contains("errcode")) {
					qyWeixinMsg.setMsg(json.get("errcode").toString());
				} else {
					ub = (QyUploadMedia) JSONObject.toBean(json, QyUploadMedia.class);
				}
			} else {
				qyWeixinMsg.setMsg("80002");
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
	 * 涓婁紶姘镐箙濯掍綋鏂囦欢(media_id)
	 * 
	 * @param File
	 *            file 鏂囦欢
	 * @param type
	 *            濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夛紝鏅�氭枃浠�(file)
	 *            鍥剧墖锛坕mage锛�:1MB锛屾敮鎸丣PG鏍煎紡 璇煶锛坴oice锛夛細2MB锛屾挱鏀鹃暱搴︿笉瓒呰繃60s锛屾敮鎸丄MR鏍煎紡
	 *            瑙嗛锛坴ideo锛夛細10MB锛屾敮鎸丮P4鏍煎紡 鏅�氭枃浠讹紙file锛夛細10MB
	 * @return media_id
	 * 
	 */
	public String uploadPermanentMediaFile(File file, Integer agentid, String type) {
		// 濯掍綋鏂囦欢绫诲瀷锛屽垎鍒湁鍥剧墖锛坕mage锛夈�佽闊筹紙voice锛夈�佽棰戯紙video锛夊拰缂╃暐鍥撅紙thumb锛�
		JSONObject json = null;
		String media_id = null;
		String up_url = "https://qyapi.weixin.qq.com/cgi-bin/material/add_material?agentid=" + agentid + "&type=" + type
				+ "&access_token=" + accessToken;
		if (!file.exists()) {
			qyWeixinMsg.setMsg("80001");
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
				logger.info("涓婁紶姘镐箙鏂囦欢鑾峰彇mediaId:" + result);
				json = JSONObject.fromObject(result);
				if (json.getInt("errcode") == 0) {
					media_id = json.getString("media_id");
				} else {
					qyWeixinMsg.setMsg(json.get("errcode").toString());
				}
			} else {
				qyWeixinMsg.setMsg("80002");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 閲婃斁杩炴帴
			postMethod.releaseConnection();
		}
		return media_id;
	}

	/**
	 * 涓嬭浇鏂囦欢
	 * 
	 * @param file_url
	 * @param media_id
	 */
	public String downloadFile(String media_id, String path, String newFileName) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id="
				+ media_id;
		System.setProperty("jsse.enableSNIExtension", "false");
		String picUrl = null;
		int BUFFER = 1024;
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setContentCharset("utf-8");
		boolean isUrl = true;
		String fileName = null;
		try {
			int status = client.executeMethod(httpGet);
			if (HttpStatus.SC_OK == status) {
				Header[] headers = httpGet.getResponseHeaders();

				for (Header header : headers) {
					if ("Content-disposition".equalsIgnoreCase(header.getName())) {
						isUrl = false;
						if (StringUtils.isBlank(newFileName)) {
							String tmp = header.getValue().split(";")[1].split("=")[1];
							fileName = tmp.substring(1, tmp.length() - 1);
						} else {
							fileName = newFileName;
						}
					}
				}

				if (!isUrl) {
					String newPath = path + File.separator + fileName;
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
						qyWeixinMsg.setMsg(json.get("errcode").toString());
					} else {
						picUrl = fileName;
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
								if (StringUtils.isBlank(newFileName)) {
									String tmp = header.getValue().split(";")[1].split("=")[1];
									fileName = tmp.substring(1, tmp.length() - 1);
								} else {
									fileName = newFileName;
								}
							}

							if (StringUtils.isNotBlank(fileName)) {
								String newPath = path + File.separator + fileName;
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
									qyWeixinMsg.setMsg(String.valueOf(json.get("errcode")));
								} else {
									picUrl = fileName;
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

	// ///////////////////////////鏂囦欢涓婁紶缁撴潫////////////////////////////////////

	// ///////////////////////////鍙戦�佹秷鎭紑濮�/////////////////////////////////////////
	/**
	 * 鍙戦�佹枃鏈�<text>娑堟伅
	 * 
	 * @param List
	 *            <String> touserList
	 *            鎴愬憳ID鍒楄〃锛堟秷鎭帴鏀惰�咃紝澶氫釜鎺ユ敹鑰呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�1000涓級銆傜壒娈婃儏鍐碉細鎸囧畾涓�
	 * @all锛屽垯鍚戝叧娉ㄨ浼佷笟搴旂敤鐨勫叏閮ㄦ垚鍛樺彂閫�
	 * @param List
	 *            <String> topartyList
	 *            閮ㄩ棬ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�100涓�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param List
	 *            <String> totagList 鏍囩ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅斻�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param Integer
	 *            agentid 浼佷笟搴旂敤鐨刬d锛屾暣鍨嬨�傚彲鍦ㄥ簲鐢ㄧ殑璁剧疆椤甸潰鏌ョ湅 (蹇呭～)
	 * @param contentMsg
	 *            娑堟伅鍐呭 (蹇呭～)
	 * 
	 * @return
	 * 
	 */
	public Boolean msgSendText(List<String> touserList, List<String> topartyList, List<String> totagList,
			Integer agentid, String contentMsg, Integer safe) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

		if (agentid == null) {
			success = false;
			qyWeixinMsg.setMsg("80011");
		} else if (StringUtils.isEmpty(contentMsg)) {
			success = false;
			qyWeixinMsg.setMsg("80012");
		}

		MsgSend ms = new MsgSend(touserList, topartyList, totagList, agentid, safe);
		JSONObject content = JSONObject.fromObject(ms.buildTextStr(contentMsg));

		if (!success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 鍙戦�佸獟浣�<image,voice,file>娑堟伅
	 * 
	 * @param List
	 *            <String> touserList
	 *            鎴愬憳ID鍒楄〃锛堟秷鎭帴鏀惰�咃紝澶氫釜鎺ユ敹鑰呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�1000涓級銆傜壒娈婃儏鍐碉細鎸囧畾涓�
	 * @all锛屽垯鍚戝叧娉ㄨ浼佷笟搴旂敤鐨勫叏閮ㄦ垚鍛樺彂閫�
	 * @param List
	 *            <String> topartyList
	 *            閮ㄩ棬ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�100涓�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param List
	 *            <String> totagList 鏍囩ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅斻�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param Integer
	 *            agentid 浼佷笟搴旂敤鐨刬d锛屾暣鍨嬨�傚彲鍦ㄥ簲鐢ㄧ殑璁剧疆椤甸潰鏌ョ湅 (蹇呭～)
	 * @param msgType
	 *            娑堟伅绫诲瀷锛屾暣鍨嬨��<image,voice,video,file> (蹇呭～)
	 * @param media_id
	 *            瀵瑰簲濯掍綋鏂囦欢id锛屽彲浠ヨ皟鐢ㄤ笂浼犲獟浣撴枃浠舵帴鍙ｈ幏鍙� (蹇呭～)
	 * 
	 * @return
	 * 
	 */
	public Boolean msgSendMedia(List<String> touserList, List<String> topartyList, List<String> totagList,
			Integer agentid, String msgType, String media_id, Integer safe) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

		if (agentid == null) {
			success = false;
			qyWeixinMsg.setMsg("80011");
		} else if (StringUtils.isEmpty(msgType)) {
			success = false;
			qyWeixinMsg.setMsg("80010");
		} else if (StringUtils.isEmpty(media_id)) {
			success = false;
			qyWeixinMsg.setMsg("80013");
		}

		MsgSend ms = new MsgSend(touserList, topartyList, totagList, agentid, safe);
		JSONObject content = null;
		if ("image".equals(msgType)) {
			content = JSONObject.fromObject(ms.buildImageStr(media_id));
		} else if ("voice".equals(msgType)) {
			content = JSONObject.fromObject(ms.buildVoiceStr(media_id));
		} else if ("file".equals(msgType)) {
			content = JSONObject.fromObject(ms.buildFileStr(media_id));
		}

		if (!success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 鍙戦�佸獟浣�<video>娑堟伅
	 * 
	 * @param List
	 *            <String> touserList
	 *            鎴愬憳ID鍒楄〃锛堟秷鎭帴鏀惰�咃紝澶氫釜鎺ユ敹鑰呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�1000涓級銆傜壒娈婃儏鍐碉細鎸囧畾涓�
	 * @all锛屽垯鍚戝叧娉ㄨ浼佷笟搴旂敤鐨勫叏閮ㄦ垚鍛樺彂閫�
	 * @param List
	 *            <String> topartyList
	 *            閮ㄩ棬ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�100涓�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param List
	 *            <String> totagList 鏍囩ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅斻�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param Integer
	 *            agentid 浼佷笟搴旂敤鐨刬d锛屾暣鍨嬨�傚彲鍦ㄥ簲鐢ㄧ殑璁剧疆椤甸潰鏌ョ湅 (蹇呭～)
	 * @param media_id
	 *            瀵瑰簲濯掍綋鏂囦欢id锛屽彲浠ヨ皟鐢ㄤ笂浼犲獟浣撴枃浠舵帴鍙ｈ幏鍙� (蹇呭～)
	 * @param title
	 *            瑙嗛娑堟伅鐨勬爣棰�
	 * @param description
	 *            瑙嗛娑堟伅鐨勬弿杩�
	 * 
	 * @return
	 * 
	 */
	public Boolean msgSendVideo(List<String> touserList, List<String> topartyList, List<String> totagList,
			Integer agentid, String media_id, String title, String description, Integer safe) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

		if (agentid == null) {
			success = false;
			qyWeixinMsg.setMsg("80011");
		} else if (StringUtils.isEmpty(media_id)) {
			success = false;
			qyWeixinMsg.setMsg("80013");
		}

		MsgSend ms = new MsgSend(touserList, topartyList, totagList, agentid, safe);
		JSONObject content = JSONObject.fromObject(ms.buildVideoStr(media_id, title, description));

		if (!success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 鍙戦�乶ews娑堟伅
	 * 
	 * @param List
	 *            <String> touserList
	 *            鎴愬憳ID鍒楄〃锛堟秷鎭帴鏀惰�咃紝澶氫釜鎺ユ敹鑰呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�1000涓級銆傜壒娈婃儏鍐碉細鎸囧畾涓�
	 * @all锛屽垯鍚戝叧娉ㄨ浼佷笟搴旂敤鐨勫叏閮ㄦ垚鍛樺彂閫�
	 * @param List
	 *            <String> topartyList
	 *            閮ㄩ棬ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�100涓�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param List
	 *            <String> totagList 鏍囩ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅斻�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param Integer
	 *            agentid 浼佷笟搴旂敤鐨刬d锛屾暣鍨嬨�傚彲鍦ㄥ簲鐢ㄧ殑璁剧疆椤甸潰鏌ョ湅 (蹇呭～)
	 * @param List
	 *            <MsgArticle>
	 *            listArticle鍥炬枃娑堟伅锛屼竴涓浘鏂囨秷鎭敮鎸�1鍒�10鏉″浘鏂�<title,description,url,picurl>
	 *            (蹇呭～)
	 * 
	 * @return
	 * 
	 */
	public Boolean msgSendNews(List<String> touserList, List<String> topartyList, List<String> totagList,
			Integer agentid, List<MsgArticle> listArticle, Integer safe) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

		if (agentid == null) {
			success = false;
			qyWeixinMsg.setMsg("80011");
		} else if (listArticle.size() == 0) {
			success = false;
			qyWeixinMsg.setMsg("80014");
		}

		MsgSend ms = new MsgSend(touserList, topartyList, totagList, agentid, safe);
		JSONObject content = JSONObject.fromObject(ms.buildNewsStr(listArticle));

		if (!success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 鍙戦�乵pnews娑堟伅
	 * 
	 * @param List
	 *            <String> touserList
	 *            鎴愬憳ID鍒楄〃锛堟秷鎭帴鏀惰�咃紝澶氫釜鎺ユ敹鑰呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�1000涓級銆傜壒娈婃儏鍐碉細鎸囧畾涓�
	 * @all锛屽垯鍚戝叧娉ㄨ浼佷笟搴旂敤鐨勫叏閮ㄦ垚鍛樺彂閫�
	 * @param List
	 *            <String> topartyList
	 *            閮ㄩ棬ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅旓紝鏈�澶氭敮鎸�100涓�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param List
	 *            <String> totagList 鏍囩ID鍒楄〃锛屽涓帴鏀惰�呯敤鈥榺鈥欏垎闅斻�傚綋touser涓篅all鏃跺拷鐣ユ湰鍙傛暟
	 * @param Integer
	 *            agentid 浼佷笟搴旂敤鐨刬d锛屾暣鍨嬨�傚彲鍦ㄥ簲鐢ㄧ殑璁剧疆椤甸潰鏌ョ湅 (蹇呭～)
	 * @param List
	 *            <MsgMpArticle> listArticle
	 *            鍥炬枃娑堟伅锛屼竴涓浘鏂囨秷鎭敮鎸�1鍒�10鏉″浘鏂�<title,thumb_media_id
	 *            ,author,content_source_url,content,digest,show_cover_pic> (蹇呭～)
	 * 
	 * @return
	 * 
	 */
	public Boolean msgSendMpNews(List<String> touserList, List<String> topartyList, List<String> totagList,
			Integer agentid, List<MsgMpArticle> listArticle, Integer safe) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

		if (agentid == null) {
			success = false;
			qyWeixinMsg.setMsg("80011");
		} else if (listArticle.size() == 0) {
			success = false;
			qyWeixinMsg.setMsg("80014");
		}

		if (!success) {
			for (int i = 0, len = listArticle.size(); i < len; i++) {
				MsgMpArticle mma = new MsgMpArticle();
				if (StringUtils.isEmpty(mma.getTitle())) {
					success = false;
					qyWeixinMsg.setMsg("80015");
				} else if (StringUtils.isEmpty(mma.getThumb_media_id())) {
					success = false;
					qyWeixinMsg.setMsg("80016");
				} else if (StringUtils.isEmpty(mma.getContent())) {
					success = false;
					qyWeixinMsg.setMsg("80017");
				}

				if (!success) {
					break;
				}

			}
		}

		MsgSend ms = new MsgSend(touserList, topartyList, totagList, agentid, safe);
		JSONObject content = JSONObject.fromObject(ms.buildMpNewsStr(listArticle));

		if (!success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	// ///////////////////////////鍙戦�佹秷鎭粨鏉�/////////////////////////////////////////

	// ///////////////////////////鍒涘缓鑿滃崟寮�濮�/////////////////////////////////////////
	public Boolean menuCreate(JSONObject content, Integer agentid) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken + "&&agentid="
				+ agentid;
		success = excuteParseBoolean(url, content);
		return success;
	}

	public Boolean menuDelete(JSONObject content, int agentid) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken + "&&agentid="
				+ agentid;
		success = excuteParseBoolean(url, content);
		return success;
	}

	public JSONArray menuSelect(JSONObject content, int agentid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken + "&&agentid="
				+ agentid;
		return excute(url, content).getJSONArray("button");
	}

	// ///////////////////////////鍒涘缓鑿滃崟缁撴潫/////////////////////////////////////////

	// ///////////////////////////鏍规嵁code鑾峰彇鎴愬憳淇℃伅////////////////////////

	/**
	 * 鏍规嵁code鑾峰彇鎴愬憳淇℃伅
	 * 
	 * @param code
	 * @param agentid
	 * @return
	 */
	public JSONObject getUserInfo(String code, Integer agentid) {
		Boolean success = false;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + accessToken + "&code="
				+ code + "&agentid=" + agentid;
		JSONObject object = null;
		if (agentid == null) {
			success = false;
			qyWeixinMsg.setMsg("80011");
		}
		if (!success) {
			object = excute(url);
		}
		return object;
	}

	public String getUserIdByCode(String code, Integer agentid) {
		Boolean success = false;
		String userId = null;
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + accessToken + "&code="
				+ code + "&agentid=" + agentid;
		if (agentid == null) {
			success = false;
			qyWeixinMsg.setMsg("80011");
		}

		if (!success) {
			JSONObject json = null;
			try {
				json = excute(url);
				if (json.containsKey("UserId")) {
					userId = json.get("UserId").toString();
				}
			} catch (Exception e) {
				qyWeixinMsg.setMsg(json.get("errcode").toString());
				e.printStackTrace();
			}
		}
		return userId;
	}

	/****************************
	 * 鍒涘缓浼氳瘽 start
	 ****************************************/

	/**
	 * 鍒涘缓浼氳瘽淇℃伅
	 * 
	 * @param String
	 *            chatid (蹇呭～)浼氳瘽id銆傚瓧绗︿覆绫诲瀷锛屾渶闀�32涓瓧绗︺�傚彧鍏佽瀛楃0-9鍙婂瓧姣峚-zA-Z,
	 *            濡傛灉鍊煎唴瀹逛负64bit鏃犵鍙锋暣鍨嬶細瑕佹眰鍊艰寖鍥村湪[1, 2^63)涔嬮棿锛孾2^63, 2^64)涓虹郴缁熷垎閰嶄細璇漣d鍖洪棿
	 * @param String
	 *            name (蹇呭～)浼氳瘽鏍囬
	 * @param String
	 *            owner(蹇呭～)绠＄悊鍛榰serid锛屽繀椤绘槸璇ヤ細璇漸serlist鐨勬垚鍛樹箣涓�
	 * @param String
	 *            userlist (蹇呭～) 浼氳瘽鎴愬憳鍒楄〃锛屾垚鍛樼敤userid鏉ユ爣璇嗐�備細璇濇垚鍛樺繀椤诲湪3浜烘垨浠ヤ笂锛�1000浜轰互涓�
	 * @return true/false
	 */
	public Boolean chatCreate(String chatid, String name, String owner, String[] userlist) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/chat/create?access_token=" + accessToken;
		JSONObject content = new JSONObject();
		if (StringUtils.isNotEmpty(name)) {
			content.put("name", name);
		}
		if (StringUtils.isNotBlank(chatid)) {
			content.put("chatid", chatid);
		}
		if (StringUtils.isNotEmpty(owner)) {
			content.put("owner", owner);
		}
		if (userlist != null) {
			content.put("userlist", JSONArray.fromObject(userlist));
		}
		return excuteParseBoolean(url, content);
	}

	/**
	 * 鑾峰彇浼氳瘽淇℃伅
	 * 
	 * @return JSONObject
	 */
	public JSONObject chatInfo(String chatid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/chat/get?access_token=" + accessToken + "&chatid=" + chatid;
		return excute(url).getJSONObject("chat_info");
	}

	/**
	 * 淇敼浼氳瘽淇℃伅
	 * 
	 * @param String
	 *            chatid (蹇呭～)浼氳瘽id銆傚瓧绗︿覆绫诲瀷锛屾渶闀�32涓瓧绗︺�傚彧鍏佽瀛楃0-9鍙婂瓧姣峚-zA-Z,
	 *            濡傛灉鍊煎唴瀹逛负64bit鏃犵鍙锋暣鍨嬶細瑕佹眰鍊艰寖鍥村湪[1, 2^63)涔嬮棿锛孾2^63, 2^64)涓虹郴缁熷垎閰嶄細璇漣d鍖洪棿
	 * @param String
	 *            name (蹇呭～)浼氳瘽鏍囬
	 * @param String
	 *            owner(蹇呭～)绠＄悊鍛榰serid锛屽繀椤绘槸璇ヤ細璇漸serlist鐨勬垚鍛樹箣涓�
	 * @param String
	 *            userlist (蹇呭～) 浼氳瘽鎴愬憳鍒楄〃锛屾垚鍛樼敤userid鏉ユ爣璇嗐�備細璇濇垚鍛樺繀椤诲湪3浜烘垨浠ヤ笂锛�1000浜轰互涓�
	 * @return true/false
	 */
	public Boolean chatUpdate(String chatid, String op_user, String name, String owner, String[] add_user_list,
			String[] del_user_list) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/chat/update?access_token=" + accessToken;
		Boolean success = true;

		JSONObject content = new JSONObject();

		if (StringUtils.isNotBlank(chatid)) {
			content.put("chatid", chatid);
		}

		if (StringUtils.isNotEmpty(op_user)) {
			content.put("op_user", op_user);
		}

		if (StringUtils.isNotEmpty(name)) {
			content.put("name", name);
		}

		if (StringUtils.isNotEmpty(owner)) {
			content.put("owner", owner);
		}

		if (add_user_list != null) {
			content.put("add_user_list", JSONArray.fromObject(add_user_list));
		}

		if (add_user_list != null) {
			content.put("del_user_list", JSONArray.fromObject(del_user_list));
		}

		if (success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 閫�鍑轰細璇濅俊鎭�
	 * 
	 * @return JSONObject
	 */
	public Boolean chatQuit(String chatid, String op_user) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/chat/quit?access_token=" + accessToken;

		Boolean success = true;

		JSONObject content = new JSONObject();

		if (StringUtils.isNotBlank(chatid)) {
			content.put("chatid", chatid);
		}

		if (StringUtils.isNotEmpty(op_user)) {
			content.put("op_user", op_user);
		}

		if (success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 娓呴櫎浼氳瘽鏈鐘舵��
	 * 
	 * @param String
	 *            op_user 浼氳瘽鎵�鏈夎�呯殑userid
	 * @param String
	 *            type 浼氳瘽绫诲瀷锛歴ingle|group锛屽垎鍒〃绀猴細缇よ亰|鍗曡亰
	 * @param String
	 *            id 浼氳瘽鍊硷紝涓簎serid|chatid锛屽垎鍒〃绀猴細鎴愬憳id|浼氳瘽id
	 * @return JSONObject
	 */
	public Boolean chatClearNotify(String op_user, String type, String id) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/chat/clearnotify?access_token=" + accessToken;

		Boolean success = true;
		JSONObject content = new JSONObject();

		if (StringUtils.isNotEmpty(op_user)) {
			content.put("op_user", op_user);
		}

		JSONObject chat = new JSONObject();

		if (StringUtils.isNotEmpty(type)) {
			chat.put("type", type);
		}

		if (StringUtils.isNotEmpty(id)) {
			chat.put("id", id);
		}

		if (chat != null) {
			content.put("chat", chat);
		}

		if (success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 鍙戞秷鎭�
	 * 
	 * @param String
	 *            type 鎺ユ敹浜虹被鍨嬶細single|group锛屽垎鍒〃绀猴細缇よ亰|鍗曡亰
	 * @param String
	 *            id 鎺ユ敹浜虹殑鍊硷紝涓簎serid|chatid锛屽垎鍒〃绀猴細鎴愬憳id|浼氳瘽id
	 * @param String
	 *            sender 鍙戦�佷汉
	 * @param String
	 *            msgtype 娑堟伅绫诲瀷锛屾鏃跺浐瀹氫负锛歵ext
	 * @param String
	 *            msgContent 娑堟伅鍐呭
	 * @return JSONObject
	 */
	public Boolean chatSend(String type, String id, String sender, String msgtype, String msgContent) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/chat/send?access_token=" + accessToken;

		Boolean success = true;

		JSONObject content = new JSONObject();
		JSONObject receiver = new JSONObject();
		if (StringUtils.isNotEmpty(type)) {
			receiver.put("type", type);
		}

		if (StringUtils.isNotEmpty(id)) {
			receiver.put("id", id);
		}

		if (receiver != null) {
			content.put("receiver", receiver);
		}

		if (StringUtils.isNotEmpty(sender)) {
			content.put("sender", sender);
		}

		if (StringUtils.isNotEmpty(msgtype)) {
			content.put("msgtype", msgtype);
			JSONObject text = new JSONObject();
			if (msgtype.equals("text")) {
				text.put("content", msgContent);
				content.put("text", text);
			} else if (msgtype.equals("image")) {
				text.put("media_id", msgContent);
				content.put("image", text);
			} else if (msgtype.equals("file")) {
				text.put("media_id", msgContent);
				content.put("file", text);
			}
		}
		if (success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/**
	 * 璁剧疆鎴愬憳鍏嶆墦鎵�
	 * 
	 * @param String
	 *            user_mute_list 鎴愬憳鏂版秷鎭厤鎵撴壈鍙傛暟锛屾暟缁勶紝鏈�澶ф敮鎸�10000涓垚鍛�
	 * @param String
	 *            userid 鎴愬憳UserID
	 * @param String
	 *            status 鍏嶆墦鎵扮姸鎬侊紝0鍏抽棴锛�1鎵撳紑,榛樿涓�0銆傚綋鎵撳紑鏃舵墍鏈夋秷鎭笉鎻愰啋锛涘綋鍏抽棴鏃讹紝浠ユ垚鍛樺浼氳瘽鐨勮缃负鍑嗐��
	 * @return JSONObject
	 */
	public Boolean chatSetmute(List<Map<String, Object>> user_mute_list) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/chat/setmute?access_token=" + accessToken;

		Boolean success = true;

		JSONObject content = new JSONObject();
		if (user_mute_list != null) {
			content.put("user_mute_list", JSONArray.fromObject(user_mute_list));
		}

		if (success) {
			success = excuteParseBoolean(url, content);
		}

		return success;
	}

	/****************************
	 * 鍒涘缓浼氳瘽 end
	 ****************************************/

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
	private Boolean excuteParseBoolean(String url, JSONObject content) {
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
	private JSONObject excute(String url, JSONObject content) {
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
	private JSONObject exc(String url, JSONObject content) {
		JSONObject json = null;
		try {
			PostMethod post = new PostMethod(url);
			post.setRequestHeader("Connection", "Keep-Alive");
			post.setRequestHeader("Cache-Control", "no-cache");
			if (content != null) {
				post.setRequestEntity(new StringRequestEntity(content.toString(), null, "utf-8"));
				post.getParams().setContentCharset("utf-8");
			}

			int status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String respStr = post.getResponseBodyAsString();
				json = JSONObject.fromObject(respStr);
				logger.info("鎺ュ彛璋冪敤杩斿洖json鍐呭:" + respStr);
				if (json.get("errcode") != null && json.getInt("errcode") != 0) {
					qyWeixinMsg.setMsg(json.get("errcode").toString());
				} else {
					qyWeixinMsg.setMsg("0");
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

	public QyWeixinMsg getQyWeixinMsg() {
		return qyWeixinMsg;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
