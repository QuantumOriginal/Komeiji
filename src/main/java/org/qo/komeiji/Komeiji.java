package org.qo.komeiji;

import org.bukkit.plugin.java.JavaPlugin;
import org.qo.komeiji.utils.Request;

import java.util.concurrent.ExecutionException;

import static org.qo.komeiji.utils.Request.*;
public final class Komeiji extends JavaPlugin {
    public final String default_url_endpoint = "http://localhost";

    @Override
    public void onEnable() {
        try {
            String result = Request.sendRequest(Method.POST, default_url_endpoint, "").thenApply(response -> {
                return response;
            }).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
