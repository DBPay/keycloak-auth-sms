package ai.dbpay.keycloak.authenticator.sms.provider.support;


public abstract class AbstractServiceProvider implements ServiceProvider {

    public int compareTo(ServiceProvider o) {
        return getWeight().compareTo(o.getWeight());
    }

}

