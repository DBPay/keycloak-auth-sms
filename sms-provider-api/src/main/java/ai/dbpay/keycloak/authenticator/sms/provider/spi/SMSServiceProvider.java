package ai.dbpay.keycloak.authenticator.sms.provider.spi;


import ai.dbpay.keycloak.authenticator.sms.provider.support.AbstractServiceProvider;
import ai.dbpay.keycloak.authenticator.sms.provider.utils.HttpHelper;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

public abstract class SMSServiceProvider extends AbstractServiceProvider {
    protected HttpHelper httpHelper;

    protected SMSServiceConfig serviceConfig;

    public void setServiceConfig(SMSServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;

        this.httpHelper = new HttpHelper();
        this.httpHelper.setProxy(serviceConfig.isProxy());
        this.httpHelper.setProxyPort(serviceConfig.getProxyPort());
        this.httpHelper.setProxyUrl(serviceConfig.getProxyUrl());

        afterServiceConfigUpdated();
    }

    public void afterServiceConfigUpdated() {

    }

    public SMSServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    /**
     * 发送文本短信(含国内国外)
     *
     * @param mobile
     * @return
     */
    public abstract SMSResult sendSMSCode(String mobile);

    /**
     * 验证文本短信(含国内国外)
     *
     * @param mobile
     * @param code
     * @return
     */
    public abstract boolean verifySMSCode(String mobile, String code);


    /**
     * 获取供应商配置
     *
     * @return
     */
    public abstract List<ProviderConfigProperty> getConfigProperties();

}