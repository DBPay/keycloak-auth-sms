package ai.dbpay.keycloak.authenticator.sms.provider.spi;

public class SMSResult {

    // 错误消息
    private String error;

    // 错误码
    private String code;

    // 是否成功
    private boolean success;

    // 使用的通道名称
    private String providerName;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
