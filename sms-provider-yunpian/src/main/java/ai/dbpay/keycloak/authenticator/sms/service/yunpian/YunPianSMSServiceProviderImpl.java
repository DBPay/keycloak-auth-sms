package ai.dbpay.keycloak.authenticator.sms.service.yunpian;

import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSResult;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSServiceProvider;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.jboss.logging.Logger;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;
import java.util.Map;

/**
 * 云片provider
 */
public class YunPianSMSServiceProviderImpl extends SMSServiceProvider {

    private static final Logger LOGGER = Logger.getLogger(YunPianSMSServiceProviderImpl.class.getPackage().getName());

    private String apiKey;

    private Integer weight;

    private boolean enabled;

    private String providerName = "yunpian";

    private YunpianClient client;

    public YunPianSMSServiceProviderImpl() {
    }

    public YunPianSMSServiceProviderImpl(String apiKey, Integer weight, boolean enabled) {
        this.apiKey = apiKey;
        this.weight = weight;
        this.enabled = enabled;

        client = new YunpianClient(apiKey).init();
    }

    @Override
    public void afterServiceConfigUpdated() {

    }

    public SMSResult sendSMSCode(String mobile) {
        //发送短信API
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, mobile);
        param.put(YunpianClient.TEXT, "code");

        Result<SmsSingleSend> r = client.sms().single_send(param);
        return convertSendResult(r.getData());
    }

    public boolean verifySMSCode(String mobile, String code) {
        return false;
    }


    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }

    public SMSResult convertSendResult(SmsSingleSend r) {
        SMSResult result = new SMSResult();
        if (null == r) {
            result.setSuccess(false);
            result.setCode("null result");
            result.setError("client result is null");
            return result;
        }
        if (r.getCode() == 0) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setCode("" + r.getCode());
            result.setError(r.getMsg());
        }
        return result;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getProviderName() {
        return providerName;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
