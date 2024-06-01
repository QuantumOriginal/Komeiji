package org.qo.komeiji;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.qo.komeiji.utils.MSPTCalc;
import org.qo.komeiji.utils.Request;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;
import static org.qo.komeiji.utils.Request.Method.POST;

public class StatusUploader {

    public void uploadStatus() {
        int status = 0;
        float latestMspt = MSPTCalc.getRecent3SecondsAverage();
        ArrayList<Double> latestMsptList = MSPTCalc.getLast60MsptTrend();
        int serverPlayerCount = Bukkit.getOnlinePlayers().size();
        ArrayList<PlayerInfo> serverPlayerList = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.name = player.getName();
            playerInfo.IPaddress = player.getAddress().getAddress().getHostAddress();
            playerInfo.isOP = player.isOp();
            playerInfo.location = new double[]{player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()};
            playerInfo.PlayerDimension = player.getWorld().getName();
            serverPlayerList.add(playerInfo);
        }
        Status statusObj = new Status(status, latestMspt, latestMsptList, serverPlayerCount, serverPlayerList);
        Gson gson = new Gson();
        String statusJson = gson.toJson(statusObj);
        Request.sendRequest(POST, Komeiji.default_url_endpoint + "/upload", statusJson);
    }

    public static class PlayerInfo {
        String name;
        String IPaddress;
        boolean isOP;
        double[] location;
        String PlayerDimension;
    }

    public static class Status {
        int status;
        float latestMspt;
        ArrayList<Double> latestMsptList;
        int serverPlayerCount;
        ArrayList<PlayerInfo> serverPlayerList;

        public Status(int status, float latestMspt, ArrayList<Double> latestMsptList, int serverPlayerCount, ArrayList<PlayerInfo> serverPlayerList) {
            this.status = status;
            this.latestMspt = latestMspt;
            this.latestMsptList = latestMsptList;
            this.serverPlayerCount = serverPlayerCount;
            this.serverPlayerList = serverPlayerList;
        }
    }
}
