package org.qo.komeiji;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.qo.komeiji.utils.Request;

import java.util.ArrayList;
import java.util.TimerTask;

import static org.qo.komeiji.utils.Request.Method.GET;

public class ServerMsgGetter extends TimerTask {
    static ArrayList<String> buffer = new ArrayList<>();

    @Override
    public void run() {
        try {
            String response = Request.sendRequest(GET,Komeiji.default_url_endpoint + "/msglist/download", "").thenApply(a-> {
                return a;
            }).get();
            JsonObject remoteObj = new Gson().fromJson(response, JsonObject.class);
            JsonArray remoteRaw = remoteObj.getAsJsonArray("messages");
            ArrayList<String> remoteArr = parseArrList(remoteRaw);
            ArrayList<String> newMessages = new ArrayList<>();

            if (buffer == null || diff(remoteArr, buffer)) {
                newMessages = new ArrayList<>(remoteArr);
                if (!newMessages.isEmpty()) {
                    newMessages.removeAll(buffer);
                }
            }

            for (String msg : newMessages) {
                if (msg.startsWith("[QQ]")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(msg);
                    }
                }
            }

            buffer = remoteArr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static ArrayList<String> parseArrList(JsonArray arr) {
        ArrayList<String> ret = new ArrayList<>();
        for (JsonElement element : arr) {
            ret.add(element.getAsString());
        }
        return ret;
    }

    static boolean diff(ArrayList<String> arr1, ArrayList<String> arr2) {
        if (arr1.size() != arr2.size()) {
            return true;
        }
        for (int i = 0; i < arr1.size(); i++) {
            if (!arr1.get(i).equals(arr2.get(i))) {
                return true;
            }
        }
        return false;
    }
}
