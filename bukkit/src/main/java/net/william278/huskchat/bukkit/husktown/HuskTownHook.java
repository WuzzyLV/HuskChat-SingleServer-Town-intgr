package net.william278.huskchat.bukkit.husktown;

import net.william278.husktowns.api.HuskTownsAPI;
import org.bukkit.entity.Player;

public class HuskTownHook {
    HuskTownsAPI huskTownsAPI;

    public HuskTownHook() {
        huskTownsAPI = HuskTownsAPI.getInstance();
    }

    public void turnOffTownChat(Player player) {
        huskTownsAPI.getUserPreferences(player).setTownChatTalking(false);
    }
}
