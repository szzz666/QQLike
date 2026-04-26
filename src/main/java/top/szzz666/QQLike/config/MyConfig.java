package top.szzz666.QQLike.config;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.szzz666.QQLike.QQLikeMain.ConfigPath;
import static top.szzz666.QQLike.QQLikeMain.ec;

public class MyConfig {

    @ConfigItem(key = "command", comment = "命令")
    public static String command = "qqlike";

    @ConfigItem(key = "reply_msg", comment = "回复消息")
    public static String reply_msg = "赞你了喵～";

    @ConfigItem(key = "like_times", comment = "每次点赞数")
    public static int like_times = 10;

    @ConfigItem(key = "like_times_per_day", comment = "每QQ每天点赞最大次数")
    public static int like_times_per_day = 10;

    @ConfigItem(key = "like_limit_msg", comment = "点赞次数达到上限时的提示消息（%used%为已赞次数，%max%为每日上限）")
    public static String like_limit_msg = "你今天已经赞了%used%次，额度不足，明天再来吧～";

    @ConfigItem(key = "trigger_key", comment = "触发关键词")
    public static List<String> trigger_key = new ArrayList<>(List.of(
            "赞我"
    ));

    @ConfigItem(key = "auto_like.reply_msg", comment = "自动点赞消息")
    public static String auto_like_reply_msg = "自动赞你了喵～";

    @ConfigItem(key = "auto_like.enable", comment = "是否开启自动点赞")
    public static boolean auto_like = true;

    @ConfigItem(key = "auto_like.uid", comment = "自动点赞的QQ号")
    public static Map<String, Long> auto_like_uid = new HashMap<>(Map.of(
            "0 0 8 * * ?", 3214948198L
    ));


    public static void initConfig() {
        ec = new EasyConfig(ConfigPath + "/config.yml");
        ec.loadFromClass(MyConfig.class);
        ec.load();
    }

}
