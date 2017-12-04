package framework.aliyun.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;

public class AliyunSmsUtil {
	  
    public void sample() {        
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "your accessKey", "your accessSecret");
        
        try {
        	DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
        	IAcsClient client = new DefaultAcsClient(profile);
        	SingleSendSmsRequest request = new SingleSendSmsRequest();
            request.setSignName("控制台创建的签名名称");
            request.setTemplateCode("控制台创建的模板CODE");
            request.setParamString("{}");
            request.setRecNum("接收号码");
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            System.out.println(httpResponse);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
 