package top.szzz666.QQLike;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import top.szzz666.QQEssential.command.QQCommandManage;
import top.szzz666.QQLike.command.MyCommand;
import top.szzz666.QQLike.config.EasyConfig;
import top.szzz666.QQLike.config.LikeData;
import top.szzz666.QQLike.listener.QQListeners;
import top.szzz666.StarrySkyLink.qq.entity.Event;
import top.szzz666.StarrySkyLink.qq.listener.QQEventManage;
import top.szzz666.StarrySkyLink.websocket.Wss;

import static top.szzz666.CronTaskApi.tools.TaskUtil.Cron;
import static top.szzz666.QQEssential.command.QQCommand.*;
import static top.szzz666.QQLike.config.MyConfig.*;
import static top.szzz666.StarrySkyLink.qq.send.Send.*;
import static top.szzz666.StarrySkyLink.qq.send.Send.GroupAtReplyMsg;


public class QQLikeMain extends PluginBase {
    public static Plugin plugin;
    public static Server nkServer;
    public static PluginLogger logger;
    public static CommandSender consoleObjects;
    public static String ConfigPath;
    public static EasyConfig ec;

    //插件读取
    @Override
    public void onLoad() {
        nkServer = this.getServer();
        plugin = this;
        logger = this.getLogger();
        consoleObjects = getServer().getConsoleSender();
        ConfigPath = getDataFolder().getPath();
        initConfig();
        LikeData.init();
        logger.info("§b" + plugin.getName() + "插件读取...");
    }

    //插件开启
    @Override
    public void onEnable() {
        //注册监听器
//        QQEventManage.registerListener(new QQListeners());
        //注册命令
        nkServer.getCommandMap().register(this.getName(), new MyCommand());

        QQCommandManage.register(trigger_key, "点赞你主页点赞", "点赞",
                PERM_ALL, SCOPE_BOTH, QQLikeMain::onLike);
        startAutoLike();
        logger.info("§b" + plugin.getName() + "插件开启");
        logger.warning("§c" + plugin.getName() + "如果遇到任何bug，请加入Q群进行反馈：894279534");
    }

    private static void onLike(Event event, String s, String[] strings) {
        long user_id = event.getUser_id();
        // 检查每日点赞次数限制
        int usedCount = LikeData.getLikeCount(user_id);
        if (usedCount >= like_times_per_day) {
            String limitMsg = like_limit_msg
                    .replace("%used%", String.valueOf(usedCount))
                    .replace("%max%", String.valueOf(like_times_per_day));
            if (event.isMineGroupMessage()) {
                event.send(GroupAtReplyMsg(event.getGroup_id(), user_id, event.getMessage_id(), "\n" + limitMsg));
            }
            if (event.isPrivateMessage()) {
                event.send(PrivateTextMsg(user_id, limitMsg));
            }
            return;
        }

        if (event.isMineGroupMessage()) {
            event.send(GroupAtReplyMsg(event.getGroup_id(), user_id, event.getMessage_id(), "\n" + reply_msg));
        }
        if (event.isPrivateMessage()) {
            event.send(PrivateTextMsg(user_id, reply_msg));
        }
        event.send(LikeMsg(user_id, like_times));
        LikeData.incrementLikeCount(user_id);
    }

    //插件关闭
    @Override
    public void onDisable() {
        logger.info("§b" + plugin.getName() + "插件关闭");
    }

    //启动自动点赞定时任务
    public static void startAutoLike() {

        if (!auto_like) {
            logger.info("自动点赞已关闭");
            return;
        }

        for (String cron : auto_like_uid.keySet()) {
            long uid = auto_like_uid.get(cron);
            Cron(() -> {
                int usedCount = LikeData.getLikeCount(uid);
                if (usedCount >= like_times_per_day) {
                    logger.info(String.format("自动点赞跳过: QQ号：%s 今日已达上限 (%d/%d)", uid, usedCount, like_times_per_day));
                    return;
                }
                Wss.QQbroadcast(null, LikeMsg(uid, like_times));
                Wss.QQbroadcast(null, PrivateTextMsg(uid, auto_like_reply_msg));
                LikeData.incrementLikeCount(uid);
                logger.info(String.format("已执行自动点赞任务: QQ号：%s", uid));
            }, cron);
        }
        logger.info("自动点赞定时任务已启动，共 " + auto_like_uid.size() + " 个任务");
    }

}
