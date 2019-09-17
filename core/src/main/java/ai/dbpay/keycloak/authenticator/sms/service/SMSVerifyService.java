package ai.dbpay.keycloak.authenticator.sms.service;

import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSResult;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSServiceConfig;

/**
 * 短信验证码服务
 */
public interface SMSVerifyService {

    /**
     * 设置服务配置
     *
     * @param serviceConfig
     */
    void setServiceConfig(SMSServiceConfig serviceConfig);

    /**
     * 发送验证码
     *
     * @param mobile
     * @return
     */
    SMSResult sendSMSCode(String mobile);
    
    boolean verifySMSCode(String mobile, String code);

    /**
     * 验证验证码
     *
     * @param providerName
     * @param mobile
     * @param code
     * @return
     */
    boolean verifySMSCode(String providerName, String mobile, String code);

}