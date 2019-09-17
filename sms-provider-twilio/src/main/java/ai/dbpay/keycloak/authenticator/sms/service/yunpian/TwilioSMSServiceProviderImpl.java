package ai.dbpay.keycloak.authenticator.sms.service.yunpian;

import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSResult;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSServiceConfig;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSServiceProvider;
import ai.dbpay.keycloak.authenticator.sms.provider.utils.HttpParams;
import org.jboss.logging.Logger;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

import static ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSAuthContstants.CONFIG_CODE_LENGTH;
import static ai.dbpay.keycloak.authenticator.sms.provider.utils.HttpHelper.METHOD_GET;
import static ai.dbpay.keycloak.authenticator.sms.provider.utils.HttpHelper.METHOD_POST;
import static ai.dbpay.keycloak.authenticator.sms.service.yunpian.TwilioContstants.CONFIG_TWILIO_SMS_API_KEY;

public class TwilioSMSServiceProviderImpl extends SMSServiceProvider {

    private static final Logger LOGGER = Logger.getLogger(TwilioSMSServiceProviderImpl.class.getPackage().getName());

    public static final String PROVIDER_NAME = "twilio";

    public static final String DEFAULT_API_URI = "https://api.authy.com";
    public static final String PHONE_VERIFICATION_API_PATH = "/protected/json/phones/verification/";

    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }

    public Integer getWeight() {
        return null;
    }

    public String getProviderName() {
        return PROVIDER_NAME;
    }

    public boolean isEnabled() {
        return false;
    }

    private String apiKey;

    private String codeLen;

    public TwilioSMSServiceProviderImpl() {
    }

    public void afterServiceConfigUpdated() {
        SMSServiceConfig serviceConfig = getServiceConfig();
        this.apiKey = serviceConfig.getProviderConfigValue(PROVIDER_NAME, CONFIG_TWILIO_SMS_API_KEY);
        this.codeLen = serviceConfig.getProviderConfigValue(PROVIDER_NAME, CONFIG_CODE_LENGTH);
    }

    public SMSResult sendSMSCode(String mobile) {


        HttpParams data = new HttpParams();
        data.setAttribute("phone_number", mobile);
        data.setAttribute("country_code", "81"); // JAPAN
        data.setAttribute("via", "ai/dbpay/keycloak/authenticator/sms"); // SMS
        data.setAttribute("code_length", codeLen);

        data.setHeader("X-Authy-API-Key", apiKey);

        boolean request = httpHelper.request(METHOD_POST, DEFAULT_API_URI + PHONE_VERIFICATION_API_PATH + "start", data);

        SMSResult result = new SMSResult();
        result.setSuccess(request);

        return result;
    }

    public boolean verifySMSCode(String telNum, String code) {

        HttpParams data = new HttpParams();
        data.setAttribute("phone_number", telNum);
        data.setAttribute("country_code", "81");
        data.setAttribute("verification_code", code);
        data.setAttribute("code_length", codeLen);

        data.setHeader("X-Authy-API-Key", apiKey);

        return httpHelper.request(METHOD_GET, DEFAULT_API_URI + PHONE_VERIFICATION_API_PATH + "check", data);
    }


}
