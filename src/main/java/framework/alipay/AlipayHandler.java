package framework.alipay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import framework.alipay.config.AlipayConfig;
import framework.alipay.util.AlipayNotify;
import framework.alipay.util.AlipaySubmit;



public class AlipayHandler {

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	@SuppressWarnings("unused")
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	/**
	 * 移动支付接口地址
	 */
	@SuppressWarnings("unused")
	private static final String ALIPAY_PHONE_GATEWAY = "http://wappaygw.alipay.com/service/rest.htm?";

	// 服务器异步通知页面路径//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
	private String returnUrl = "return_url.json";
	// 页面跳转同步通知页面路径 //需http://格式的完整路径，不能加?id=123这类自定义参数
	private String notifyUrl = "notify_url.json";

	private String alipayMsg;
	
	//超时时间
	private	String it_b_pay ="";

			//钱包token
	private	String extern_token ="";

	//防钓鱼时间戳
	private String anti_phishing_key = "";
	//若要使用请调用类文件submit中的query_timestamp函数

	//客户端的IP地址
	private String exter_invoke_ip = "";
	//非局域网的外网IP地址，如：221.0.0.1

	/**
	 * 构建支付对象
	 */
	public AlipayHandler() {

	}

	/**
	 * 构建支付对象 (采用RSA加密)  手机网站配置文件
	 * 
	 * @param  partner 合作身份者ID，以2088开头由16位纯数字组成的字符串
	 * @param  privateKey 商户的私钥
	 * @param  sellerId  收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	 * 
	 */
	public AlipayHandler(String partner,String privateKey,String sellerId) {
		AlipayConfig.setPartner(partner);
		AlipayConfig.setPrivate_key(privateKey);
		AlipayConfig.setSeller_id(sellerId);
		AlipayConfig.setSign_type("RSA");
	}
	
	
	/**
	 * 构建支付对象 (采用MD5加密)  支付宝即时到帐
	 * 
	 * @param  partner 合作身份者ID，以2088开头由16位纯数字组成的字符串
	 * @param  key 商户的私钥
	 * @param  seller_email  收款支付宝账号
	 * @param  sign_type  签名方式 不需修改
	 * 
	 */
	public AlipayHandler(String partner,String key,String seller_email,String sign_type) {
		AlipayConfig.setPartner(partner);
		AlipayConfig.setKey(key);
		AlipayConfig.setSeller_email(seller_email);
		AlipayConfig.setSign_type("MD5");
	}
	
	/**
	 * 阿里获取支付页面html （支付宝即时到帐）(采用MD5加密)
	 * 
	 * @param out_trade_no
	 *            商户订单号
	 * @param subject
	 *            订单名称
	 * @param body
	 *            订单描述
	 * @param total_fee
	 *            付款金额
	 * @return
	 */
	public String getPayHtmlText(String out_trade_no, String subject,
			String body, String total_fee) {
		//支付类型
		String payment_type = "1";
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_email", AlipayConfig.seller_email);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notifyUrl);
		sParaTemp.put("return_url", returnUrl);
		
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		
		sParaTemp.put("show_url", returnUrl);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		
		
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");

		return sHtmlText;
	}
	

	/**
	 * 手机移动端获取支付页面html （手机网站支付）(采用RSA加密)
	 * 
	 * @param out_trade_no
	 *            商户订单号
	 * @param subject
	 *            订单名称
	 * @param body
	 *            订单描述
	 * @param total_fee
	 *            付款金额
	 * @return
	 */
	public String getPonePayHtmlText(String out_trade_no, String subject,
			String body, String total_fee) {
		
		//支付类型
		String payment_type = "1";
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notifyUrl);
		sParaTemp.put("return_url", returnUrl);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("show_url", returnUrl);
		sParaTemp.put("body", body);
		sParaTemp.put("it_b_pay", it_b_pay);
		sParaTemp.put("extern_token", extern_token);
		
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");

		return sHtmlText;
	}

	/**
	 * 返回信息
	 * 
	 * @param requestParams
	 *            支付宝GET过来反馈信息
	 * @param out_trade_no
	 *            支付宝交易号
	 * @param trade_no
	 *            订单号
	 * @param trade_status
	 *            总金额
	 * @return true 支付成功false支付失败
	 */
	@SuppressWarnings("rawtypes")
	public static Boolean delReturn(Map requestParams, String out_trade_no,
			String trade_no, String trade_status) {
		Boolean success = false;
			Map<String,String> params = new HashMap<String,String>();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				
				params.put(name, valueStr);
			}
	
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			
			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(params);
			
			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码
	
				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
				}
				
				//该页面可做页面美工编辑
				success = true;
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
	
				//////////////////////////////////////////////////////////////////////////////////////////
			}else{
				//该页面可做页面美工编辑
				success = false;
			}
		return success;
	}
	/**
	 * 通知信息
	 * 
	 * @param requestParams
	 *            支付宝GET过来反馈信息
	 * @param out_trade_no
	 *            支付宝交易号
	 * @param trade_no
	 *            订单号
	 * @param trade_status
	 *            总金额
	 * @return true 支付成功false支付失败
	 */
	@SuppressWarnings("rawtypes")
	public static Boolean delNotify(Map requestParams, String out_trade_no,
			String trade_no, String trade_status) {
		Boolean success = false;
		//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
	
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
	
			if(AlipayNotify.verify(params)){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码
	
				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					success = true;
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//付款完成后，支付宝系统发送该交易状态通知
					success = true;
				}
				success = true;
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			}else{//验证失败
				success=false;
			}
		return success;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}


	public String getAlipayMsg() {
		return alipayMsg;
	}

	public void setAlipayMsg(String alipayMsg) {
		this.alipayMsg = alipayMsg;
	}
}
