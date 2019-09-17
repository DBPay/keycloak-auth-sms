package ai.dbpay.keycloak.authenticator.sms.provider.spi;

import java.util.HashMap;
import java.util.Map;

public class SMSServiceConfig {

    private boolean isProxy;
    private String proxyUrl;
    private String proxyPort;
    private String codeLength;

    private Map<String, Map<String, String>> providersConfig = new HashMap<String, Map<String, String>>();

    public boolean isProxy() {
        return isProxy;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxy(boolean proxy) {
        isProxy = proxy;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(String codeLength) {
        this.codeLength = codeLength;
    }

    public Map<String, Map<String, String>> getProvidersConfig() {
        return providersConfig;
    }

    public void setProvidersConfig(Map<String, Map<String, String>> providersConfig) {
        this.providersConfig = providersConfig;
    }

    public Map<String, String> getProviderConfig(String providerName) {
        return providersConfig.get(providerName);
    }

    public String getProviderConfigValue(String providerName, String configKey) {
        return providersConfig.get(providerName).get(configKey);
    }
}
