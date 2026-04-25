package top.szzz666.QQLike;

import cn.hutool.cron.CronUtil;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import top.szzz666.QQLike.command.MyCommand;
import top.szzz666.QQLike.config.EasyConfig;
import top.szzz666.QQLike.listener.QQListeners;
import top.szzz666.StarrySkyLink.qq.listener.QQEventSource;
import top.szzz666.StarrySkyLink.websocket.Wss;

import static top.szzz666.QQLike.config.MyConfig.*;
import static top.szzz666.StarrySkyLink.qq.send.Send.LikeMsg;
import static top.szzz666.StarrySkyLink.qq.send.Send.PrivateTextMsg;


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
        logger.info("§b" + plugin.getName() + "插件读取...");
    }

    //插件开启
    @Override
    public void onEnable() {
        //注册监听器
        QQEventSource.registerListener(new QQListeners());
        //注册命令
        nkServer.getCommandMap().register(this.getName(), new MyCommand());

        startAutoLike();

        logger.info("§b" + plugin.getName() + "插件开启");
        logger.warning("§c" + plugin.getName() + "如果遇到任何bug，请加入Q群进行反馈：894279534");
    }

    //插件关闭
    @Override
    public void onDisable() {
        if (CronUtil.getScheduler().isStarted()) {
            CronUtil.stop();
        }
        logger.info("§b" + plugin.getName() + "插件关闭");
    }

    //启动自动点赞定时任务
    public static void startAutoLike() {
        //先停止已有的定时任务，防止重复注册
        if (CronUtil.getScheduler().isStarted()) {
            CronUtil.stop();
        }

        if (!auto_like) {
            logger.info("自动点赞已关闭");
            return;
        }

        for (String cron : auto_like_uid.keySet()) {
            long uid = auto_like_uid.get(cron);
            CronUtil.schedule(cron, (Runnable) () -> {
                Wss.QQbroadcast(null, LikeMsg(uid, like_times));
                Wss.QQbroadcast(null, PrivateTextMsg(uid, auto_like_reply_msg));
                logger.info(String.format("已执行自动点赞任务: QQ号：%s", uid));
            });
        }
        CronUtil.start();
        logger.info("自动点赞定时任务已启动，共 " + auto_like_uid.size() + " 个任务");
    }

}
