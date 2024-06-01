package org.qo.komeiji;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import org.qo.komeiji.utils.Request;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.qo.komeiji.utils.Request.Method.POST;

public class PlayerChatUploader implements Listener
{
    public static boolean isShutup(Player player)
    {
        boolean istagged = false;
        for(String s:player.getScoreboardTags()) if(s.contentEquals("muteqq")) istagged = true;
        return istagged;
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (!isShutup(event.getPlayer())) {
            try {
                String playerName = event.getPlayer().getName();
                String message = event.getMessage();
                StringBuilder sb = new StringBuilder();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(new Date());
                sb.append("[" + Komeiji.server_name + "|" + currentTime + "]").append("<").append(playerName).append(">: ").append(message);
                String encodedMessage = new String(sb.toString().getBytes("UTF-8"), "ISO-8859-1");
                Request.sendRequest(POST, Komeiji.default_url_endpoint + "/msglist/upload", encodedMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
