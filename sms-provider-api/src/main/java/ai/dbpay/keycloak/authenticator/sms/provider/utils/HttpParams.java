package ai.dbpay.keycloak.authenticator.sms.provider.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * SMS Parameters
 */
public class HttpParams {
    private Map<String, String> header;

    private Map<String, String> data;

    public HttpParams() {
        data = new HashMap<String, String>();
        header = new HashMap<String, String>();

        setHeader("Accept", "application/json");
        setHeader("Content-Type", "application/json");

    }

    public void setHeader(String key, String value) {
        this.header.put(key, value);
    }

    public void setAttribute(String key, String value) {
        this.data.put(key, value);
    }

    public Map<String, String> toMap() {
        return this.data;
    }

    public Map<String, String> getHeader() {
        return this.header;
    }

    public String toJSON() {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : toMap().entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toString();
    }
}
