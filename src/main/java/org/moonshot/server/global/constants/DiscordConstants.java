package org.moonshot.server.global.constants;

import org.springframework.beans.factory.annotation.Value;

public class DiscordConstants {

    @Value("${logging.discord.signin.webhook-uri}")
    public static String signInWebhookUrl;
    @Value("${logging.discord.signin.username}")
    public static String botName;
    @Value("${logging.discord.signin.img-url}")
    public static String botImgUrl;

}
