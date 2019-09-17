package ai.dbpay.keycloak.authenticator.sms.service;

import ai.dbpay.keycloak.authenticator.sms.provider.spi.AbstractSMSServiceImpl;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSResult;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSServiceConfig;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSServiceProvider;
import org.jboss.logging.Logger;

public class SMSVerifyServiceImpl extends AbstractSMSServiceImpl implements SMSVerifyService {

    private static final Logger LOGGER = Logger.getLogger(SMSVerifyServiceImpl.class.getPackage().getName());

    public SMSVerifyServiceImpl() {
        super(null);
    }

    public SMSVerifyServiceImpl(SMSServiceConfig config) {
        super(config);
    }

    public SMSResult sendSMSCode(String mobile) {
        SMSResult result = null;
        boolean providerFound = false;

        for (SMSServiceProvider sp : getEnabledServiceProviders()) {
            providerFound = true;

            result = sp.sendSMSCode(mobile);
            result.setProviderName(sp.getProviderName());

            if (result.isSuccess()) {
                break;
            }
        }

        if (!providerFound) {
            result = new SMSResult();
            result.setSuccess(false);
            result.setCode("NO Supported Provider found for Notification! ");
        }

        return result;
    }

    public boolean verifySMSCode(String mobile, String code) {
        return false;
    }


    public boolean verifySMSCode(String providerName, String mobile, String code) {
        boolean result = false;
        boolean providerFound = false;

        for (SMSServiceProvider sp : getEnabledServiceProviders()) {

            if (sp.getProviderName().equalsIgnoreCase(providerName)) {
                providerFound = true;
                result = sp.verifySMSCode(mobile, code);
            }

        }

        return result;
    }
}
