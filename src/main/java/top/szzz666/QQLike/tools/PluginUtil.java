package top.szzz666.QQLike.tools;

import cn.nukkit.command.CommandSender;
import top.szzz666.QQLike.QQLikeMain;


import static top.szzz666.QQLike.QQLikeMain.nkServer;

public class PluginUtil {
    public static void multCmd(CommandSender sender, String command) {
        nkServer.getCommandMap().dispatch(sender, command);
    }




    //将输入的字符串按行打印到控制台。
    public static void lineConsole(String s) {
        String[] lines = s.split("\n");
        for (String line : lines) {
            QQLikeMain.logger.info(line);
        }
    }


}
