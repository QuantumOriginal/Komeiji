package org.qo.komeiji;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.qo.komeiji.PlayerChatUploader.isShutup;

public class CommandHandler {
    public static boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("shutup") && args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            Player s = (Player) sender;
            if (args[0].contentEquals("query"))
                s.sendMessage("当前向QQ同步消息的状态为：" + (isShutup(s) ? "关闭" : "开启"));
            else if (args[0].contentEquals("enable")) {
                s.removeScoreboardTag("muteqq");
                s.sendMessage("已经启用QQ同步");
            } else if (args[0].contentEquals("disable")) {
                s.addScoreboardTag("muteqq");
                s.sendMessage("已经禁用QQ同步");
            }
            return true;
        }
        return false;
    }
}
