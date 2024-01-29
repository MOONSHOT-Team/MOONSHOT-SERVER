package org.moonshot.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.discord.model.JsonObject;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCallUtil {

    public static void callDiscordAppenderPostAPI(String urlString, JsonObject json) throws IOException {
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

    public static void callDiscordAppenderSignInAPI(String signinWebhookUrl, JsonObject json) throws IOException {
        URL url = new URL(signinWebhookUrl);
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

}
