package top.szzz666.QQLike.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import top.szzz666.StarrySkyLink.websocket.Wss;

import static top.szzz666.QQLike.QQLikeMain.ec;
import static top.szzz666.QQLike.QQLikeMain.plugin;
import static top.szzz666.QQLike.QQLikeMain.startAutoLike;
import static top.szzz666.QQLike.config.MyConfig.initConfig;
import static top.szzz666.QQLike.config.MyConfig.like_times;
import static top.szzz666.StarrySkyLink.qq.send.Send.LikeMsg;


public class MyCommand extends Command {

    public MyCommand() {
        super(ec.getString("command"), plugin.getName() + "命令");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.isOp() || sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                if ("reload".equals(args[0])) {
                    initConfig();
                    startAutoLike();
                    sender.sendMessage(plugin.getName() + "插件配置已重新加载");
                    return true;
                }else {
                    try {
                        long uid = Long.parseLong(args[0]);
                        Wss.QQbroadcast(null, LikeMsg(uid, like_times));
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage("§c无效的QQ号: " + args[0]);
                        return false;
                    }
                }

            }

        }
        return false;
    }

}
