package ai.dbpay.keycloak.authenticator.sms;

import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSAuthContstants;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSResult;
import ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSServiceConfig;
import ai.dbpay.keycloak.authenticator.sms.service.SMSVerifyService;
import ai.dbpay.keycloak.authenticator.sms.service.SMSVerifyServiceImpl;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;

import static ai.dbpay.keycloak.authenticator.sms.provider.spi.SMSAuthContstants.*;

/**
 * SMS认证
 */
public class SMSAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(SMSAuthenticator.class.getPackage().getName());

    private SMSVerifyService service;

    public SMSAuthenticator(SMSServiceConfig config) {
        service = new SMSVerifyServiceImpl(config);
    }

    public SMSAuthenticator() {
        service = new SMSVerifyServiceImpl();
    }

    private String getConfigString(AuthenticatorConfigModel config, String configName) {
        String value = null;
        if (config.getConfig() != null) {
            value = config.getConfig().get(configName);
        }
        return value;
    }

    public SMSServiceConfig retriveConfig(AuthenticatorConfigModel model) {
        SMSServiceConfig config = new SMSServiceConfig();

        config.setProxy(Boolean.valueOf(getConfigString(model, CONFIG_PROXY_FLAG)));
        config.setProxyUrl(getConfigString(model, CONFIG_PROXY_URL));
        config.setProxyPort(getConfigString(model, CONFIG_PROXY_PORT));
        config.setCodeLength(getConfigString(model, CONFIG_CODE_LENGTH));

        // getConfigString(config, SMSAuthContstants.CONFIG_TWILIO_SMS_API_KEY)

        return config;
    }

    /**
     * 初始化调用认证. 这个方法检查Http请求来决定该请求是否满足该认证器的必要条件. 如果不满足，应该返回一个challenge response。
     *
     * @param context
     */
    public void authenticate(AuthenticationFlowContext context) {
        logger.debug("Method [authenticate]");

        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        service.setServiceConfig(retriveConfig(config));

        UserModel user = context.getUser();
        String phoneNumber = getPhoneNumber(user);
        logger.debugv("phoneNumber : {0}", phoneNumber);

        if (phoneNumber != null) {
            SMSResult result = service.sendSMSCode(phoneNumber);
            if (result.isSuccess()) {
                Response challenge = context.form().createForm(SMSAuthContstants.TEMPLATE_SMS_VALIDATION);
                context.challenge(challenge);
            } else {
                Response challenge = context.form().addError(new FormMessage("sendSMSCodeErrorMessage"))
                        .createForm(SMSAuthContstants.TEMPLATE_SMS_VALIDATION_ERROR);
                context.challenge(challenge);
            }

        } else {
            Response challenge = context.form().addError(new FormMessage("missingTelNumberMessage"))
                    .createForm(SMSAuthContstants.TEMPLATE_SMS_VALIDATION_ERROR);
            context.challenge(challenge);
        }

    }

    public void action(AuthenticationFlowContext context) {
        logger.debug("Method [action]");

        MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
        String enteredCode = inputData.getFirst("smsCode");

        UserModel user = context.getUser();
        String phoneNumber = getPhoneNumber(user);
        logger.debugv("phoneNumber : {0}", phoneNumber);

        // SendSMS
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        service.setServiceConfig(retriveConfig(config));

        if (service.verifySMSCode(phoneNumber, enteredCode)) {
            logger.info("verify code check : OK");
            context.success();
        } else {
            Response challenge = context.form()
                    .setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
                    .addError(new FormMessage("invalidSMSCodeMessage")).createForm("sms-validation-error.ftl");
            context.challenge(challenge);
        }

    }

    public boolean requiresUser() {
        logger.debug("Method [requiresUser]");
        return false;
    }

    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        logger.debug("Method [configuredFor]");
        return false;
    }

    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    public void close() {
        logger.debug("<<<<<<<<<<<<<<< SMSAuthenticator close");
    }

    /**
     * 获取用户手机号码
     *
     * @param user
     * @return
     */
    private String getPhoneNumber(UserModel user) {
        List<String> phoneNumberList = user.getAttribute(SMSAuthContstants.ATTR_PHONE_NUMBER);
        if (phoneNumberList != null && !phoneNumberList.isEmpty()) {
            return phoneNumberList.get(0);
        }
        return null;
    }
}