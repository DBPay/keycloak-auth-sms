package ai.dbpay.keycloak.authenticator.sms.provider.spi;

public class SMSAuthContstants {

    public static final String ATTR_PHONE_NUMBER = "phoneNumber";

    // 代理配置
    public static final String CONFIG_PROXY_FLAG = "verifySMS.proxy-flg";
    public static final String CONFIG_PROXY_URL = "verifySMS.proxy-url";
    public static final String CONFIG_PROXY_PORT = "verifySMS.proxy-port";
    public static final String CONFIG_CODE_LENGTH = "verifySMS.code-length";

    public static final String TEMPLATE_SMS_VALIDATION = "sms-validation.ftl";

    public static final String TEMPLATE_SMS_VALIDATION_ERROR = "sms-validation-error.ftl";

}
