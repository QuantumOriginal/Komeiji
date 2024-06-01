package org.qo.komeiji;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.qo.komeiji.utils.MSPTCalc;
import org.qo.komeiji.utils.Request;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.qo.komeiji.utils.Request.*;
public final class Komeiji extends JavaPlugin {

    public static final String default_url_endpoint = "http://localhost";
    public final String auth_key = "default_authkey";
    private final MSPTCalc msptCalc = new MSPTCalc();
    public static String server_name = Configuration.get("server_name");
    private Logger logger = Bukkit.getLogger();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return CommandHandler.onCommand(sender,command,label,args);
    }
    @Override
    public void onEnable() {

        String result;

        try {
            result = Request.sendRequest(Method.POST, default_url_endpoint + "/connect", Configuration.get("auth")).thenApply(response -> {
                return response;
            }).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        if (result == null){
            logger.log(Level.SEVERE, "could not connect to target server:" + default_url_endpoint);
            Bukkit.shutdown();
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            StatusUploader uploader = new StatusUploader();
            uploader.uploadStatus();
        }, 0, 40L);

        Bukkit.getScheduler().runTaskTimer(this, msptCalc::onServerTick, 0, 1);

        getServer().getPluginManager().registerEvents(new PlayerChatUploader(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
