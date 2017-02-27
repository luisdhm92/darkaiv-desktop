package org.uclv.darkaiv.dspace;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class TestHelper {

    public static final String COMMUNITIES = "/communities";
    public static final String TOP_COMMUNITIES = "/communities/top-communities";
    public static final String COLLECTIONS = "/collections";
    public static final String ITEMS = "/items";
    public static final String BITSTREAMS = "/bitstreams";

    public String server;
    public String user;
    public String password;

    public TestHelper() throws MalformedURLException {
        File file = new File("./config/dspace/");
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle resource = ResourceBundle.getBundle("dspace", Locale.getDefault(), loader);

        server = resource.getString("dspace.server.url");
        user = resource.getString("dspace.server.user");
        password = resource.getString("dspace.server.password");
    }

    public String getServer() {
        return server;
    }

    public String loginAdmin() {
        Client client = createClient();
        WebResource webResource = client.resource(getServer() + "/login");
        ClientResponse response = webResource.accept("application/json").post(ClientResponse.class, new User(user, password));
        String token = response.getEntity(String.class);
        return token;
    }

    /**
     * Create HTTPS client.
     *
     * @return Client with prepared HTTPS connection.
     */
    public Client createClient() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {

            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {

            }
        }};

        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, certs, new java.security.SecureRandom());
            config.getProperties().put(
                    HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    new HTTPSProperties(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname,
                                SSLSession session) {
                            return true;
                        }
                    }, context));
        } catch (Exception e) {
            // printException(e);
        }
        Client client = Client.create(config);
        return client;
    }

    /**
     * Get path of files in resource folder.
     *
     * @param name Name of file.
     * @return String with full path of file.
     */
    public String getPath(String name) {
        try {
            return URLDecoder.decode(TestHelper.class.getClassLoader().getResource(name).getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
