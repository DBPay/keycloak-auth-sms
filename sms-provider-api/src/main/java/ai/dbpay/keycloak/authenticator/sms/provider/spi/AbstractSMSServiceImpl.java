package ai.dbpay.keycloak.authenticator.sms.provider.spi;

import java.util.*;

public abstract class AbstractSMSServiceImpl {

    private List<SMSServiceProvider> serviceProviders;

    private List<SMSServiceProvider> enabledServiceProviders;

    private ServiceLoader<SMSServiceProvider> serviceLoader;

    private SMSServiceConfig serviceConfig;

    public SMSServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(SMSServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
        wrapServiceProvider(serviceLoader.iterator());
    }

    public AbstractSMSServiceImpl(SMSServiceConfig config) {
        serviceLoader = ServiceLoader.load(SMSServiceProvider.class);
        setServiceConfig(config);
    }

    protected void reloadServiceProviders() {
        serviceLoader.reload();
        wrapServiceProvider(serviceLoader.iterator());
    }

    public List<SMSServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public List<SMSServiceProvider> getEnabledServiceProviders() {
        return enabledServiceProviders;
    }

    public void wrapServiceProvider(Iterator<SMSServiceProvider> iterator) {

        serviceProviders = new ArrayList<SMSServiceProvider>();

        while (iterator.hasNext()) {
            SMSServiceProvider next = iterator.next();
            next.setServiceConfig(serviceConfig);
            serviceProviders.add(next);
        }

        Collections.sort(serviceProviders);

        enabledServiceProviders = new ArrayList<SMSServiceProvider>();

        for (SMSServiceProvider sp : serviceProviders) {
            if (sp.isEnabled()) {
                enabledServiceProviders.add(sp);
            }
        }

    }

}
