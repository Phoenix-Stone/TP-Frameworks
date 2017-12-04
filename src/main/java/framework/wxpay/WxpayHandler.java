
package framework.wxpay;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import framework.wxpay.utils.GetWxOrderno;
import framework.wxpay.utils.RequestHandler;
import framework.wxpay.utils.Sha1Util;
import framework.wxpay.utils.TenpayUtil;

public class WxpayHandler {
	private String appid;
	private String appsecret;
	private String key;
	private String mchid;
	private final String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	public WxpayHandler(String appid, String appsecret, String key,
			String mchid) {
		super();
		this.appid = appid;
		this.appsecret = appsecret;
		this.key = key;
		this.mchid = mchid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param body
	 *            商品信息
	 * @param money
	 *            总价
	 * @param attach
	 *            附加信息
	 * @param openid
	 *            支付人openid
	 * @return 支付js链接
	 */
	public String getAlipayStr(HttpServletRequest request,
			HttpServletResponse response, String body, String money,
			String attach, String openid, String out_trade_no, String notify_url) {
		String returnStr = "";
		Boolean success = false;

		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());

		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 设备号 非必输
		// String device_info="";
		// 随机数
		String nonce_str = strTime + strRandom;

		// 商户订单号
		// out_trade_no = appid + out_trade_no;

		// 订单生成的机器 IP
		String spbill_create_ip = request.getRemoteAddr();
		// 订 单 生 成 时 间 非必输
		// String time_start ="";
		// 订单失效时间 非必输
		// String time_expire = "";
		// 商品标记 非必输
		// String goods_tag = "";

		String trade_type = "APP"; // JSAPI
		// 非必输
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mchid);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", money);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openid);

		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appid, appsecret, key);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mchid + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" + "<attach>" + attach
				+ "</attach>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<total_fee>" + money + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openid + "</openid>"
				+ "</xml>";

		// 生成预付ID
		String prepay_id = "";
		try {
			prepay_id = GetWxOrderno.getPayNo(unifiedorderUrl, xml);
			if (!prepay_id.equalsIgnoreCase("")) {
				success = true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timeStamp = Sha1Util.getTimeStamp();
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", appid);
		finalpackage.put("timeStamp", timeStamp);
		finalpackage.put("nonceStr", nonce_str);
		finalpackage.put("prepayid", prepay_id);
		finalpackage.put("partnerId", mchid);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");

		if (success) {
			returnStr = "{\"appId\":\"" + appid + "\",\"timeStamp\":\""
					+ timeStamp + "\",\"nonceStr\":\"" + nonce_str
					+ "\",\"prepayid\":\"" + prepay_id + "\",\"partnerId\":\""
					+ mchid + "\",\"package\":\"" + packages
					+ "\",\"signType\":\"MD5\",\"paySign\":\""
					+ reqHandler.createSign(finalpackage) + "\"}";
		}

		return returnStr;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param body 商品信息
	 * @param money 总价
	 * @param attach 附加信息
	 * @param openid 支付人openid
	 * @return 支付js链接
	 */
	public Map<String, String> getWxAlipay(HttpServletRequest request, HttpServletResponse response, String body, String money, String attach, String openid, String out_trade_no, String notify_url){
		Map<String, String> ret = new HashMap<String, String>();
		Boolean success = false;
		
		String currTime = TenpayUtil.getCurrTime();
		//8位日期
		String strTime = currTime.substring(8, currTime.length());
		
		//四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		//设备号   非必输
//		String device_info="";
		//随机数 
		String nonce_str = strTime + strRandom;
		
		//商户订单号
//		out_trade_no = appid+out_trade_no;
		
		
		//订单生成的机器 IP
		String spbill_create_ip = request.getRemoteAddr();
		//订 单 生 成 时 间   非必输
//		String time_start ="";
		//订单失效时间      非必输
//		String time_expire = "";
		//商品标记   非必输
//		String goods_tag = "";
		
		String trade_type = "JSAPI";
		//非必输
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);  
		packageParams.put("mch_id", mchid);  
		packageParams.put("nonce_str", nonce_str);  
		packageParams.put("body", body);  
		packageParams.put("attach", attach);  
		packageParams.put("out_trade_no", out_trade_no);  
		
		//这里写的金额为1 分到时修改
		packageParams.put("total_fee", money);  
		packageParams.put("spbill_create_ip", spbill_create_ip);  
		packageParams.put("notify_url", notify_url);  
		packageParams.put("trade_type", trade_type);  
		packageParams.put("openid", openid);  

		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appid, appsecret, key);
		
		String sign = reqHandler.createSign(packageParams);
		String xml="<xml>"+
				   "<appid>"+appid+"</appid>"+
				   "<mch_id>"+mchid+"</mch_id>"+
				   "<nonce_str>"+nonce_str+"</nonce_str>"+
				   "<sign>"+sign+"</sign>"+
				   "<body><![CDATA["+body+"]]></body>"+
				   "<attach>"+attach+"</attach>"+
				   "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
				   "<total_fee>"+money+"</total_fee>"+
				   "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
				   "<notify_url>"+notify_url+"</notify_url>"+
				   "<trade_type>"+trade_type+"</trade_type>"+
				   "<openid>"+openid+"</openid>"+
				   "</xml>";
		
		//生成预付ID
		String prepay_id="";
		try {
			prepay_id = GetWxOrderno.getPayNo(unifiedorderUrl, xml);
			if(!prepay_id.equalsIgnoreCase("")){
				success = true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timeStamp = Sha1Util.getTimeStamp();
		String prepay_id2 = "prepay_id="+prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", appid);  
		finalpackage.put("timeStamp", timeStamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		
		if(success) {
			ret.put("timeStamp", timeStamp);
			ret.put("nonceStr", nonce_str);
			ret.put("packages", packages);
			ret.put("signType", "MD5");
			ret.put("paySign", reqHandler.createSign(finalpackage));
		}
		
		return ret;
	}

	/**
	 * 组装返回值<br>
	 * 
	 * @param reqHandler
	 * @param finalpackage
	 * @return
	 */
	public String getReturnStr(RequestHandler reqHandler,
			SortedMap<String, String> finalpackage) {
		JSONObject object = new JSONObject();
		object.put("appId", appid);
		object.put("timeStamp", finalpackage.get("timeStamp"));
		object.put("nonceStr", finalpackage.get("nonceStr"));
		object.put("package", finalpackage.get("package"));
		object.put("partnerId", mchid);
		object.put("signType", "MD5");
		object.put("paySign", reqHandler.createSign(finalpackage));
		return object.toString();
	}
}
