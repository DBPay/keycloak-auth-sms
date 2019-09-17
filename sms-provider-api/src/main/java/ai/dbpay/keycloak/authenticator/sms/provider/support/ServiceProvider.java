package ai.dbpay.keycloak.authenticator.sms.provider.support;

/**
 * 第三方服务商抽象接口
 * <p>
 */
public interface ServiceProvider extends Comparable<ServiceProvider> {

    /**
     * 获取服务权重
     */
    Integer getWeight();

    /**
     * 获取服务商名称
     */
    String getProviderName();

    /**
     * 服务商是否可用
     */
    boolean isEnabled();

}
