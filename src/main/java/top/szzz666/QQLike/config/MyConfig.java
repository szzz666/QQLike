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

    @ConfigItem(key = "like_times", comment = "点赞次数")
    public static int like_times = 10;

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
