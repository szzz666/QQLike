package top.szzz666.QQLike.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static top.szzz666.QQLike.QQLikeMain.ConfigPath;
import static top.szzz666.QQLike.config.MyConfig.like_times;

public class LikeData {

    private static EasyConfig dataConfig;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void init() {
        dataConfig = new EasyConfig(ConfigPath + "/like_data.yml");
        dataConfig.load();
    }

    /**
     * 获取用户今日已点赞次数，跨天自动归零
     */
    public static int getLikeCount(long userId) {
        String today = LocalDate.now().format(DATE_FORMAT);
        String storedDate = dataConfig.getString(userId + ".date");
        if (!today.equals(storedDate)) {
            return 0;
        }
        return dataConfig.getInt(userId + ".count", 0);
    }

    /**
     * 用户点赞次数+1并持久化，同时更新日期
     */
    public static void incrementLikeCount(long userId) {
        String today = LocalDate.now().format(DATE_FORMAT);
        int currentCount = getLikeCount(userId);
        dataConfig.set(userId + ".date", today);
        dataConfig.set(userId + ".count", currentCount + like_times);
        dataConfig.save();
    }

    /**
     * 重置某用户的点赞数据
     */
    public static void resetUser(long userId) {
        dataConfig.set(userId + ".date", null);
        dataConfig.set(userId + ".count", null);
        dataConfig.save();
    }
}
