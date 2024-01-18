package org.moonshot.server.global.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.global.external.discord.model.JsonObject;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCallUtil {

    public static void callDiscordAppenderPostAPI(String urlString, JsonObject json) throws IOException, NoSuchAlgorithmException, KeyManagementException {
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, createTrustManagers(), new java.security.SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        HostnameVerifier allHostsValid = (hostname, session) -> true;
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        try (OutputStream stream = connection.getOutputStream()) {
            stream.write(json.toString().getBytes());
            stream.flush();

            connection.getInputStream().close();
            connection.disconnect();

        } catch (IOException ioException) {
            throw ioException;
        }
    }

    public static void callDiscordAppenderSignInAPI(JsonObject json) throws IOException {
        URL url = new URL("https://discord.com/api/webhooks/1197531070241968261/h18e24N_r18WGafR6syjSVX8568oO6grRY6jt21V2_vy-LVuy47jVUmMnB-sTFx3CZ-S");
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        try (OutputStream stream = connection.getOutputStream()) {
            stream.write(json.toString().getBytes());
            stream.flush();

            connection.getInputStream().close();
            connection.disconnect();

        } catch (IOException ioException) {
            throw ioException;
        }
    }



    /** URL Connection SSL ignore **/
//    public static TrustManager[] createTrustManagers() {
//        TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {
//            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) { }
//            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) { }
//            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
//        }};
//
//        return trustAllCerts;
//    }
}
