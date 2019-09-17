package ai.dbpay.keycloak.authenticator.sms;

import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSAuthContstants;
import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;

import static org.keycloak.provider.ProviderConfigProperty.BOOLEAN_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

/**
 * 短信验证码验证登录
 *
 * @jacky.yong
 */
public class SMSAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "deepblue-auth-sms";

    private static final SMSAuthenticator SINGLETON = new SMSAuthenticator();

    private static final Logger logger = Logger.getLogger(SMSAuthenticatorFactory.class.getPackage().getName());

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };

    private static final List<ProviderConfigProperty> configProperties;

    static {
        // TWILIO
        configProperties = ProviderConfigurationBuilder
                .create()

//                .property()
//                .name(SMSAuthContstants.CONFIG_TWILIO_SMS_API_KEY)
//                .label("API-KEY")
//                .type(STRING_TYPE)
//                .helpText("")
//                .add()

                .property()
                .name(SMSAuthContstants.CONFIG_PROXY_FLAG)
                .label("Proxy Enabled")
                .type(BOOLEAN_TYPE)
                .defaultValue(false)
                .helpText("")
                .add()

                .property()
                .name(SMSAuthContstants.CONFIG_PROXY_URL)
                .label("Proxy URL")
                .type(STRING_TYPE)
                .helpText("")
                .add()

                .property()
                .name(SMSAuthContstants.CONFIG_PROXY_PORT)
                .label("Proxy Port")
                .type(STRING_TYPE)
                .helpText("")
                .add()

                .property()
                .name(SMSAuthContstants.CONFIG_CODE_LENGTH)
                .label("SMS Code Length")
                .type(STRING_TYPE)
                .helpText("")
                .defaultValue(4)
                .add()

                .build();

    }

    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    public String getId() {
        return PROVIDER_ID;
    }

    public void init(Scope scope) {
        logger.debug("Method [init]");
    }

    public void postInit(KeycloakSessionFactory factory) {
        logger.debug("Method [postInit]");
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    public String getHelpText() {
        return "SMS Authenticate(support SMS Providers: Twilio、253.com、yunpian.com.";
    }

    public String getDisplayType() {
        return "SMS Authentication";
    }

    public String getReferenceCategory() {
        logger.debug("Method [getReferenceCategory]");
        return "sms-auth-code";
    }

    public boolean isConfigurable() {
        return true;
    }

    public Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES == null ? null : (Requirement[]) REQUIREMENT_CHOICES.clone();
    }

    public boolean isUserSetupAllowed() {
        return true;
    }

    public void close() {
        logger.debug("<<<<<<<<<<<<<<< SMSAuthenticatorFactory close");
    }
}
