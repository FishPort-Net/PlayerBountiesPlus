package com.tcoded.playerbountiesplus.command.admin;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerBountiesPlusForceSetCmd {

    private static final String RELOAD_PERMISSION = "playerbountiesplus.command.playerbountiesplus.forceset";

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {

        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            String noPerm = plugin.getLang().getColored("command.no-permission");
            String noPermDetailed = plugin.getLang().getColored("command.no-permission-detailed")
                            .replace("{no-permission-msg}", noPerm)
                            .replace("{permission}", RELOAD_PERMISSION);
            sender.sendMessage(noPermDetailed);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("缺少参数。请使用 /pbp forceset <player> <amount>");
            return true;
        }

        String playerName = args[0];
        String amountStr = args[1];
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            sender.sendMessage("金额格式不正确。请提供一个有效的数字。");
            return true;
        }
        // 获取玩家
        OfflinePlayer player = plugin.getServer().getOfflinePlayer(playerName);


        // Calculate total bounty including previous bounties
        ConcurrentHashMap<UUID, Integer> bounties = plugin.getBountyDataManager().getBounties();
        bounties.put(player.getUniqueId(), (int) Math.round(amount));

        plugin.getBountyDataManager().saveBountiesAsync();

        return true;
    }

}
