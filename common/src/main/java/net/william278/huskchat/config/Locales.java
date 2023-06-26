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

import de.themoep.minedown.adventure.MineDown;
import de.themoep.minedown.adventure.MineDownParser;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.william278.huskchat.HuskChat;
import net.william278.huskchat.channel.Channel;
import net.william278.huskchat.player.ConsolePlayer;
import net.william278.huskchat.player.Player;
import net.william278.huskchat.player.PlayerCache;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Locales {

    private final HuskChat plugin;
    private final Map<String, String> locales = new HashMap<>();

    public Locales(@NotNull HuskChat plugin) {
        this.plugin = plugin;
        final String languagePath = "languages/" + plugin.getSettings().getLanguage() + ".yml";
        try (InputStream stream = Objects.requireNonNull(plugin.getResource(languagePath))) {
            final YamlDocument document = YamlDocument.create(new File(plugin.getDataFolder(),
                    "messages-" + plugin.getSettings().getLanguage() + ".yml"), stream);
            locales.clear();
            for (String messageId : document.getRoutesAsStrings(false)) {
                locales.put(messageId, document.getString(messageId, ""));
            }
        } catch (Throwable e) {
            plugin.log(Level.SEVERE, "Failed to load messages file", e);
        }
    }

    public String getRawMessage(@NotNull String messageID) {
        return locales.get(messageID);
    }

    // todo use Adventure's provided serializer?
    @NotNull
    public String extractMineDownLinks(String string) {
        String[] in = string.split("\n");
        StringBuilder out = new StringBuilder();

        // This regex extracts the text and url, only supports one link per line.
        String regex = "[^\\[\\]() ]*\\[([^()]+)]\\([^()]+open_url=(\\S+).*\\)";

        for (int i = 0; i < in.length; i++) {
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(in[i]);

            if (m.find()) {
                // match 0 is the whole match, 1 is the text, 2 is the url
                out.append(in[i].replace(m.group(0), ""));
                out.append(m.group(2));
            } else {
                out.append(in[i]);
            }

            if (i + 1 != in.length) {
                out.append("\n");
            }
        }

        return out.toString();
    }

    public void sendMessage(@NotNull Player player, @NotNull String messageId, @NotNull String... replacements) {
        String message = getRawMessage(messageId);

        // Don't send empty messages
        if (message == null || message.isEmpty()) {
            return;
        }

        // Replace placeholders
        int replacementIndexer = 1;
        for (String replacement : replacements) {
            String replacementString = "%" + replacementIndexer + "%";
            message = message.replace(replacementString, replacement);
            replacementIndexer = replacementIndexer + 1;
        }

        if (player instanceof ConsolePlayer) {
            sendMineDownToConsole(message);
            return;
        }

        player.sendMessage(new MineDown(message));
    }

    public void sendCustomMessage(@NotNull Player player, @NotNull String message) {
        if (player instanceof ConsolePlayer) {
            sendMineDownToConsole(message);
            return;
        }
        player.sendMessage(new MineDown(message));
    }

    public void sendChannelMessage(@NotNull Player target, @NotNull Player sender, @NotNull Channel channel, @NotNull String message) {
        plugin.replacePlaceholders(sender, channel.getFormat()).thenAccept(replaced -> {
            final TextComponent.Builder componentBuilder = Component.text()
                    .append(new MineDown(replaced).toComponent());
            if (sender.hasPermission("huskchat.formatted_chat")) {
                componentBuilder.append(new MineDown(message)
                        .disable(MineDownParser.Option.ADVANCED_FORMATTING).toComponent());
            } else {
                componentBuilder.append(Component.text(message));
            }
            target.sendMessage(componentBuilder.build());
        });
    }

    public void sendOutboundPrivateMessage(@NotNull Player sender, @NotNull List<Player> recipients, @NotNull String message) {
        plugin.replacePlaceholders(recipients.get(0), recipients.size() == 1
                ? plugin.getSettings().getOutboundMessageFormat()
                : plugin.getSettings().getGroupOutboundMessageFormat()
        ).thenAccept(replaced -> {
            final TextComponent.Builder builder = Component.text();
            if (recipients.size() > 1) {
                replaced = replaced.replace("%group_amount_subscript%", superscriptNumber(recipients.size() - 1))
                        .replace("%group_amount%", Integer.toString(recipients.size() - 1))
                        .replace("%group_members_comma_separated%", getGroupMemberList(recipients, ","))
                        .replace("%group_members%", MineDown.escape(getGroupMemberList(recipients, "\n")));
            }

            builder.append(new MineDown(replaced).toComponent());
            if (sender.hasPermission("huskchat.formatted_chat")) {
                builder.append(new MineDown(message).disable(MineDownParser.Option.ADVANCED_FORMATTING).toComponent());
            } else {
                builder.append(Component.text(message));
            }

            if (sender instanceof ConsolePlayer) {
                plugin.getConsole().sendMessage(builder.build());
                return;
            }
            sender.sendMessage(builder.build());
        });
    }

    public void sendInboundPrivateMessage(List<Player> recipients, Player sender, String message) {
        plugin.replacePlaceholders(sender, recipients.size() == 1
                ? plugin.getSettings().getInboundMessageFormat()
                : plugin.getSettings().getGroupInboundMessageFormat()
        ).thenAccept(replaced -> {
            final TextComponent.Builder builder = Component.text();
            if (recipients.size() > 1) {
                replaced = replaced.replace("%group_amount_subscript%", superscriptNumber(recipients.size() - 1))
                        .replace("%group_amount%", Integer.toString(recipients.size() - 1))
                        .replace("%group_members_comma_separated%", getGroupMemberList(recipients, ","))
                        .replace("%group_members%", MineDown.escape(getGroupMemberList(recipients, "\n")));
            }

            builder.append(new MineDown(replaced).toComponent());
            if (sender.hasPermission("huskchat.formatted_chat")) {
                builder.append(new MineDown(message).disable(MineDownParser.Option.ADVANCED_FORMATTING).toComponent());
            } else {
                builder.append(Component.text(message));
            }

            for (final Player recipient : recipients) {
                if (recipient instanceof ConsolePlayer) {
                    plugin.getConsole().sendMessage(builder.build());
                    return;
                }
                recipient.sendMessage(builder.build());
            }
        });
    }

    public void sendLocalSpy(@NotNull Player spy, @NotNull PlayerCache.SpyColor spyColor, @NotNull Player sender,
                             @NotNull Channel channel, @NotNull String message) {
        plugin.replacePlaceholders(sender, plugin.getSettings().getLocalSpyFormat())
                .thenAccept(replaced -> {
                    final TextComponent.Builder componentBuilder = Component.text()
                            .append(new MineDown(replaced.replace("%spy_color%", spyColor.colorCode)
                                                         .replace("%channel%", channel.getId()) +
                                                 MineDown.escape(message)).toComponent());
                    spy.sendMessage(componentBuilder.build());
                });
    }

    public void sendSocialSpy(@NotNull Player spy, @NotNull PlayerCache.SpyColor spyColor, @NotNull Player sender,
                              @NotNull List<Player> receivers, @NotNull String message) {
        plugin.replacePlaceholders(sender, receivers.size() == 1
                ? plugin.getSettings().getSocialSpyFormat() : plugin.getSettings().getSocialSpyGroupFormat()
                .replace("%sender_", "%")
        ).thenAccept(senderReplaced -> plugin.replacePlaceholders(receivers.get(0), senderReplaced
                .replace("%receiver_", "%")
        ).thenAccept(replaced -> {
            if (receivers.size() > 1) {
                replaced = replaced.replace("%group_amount_subscript%", superscriptNumber(receivers.size() - 1))
                        .replace("%group_amount%", Integer.toString(receivers.size() - 1))
                        .replace("%group_members_comma_separated%", getGroupMemberList(receivers, ","))
                        .replace("%group_members%", MineDown.escape(getGroupMemberList(receivers, "\n")));
            }
            spy.sendMessage(new MineDown(replaced.replace("%spy_color%", spyColor.colorCode) + MineDown.escape(message)));
        }));
    }

    public void sendFormattedBroadcastMessage(@NotNull Player player, @NotNull String message) {
        final TextComponent.Builder componentBuilder = Component.text();
        componentBuilder.append(new MineDown(plugin.getSettings().getBroadcastMessageFormat()).toComponent());
        componentBuilder.append(new MineDown(message).disable(MineDownParser.Option.ADVANCED_FORMATTING).toComponent());
        player.sendMessage(componentBuilder.build());
    }

    private void sendMineDownToConsole(@NotNull String mineDown) {
        plugin.getConsole().sendMessage(Component.text(extractMineDownLinks(mineDown)));
    }

    // Returns a newline-separated list of player names
    @NotNull
    public final String getGroupMemberList(@NotNull List<Player> players, @NotNull String delimiter) {
        final StringJoiner memberList = new StringJoiner(delimiter);
        for (Player player : players) {
            memberList.add(player.getName());
        }
        return memberList.toString();
    }

    // Get the corresponding subscript unicode character from a normal one
    @NotNull
    public final String superscriptNumber(int number) {
        final String[] digits = {"₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉"};
        final StringBuilder sb = new StringBuilder();
        for (char c : String.valueOf(number).toCharArray()) {
            sb.append(digits[Integer.parseInt(String.valueOf(c))]);
        }
        return sb.toString();
    }

}
