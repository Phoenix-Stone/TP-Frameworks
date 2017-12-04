package framework.redreward;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 企业商户公用类
 * @author Cristo
 *
 */
public class BusinessEnterpriseHandler {
    //签名文件路径
	private String apiclientCertPath = "";
	//发红包账号
	private String mchId = "";
	//红包签名
	private String signKey = "";
	//发红包微信公众账号appid
	private String wxappid = "";

	// 类型
	private String type = "PKCS12";

	public BusinessEnterpriseHandler() {

	}

	public BusinessEnterpriseHandler(String mchId, String wxappid, String signKey,
			String apiclientCertPath) {
		this.apiclientCertPath = apiclientCertPath;
		this.mchId = mchId;
		this.wxappid = wxappid;
		this.signKey = signKey;
	}
	
	/**
	 * 发送企业付款
	 * @param mapConfig
	 * @return
	 * @throws Exception
	 */
	public JSONObject sendCompanyPay(SortedMap<String, String> mapConfig) throws Exception {
		SortedMap<String, String> map = new TreeMap<String, String>();
		map.put("partner_trade_no", getMchId() + mapConfig.get("partner_trade_no"));
		map.put("mchid", getMchId());
		map.put("mch_appid", getWxappid());
		map.put("spbill_create_ip", "127.0.0.1");
		map.put("check_name", "OPTION_CHECK");
		map.put("nonce_str", "50780e0cca98c8c8e814883e5caa672e");
		map.putAll(mapConfig);
		
		JSONObject returnObj = send(map, "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
		
		return returnObj;
	}
	
	/**
	 * 发送红包
	 * @param mapConfig
	 * @return
	 * @throws Exception
	 */
	public JSONObject sendRedReward(SortedMap<String, String> mapConfig) throws Exception {
		SortedMap<String, String> map = new TreeMap<String, String>();
		map.put("mch_billno", getMchId() + mapConfig.get("mch_billno"));
		map.put("mch_id", getMchId());
		map.put("wxappid", getWxappid());
		map.put("client_ip", "127.0.0.1");
		map.put("nonce_str", "50780e0cca98c8c8e814883e5caa672e");
		map.putAll(mapConfig);
		
		JSONObject returnObj = send(map, "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");
		return returnObj;
	}

    /**
     * 发送信息
     * @param map
     * @param url
     * @return
     * @throws Exception
     */
	public JSONObject send(SortedMap<String, String> map, String url) throws Exception {
		JSONObject returnObj = null;
		Boolean success = true;
		
		/*
		 * 加载加密签名文件
		 */
		KeyStore keyStore = KeyStore.getInstance(this.type);
		FileInputStream instream = null;
		try {
			File apiclientCertFile = new File(this.apiclientCertPath);
			if(apiclientCertFile.exists()) {
				instream = new FileInputStream(apiclientCertFile);
				keyStore.load(instream, this.mchId.toCharArray());
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			instream.close();
		}
		
		/**
		 * 远程验证合法性 
		 */
		CloseableHttpClient httpclient = null;
		if(success) {
			try {
				// Trust own CA and all self-signed certs
				SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, this.mchId.toCharArray()).build();
				
				// Allow TLSv1 protocol only
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
				httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			} catch (Exception e) {
				success = false;
				e.printStackTrace();
			}
		}

		if(success) {
			try {
				HttpPost httpget = new HttpPost(url);
				HttpEntity entity1 = new StringEntity(generalContent(map), "UTF-8");
				httpget.setEntity(entity1);
				CloseableHttpResponse response = httpclient.execute(httpget);
				
				try {
					HttpEntity entity = response.getEntity();
					
					if (entity != null) {
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
						StringBuffer textBuffer = new StringBuffer();
						String text;
						while ((text = bufferedReader.readLine()) != null) {
							textBuffer.append(new String(text.getBytes(), "utf-8"));
							System.out.println(new String(text.getBytes(), "utf-8"));
						}
						
						returnObj  = JSONObject.fromObject(new XMLSerializer().read(textBuffer.toString()).toString());
					}
					
					EntityUtils.consume(entity);
				} catch (Exception e) {
					success = false;
					e.printStackTrace();
				} finally {
					response.close();
				}
			} catch (Exception e) {
				success = false;
				e.printStackTrace();
			} finally {
				httpclient.close();
			}
		}
		
		return returnObj;
	}

	/**
	 * 生成发送字符串
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String generalContent(SortedMap<String, String> map){
		StringBuffer sb = new StringBuffer();
		
		try {
			String sign = Sha1Util.createMD5Sign(map, this.signKey).toUpperCase();
			sb.append("<xml>");
			Set es = map.entrySet();
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				String v = (String) entry.getValue();
				sb.append("<" + k + "><![CDATA[" + v + "]]></" + k + ">");
			}

			sb.append("<sign><![CDATA[" + sign + "]]></sign>");
			sb.append("</xml>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public String getApiclientCertPath() {
		return apiclientCertPath;
	}

	public void setApiclientCertPath(String apiclientCertPath) {
		this.apiclientCertPath = apiclientCertPath;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWxappid() {
		return wxappid;
	}

	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}

}
