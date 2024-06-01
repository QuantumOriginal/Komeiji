package org.qo.komeiji.utils;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MSPTCalc {

    /** 最终展现在返回结果的 MilliSecond Per Tick 值 */
    public static float mspt = 0f;
    private static final ArrayList<Long> recent_60tick = new ArrayList<>();
    private static final LinkedList<Double> last_60_mspt = new LinkedList<>();
    /** 记录一个游戏刻开始的毫秒时间 */
    private static long starttime = 0;

    /**
     * 监听游戏刻
     */
    public void onServerTick() {
        long currentTime = System.currentTimeMillis();
        if (starttime != 0) {
            long tickTime = currentTime - starttime;
            if (Float.isNaN(mspt)) {
                mspt = tickTime;
                Bukkit.getLogger().warning("Why you get NaN in prev mspt?");
            } else {
                mspt = mspt * 0.95f + tickTime * 0.05f;
            }
            recent_60tick.add(tickTime);

            if (last_60_mspt.size() >= 60) {
                last_60_mspt.removeFirst();
            }
            last_60_mspt.add((double) tickTime);
        }
        starttime = currentTime;
    }

    /**
     * 获取最近三秒的 mspt 平均值
     * @return 最近三秒的 mspt 平均值
     */
    public static float getRecent3SecondsAverage() {
        int sum = 0;
        int count = 0;
        for (int i = recent_60tick.size() - 1; i >= 0 && count < 60; i--) {
            sum += recent_60tick.get(i);
            count++;
        }
        return count == 0 ? 0 : (float) sum / count;
    }

    /**
     * 获取最近60的 mspt 趋势
     * @return 包含最近60的 mspt 趋势（60个值）
     */
    public static ArrayList<Double> getLast60MsptTrend() {
        return new ArrayList<>(last_60_mspt);
    }

    private static String f(int i) {
        if (i >= 10) return i + "";
        else return "0" + i;
    }
}
