package ai.dbpay.keycloak.authenticator.sms.provider.utils;

import com.google.gson.Gson;
import org.jboss.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Http工具类
 */
public class HttpHelper {

    private static final Logger logger = Logger.getLogger(HttpHelper.class.getPackage().getName());

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    private boolean isProxy;
    private String proxyUrl;
    private String proxyPort;

    public HttpHelper() {
        this.isProxy = false;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public void setProxy(boolean proxy) {
        isProxy = proxy;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public HttpHelper(String isProxy, String proxyUrl, String proxyPort) {
        this.isProxy = Boolean.parseBoolean(isProxy);
        this.proxyUrl = proxyUrl;
        this.proxyPort = proxyPort;
    }

    public boolean request(String method,
                           String absURL,
                           HttpParams data) {

        String result = requestString(method, absURL, data);

        return result != null;
    }

    public <T> T requestJSON(String method,
                             String absURL,
                             HttpParams data, Class<T> responseClazz) {

        String result = requestString(method, absURL, data);

        if (result != null) {
            T p = new Gson().fromJson(result, responseClazz);
            return p;
        }

        return null;
    }

    public String requestString(String method,
                                String absURL,
                                HttpParams data) {
        String result = null;

        HttpsURLConnection conn;
        InputStream in = null;
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();

            if (method.equals(METHOD_GET)) {
                sb.append(prepareGet(data));
            }

            URL url = new URL(absURL + sb.toString());

            if (isProxy) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(this.proxyUrl, Integer.parseInt(this.proxyPort)));
                conn = (HttpsURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpsURLConnection) url.openConnection();
            }

            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            setHttpHeader(data, conn);

            if (method.equals(METHOD_POST)) {
                writeJson(conn, data);
            }

            final int resStatus = conn.getResponseCode();
            logger.infov("RESPONSE STATUS : {0}", resStatus);

            if (resStatus == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                String line;
                StringBuffer resultBuffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    logger.infov("RESPONSE DETAIL : {0}", line);
                    resultBuffer.append(line);
                }
                result = resultBuffer.toString();
            }

        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }

        return result;
    }

    public void setHttpHeader(HttpParams data, HttpsURLConnection conn) {
        Map<String, String> header = data.getHeader();
        Iterator<String> iterator = header.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            conn.setRequestProperty(next, header.get(next));
        }
    }

    private void writeJson(HttpURLConnection connection, HttpParams data) {
        if (data == null) {
            return;
        }

        OutputStream os = null;
        BufferedWriter output = null;
        try {
            os = connection.getOutputStream();
            output = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            output.write(data.toJSON());
            output.flush();
            output.close();
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }

    }

    public String prepareGet(HttpParams data) {

        if (data == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder("?");
        Map<String, String> params = data.toMap();

        boolean first = true;

        for (Map.Entry<String, String> s : params.entrySet()) {

            if (first) {
                first = false;
            } else {
                sb.append('&');
            }
            try {
                sb.append(URLEncoder.encode(s.getKey(), "UTF-8")).append("=")
                        .append(URLEncoder.encode(s.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("Encoding not supported" + e.getMessage());
            }
        }

        return sb.toString();
    }

}
