/*
 * This file is part of HuskChat, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.huskchat.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.william278.huskchat.channel.Channel;
import net.william278.huskchat.discord.DiscordHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for loading and storing plugin settings
 */
@SuppressWarnings("FieldMayBeFinal")
@Getter
@Configuration
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Settings {

    static final String CONFIG_HEADER = """
            ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
            ┃       HuskChat - Config      ┃
            ┃    Developed by William278   ┃
            ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
            ┣╸ Information: https://william278.net/project/huskchat/
            ┣╸ Config Help: https://william278.net/docs/huskchat/config-files/
            ┗╸ Documentation: https://william278.net/docs/huskchat/""";

    @Comment("Locale of the default language file to use. Docs: https://william278.net/docs/huskclaims/translations")
    private String language = Locales.DEFAULT_LOCALE;

    @Comment("Whether to automatically check for plugin updates on startup")
    private boolean checkForUpdates = true;

    @Comment("Whether to handle chat packets directly for better 1.19+ support (may cause rare compatibility issues)")
    private boolean usePacketListening = true;

    @Comment("Placeholder settings")
    private PlaceholderSettings placeholder = new PlaceholderSettings();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PlaceholderSettings {
        @Comment("Use PlaceholderAPI. If you're on Bungee/Velocity, this requires PAPIProxyBridge installed")
        private boolean usePapi = true;

<<<<<<< HEAD
    // Message command config
    private boolean doMessageCommand;
    private boolean doGroupMessages;
    private int maxGroupMessageSize;
    private List<String> messageCommandAliases;
    private List<String> replyCommandAliases;
    private String inboundMessageFormat;
    private String outboundMessageFormat;
    private String groupInboundMessageFormat;
    private String groupOutboundMessageFormat;
    private boolean logPrivateMessages;
    private boolean censorPrivateMessages;
    private String messageLogFormat;
    private List<String> messageCommandRestrictedServers;

    // Social spy
    private boolean doSocialSpyCommand;
    private String socialSpyFormat;
    private String socialSpyGroupFormat;
    private List<String> socialSpyCommandAliases;


    // Local spy
    private boolean doLocalSpyCommand;
    private String localSpyFormat;
    private List<String> excludedLocalSpyChannels;
    private List<String> localSpyCommandAliases;


    // Broadcast command
    private boolean doBroadcastCommand;
    private List<String> broadcastCommandAliases;
    private String broadcastMessageFormat;
    private boolean logBroadcasts;
    private String broadcastLogFormat;


    // Chat filters
    private Map<String, List<ChatFilter>> chatFilters;

    // Join and Quit messages
    private boolean doJoinMessages;
    private String joinMessageFormat;
    private boolean doQuitMessages;
    private String quitMessageFormat;
    private Channel.BroadcastScope joinQuitBroadcastScope;


    // Discord integration
    private boolean doDiscordIntegration;
    private Map<String, URL> webhookUrls;
    private WebHook.Format webhookFormat;
    private boolean useSpicord;
    private String discordUsernameFormat;
    private Map<String, String> spicordReceiveChannelMap;
    private Map<String, String> spicordSendChannelMap;

    // HuskTown integration
    private boolean doHuskTownIntegration;

    // Server names
    private Map<String, String> serverNameReplacement;


    public Settings(@NotNull YamlDocument configFile) {
        this.loadConfig(configFile);
=======
        @Comment("If using PAPIProxyBridge, how long to cache placeholders for (in milliseconds)")
        private long cacheTime = 3000;
>>>>>>> 98eb365d15bae08775e873b3b6b0a1d30fe99244
    }

    @Comment("Message comamnd settings")
    private MessageSettings messageCommand = new MessageSettings();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MessageSettings {
        @Comment("Whether to enable the /msg command")
        private boolean enabled = true;

        @Comment("List of command aliases for /msg")
        @Getter(AccessLevel.NONE)
        private List<String> msgAliases = List.of("/msg", "/m", "/tell", "/whisper", "/w", "/pm");

        @Comment("List of command aliases for /reply")
        @Getter(AccessLevel.NONE)
        private List<String> replyAliases = List.of("/reply", "/r");

        @Comment("Whether to apply censorship filters on private messages")
        private boolean censor = false;

        @Comment("Whether to log private messages to the console")
        private boolean logToConsole = true;

        @Comment("Logging format for private messages")
        private String logFormat = "[MSG] [%sender% -> %receiver%]: ";

        @Comment("Group private message settings")
        private GroupSettings groupMessages = new GroupSettings();

        @Getter
        @Configuration
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class GroupSettings {
            @Comment("Whether to enable group private messages (/msg Player1,Player2,...)")
            private boolean enabled = true;

            @Comment("Maximum amount of players in a group message")
            private int maxSize = 10;
        }

<<<<<<< HEAD
        // HuskTown integration
        doHuskTownIntegration = configFile.getBoolean("husktown.enabled", false);

        // Server name replacement
        serverNameReplacement = new LinkedHashMap<>();
        Section serverNameReplacementSection = configFile.getSection("server_name_replacement");
        if (serverNameReplacementSection != null) {
            for (String s : serverNameReplacementSection.getRoutesAsStrings(false)) {
                serverNameReplacement.put(s, serverNameReplacementSection.getString(s));
            }
=======
        @Comment("Formats for private messages (uses MineDown)")
        private MessageFormat format = new MessageFormat();

        @Getter
        @Configuration
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class MessageFormat {
            private String inbound = "&e&l%name% &8→ &e&lYou&8: &f";
            private String outbound = "&e&lYou &8→ &e&l%name%&8: &f";
            private String groupInbound = "&e&l%name% &8→ &e&lYou[₍₊%group_amount_subscript%₎](gray show_text=&7%group_members%)&8: &f";
            private String groupOutbound = "&e&lYou &8→ &e&l%name%[₍₊%group_amount_subscript%₎](gray show_text=&7%group_members%)&8: &f";
        }

        @Comment("(Bungee/Velocity only) List of servers where private messages cannot be sent")
        private List<String> restrictedServers = List.of();

        @NotNull
        public List<String> getMsgAliases() {
            return formatCommands(msgAliases);
        }

        @NotNull
        public List<String> getReplyAliases() {
            return formatCommands(replyAliases);
        }


    }

    @Comment("Social spy settings (see other users' private messages)")
    private SocialSpySettings socialSpy = new SocialSpySettings();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SocialSpySettings {
        private boolean enabled = true;
        private String format = "&e[Spy] &7%name% &8→ &7%receiver_name%:%spy_color% ";
        private String groupFormat = "&e[Spy] &7%name% &8→ &7%receiver_name% [₍₊%group_amount_subscript%₎](gray show_text=&7%group_members% suggest_command=/msg %group_members_comma_separated% ):%spy_color% ";
        @Getter(AccessLevel.NONE)
        private List<String> socialspyAliases = List.of("/socialspy", "/ss");

        @NotNull
        public List<String> getSocialspyAliases() {
            return formatCommands(socialspyAliases);
>>>>>>> 98eb365d15bae08775e873b3b6b0a1d30fe99244
        }
    }


    @Comment("(Bungee/Velocity only) Local spy settings (see local messages on other servers)")
    private LocalSpySettings localSpy = new LocalSpySettings();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LocalSpySettings {
        private boolean enabled = true;
        private String format = "&e[Spy] &7[%channel%] %name%&8:%spy_color% ";
        @Getter(AccessLevel.NONE)
        private List<String> localspyAliases = List.of("/localspy", "/ls");
        @Comment("List of channels to exclude from local spy")
        private List<String> excludedLocalChannels = List.of();

        @NotNull
        public List<String> getLocalspyAliases() {
            return formatCommands(localspyAliases);
        }
    }

    @Comment("Broadcast command settings")
    private BroadcastSettings broadcastCommand = new BroadcastSettings();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BroadcastSettings {
        private boolean enabled = true;
        @Getter(AccessLevel.NONE)
        private List<String> broadcastAliases = List.of("/broadcast", "/alert");
        private String format = "&6[Broadcast]&e ";
        private boolean logToConsole = true;
        private String logFormat = "[BROADCAST]: ";

        @NotNull
        public List<String> getBroadcastAliases() {
            return formatCommands(broadcastAliases);
        }
    }

    @Comment("Join and quit message settings")
    private JoinQuitSettings joinAndQuitMessages = new JoinQuitSettings();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class JoinQuitSettings {

        @Comment({"Use the \"huskchat.join_message.[text]\" permission to override this.",
                "Use the \"huskchat.silent_join\" permission to silence for a user."})
        private ConnectionMessage join = new ConnectionMessage(true, "&e%name% joined the network");

        @Comment({"Use the \"huskchat.quit_message.[text]\" permission to override this.",
                "Use the \"huskchat.silent_quit\" permission to silence for a user."})
        private ConnectionMessage quit = new ConnectionMessage(true, "&e%name% left the network");

        @Comment("Note that on Velocity/Bungee, PASSTHROUGH modes won't cancel local join/quit messages")
        private Channel.BroadcastScope broadcastScope = Channel.BroadcastScope.GLOBAL;

        @Getter
        @Configuration
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class ConnectionMessage {
            private boolean enabled;
            private String format;
        }
    }

    @Comment("Discord integration settings. Docs: https://william278.net/docs/huskchat/discord-hook")
    private DiscordSettings discord = new DiscordSettings();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DiscordSettings {
        @Comment("Enable hooking into Discord via Webhooks and/or Spicord")
        private boolean enabled = false;

        @Comment("Discord message format style (INLINE or EMBEDDED)")
        private DiscordHook.Format formatStyle = DiscordHook.Format.INLINE;

        @Comment("Send messages in channels to a webhook by mapped URL")
        @Getter(AccessLevel.NONE)
        private Map<String, String> channelWebhooks = new HashMap<>();

        @Comment("Whether to hook into Spicord for two-way chat")
        private SpicordSettings spicord = new SpicordSettings();

        @Getter
        @Configuration
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class SpicordSettings {
            @Comment("Requires Spicord installed and \"huskchat\" added to the \"addons\" in config.toml")
            private boolean enabled = true;

            @Comment("Format of Discord users in-game. Note this doesn't support other placeholders")
            private String usernameFormat = "@%discord_handle%";

            @Comment("Send in-game messages on these channels to a specified Discord channel (by numeric ID)")
            private Map<String, String> receiveChannelMap = new HashMap<>(Map.of(
                    "global", "123456789012345678"
            ));

            @Comment("Send Discord messages on these channels (by numeric ID) to a specified in-game channel")
            private Map<String, String> sendChannelMap = new HashMap<>(Map.of(
                    "123456789012345678", "global"
            ));
        }

        @NotNull
        @Unmodifiable
        public Map<String, URL> getChannelWebhooks() throws IllegalStateException {
            final Map<String, URL> webhookMap = new HashMap<>();
            for (String channel : channelWebhooks.keySet()) {
                try {
                    webhookMap.put(channel, new URI(channelWebhooks.get(channel)).toURL());
                } catch (Throwable e) {
                    throw new IllegalStateException("Invalid URL for Discord webhook: " + channelWebhooks.get(channel));
                }
            }
            return webhookMap;
        }
    }

    @Comment("Custom names to display wherever you use the \"%server%\" placeholder instead of their default name")
    private Map<String, String> serverNameReplacement = new HashMap<>(
            Map.of("very-long-server-name", "VLSN")
    );

    @NotNull
<<<<<<< HEAD
    public String getLanguage() {
        return language;
    }

    public boolean doCheckForUpdates() {
        return checkForUpdates;
    }

    public boolean doPlaceholderAPI() {
        return doPlaceholderAPI;
    }

    public long getPapiProxyBridgeCacheTime() {
        return papiProxyBridgeCacheTime;
    }

    @NotNull
    public String getDefaultChannel() {
        return defaultChannel;
    }

    @NotNull
    public Map<String, String> getServerDefaultChannels() {
        return serverDefaultChannels;
    }

    @NotNull
    public Map<String, Channel> getChannels() {
        return channels;
    }

    @NotNull
    public String getChannelLogFormat() {
        return channelLogFormat;
    }

    @NotNull
    public List<String> getChannelCommandAliases() {
        return channelCommandAliases;
    }

    public boolean isDoMessageCommand() {
        return doMessageCommand;
    }

    public boolean doGroupMessages() {
        return doGroupMessages;
    }

    public int getMaxGroupMessageSize() {
        return maxGroupMessageSize;
    }

    @NotNull
    public List<String> getMessageCommandAliases() {
        return messageCommandAliases;
    }

    @NotNull
    public List<String> getReplyCommandAliases() {
        return replyCommandAliases;
    }

    @NotNull
    public String getInboundMessageFormat() {
        return inboundMessageFormat;
    }

    @NotNull
    public String getOutboundMessageFormat() {
        return outboundMessageFormat;
    }

    @NotNull
    public String getGroupInboundMessageFormat() {
        return groupInboundMessageFormat;
    }

    @NotNull
    public String getGroupOutboundMessageFormat() {
        return groupOutboundMessageFormat;
    }

    public boolean doLogPrivateMessages() {
        return logPrivateMessages;
    }

    public boolean isCensorPrivateMessages() {
        return censorPrivateMessages;
    }

    @NotNull
    public String getMessageLogFormat() {
        return messageLogFormat;
    }

    @NotNull
    public List<String> getMessageCommandRestrictedServers() {
        return messageCommandRestrictedServers;
    }

    public boolean doSocialSpyCommand() {
        return doSocialSpyCommand;
    }

    @NotNull
    public String getSocialSpyFormat() {
        return socialSpyFormat;
    }

    @NotNull
    public String getSocialSpyGroupFormat() {
        return socialSpyGroupFormat;
    }

    @NotNull
    public List<String> getSocialSpyCommandAliases() {
        return socialSpyCommandAliases;
    }

    public boolean doLocalSpyCommand() {
        return doLocalSpyCommand;
    }

    @NotNull
    public String getLocalSpyFormat() {
        return localSpyFormat;
    }

    /**
     * Returns if a channel is excluded from local spy messages
     *
     * @param channel {@link Channel} to check
     * @return {@code true} if the channel should be excluded from /localspy; {@code false} otherwise
     */
    public boolean isLocalSpyChannelExcluded(@NotNull Channel channel) {
        for (String excludedChannel : excludedLocalSpyChannels) {
            if (excludedChannel.equals(channel.getId())) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public List<String> getLocalSpyCommandAliases() {
        return localSpyCommandAliases;
    }

    public boolean isDoBroadcastCommand() {
        return doBroadcastCommand;
    }

    @NotNull
    public List<String> getBroadcastCommandAliases() {
        return broadcastCommandAliases;
    }

    @NotNull
    public String getBroadcastMessageFormat() {
        return broadcastMessageFormat;
    }

    public boolean doLogBroadcasts() {
        return logBroadcasts;
    }

    @NotNull
    public String getBroadcastLogFormat() {
        return broadcastLogFormat;
    }

    public boolean doJoinMessages() {
        return doJoinMessages;
    }

    @NotNull
    public String getJoinMessageFormat() {
        return joinMessageFormat;
    }

    public boolean doQuitMessages() {
        return doQuitMessages;
    }

    @NotNull
    public String getQuitMessageFormat() {
        return quitMessageFormat;
    }

    @NotNull
    public Channel.BroadcastScope getJoinQuitBroadcastScope() {
        return joinQuitBroadcastScope;
    }

    @NotNull
    public Map<String, List<ChatFilter>> getChatFilters() {
        return chatFilters;
    }

    public boolean doDiscordIntegration() {
        return doDiscordIntegration;
    }

    public boolean doHuskTownIntegration() {
        return doHuskTownIntegration;
    }

    @NotNull
    public Map<String, URL> getWebhookUrls() {
        return webhookUrls;
    }

    @NotNull
    public WebHook.Format getDiscordMessageFormat() {
        return webhookFormat;
    }

    public boolean useSpicord() {
        return useSpicord;
    }

    @NotNull
    public String getDiscordUsernameFormat() {
        return discordUsernameFormat;
    }

    @NotNull
    public Map<String, String> getSpicordReceiveChannelMap() {
        return spicordReceiveChannelMap;
    }

    @NotNull
    public Map<String, String> getSpicordSendChannelMap() {
        return spicordSendChannelMap;
    }

    @NotNull
    public Map<String, String> getServerNameReplacement() {
        return serverNameReplacement;
=======
    public static List<String> formatCommands(@NotNull List<String> rawCommands) {
        return rawCommands.stream().map(c -> c.startsWith("/") ? c.substring(1) : c).toList();
>>>>>>> 98eb365d15bae08775e873b3b6b0a1d30fe99244
    }

}
