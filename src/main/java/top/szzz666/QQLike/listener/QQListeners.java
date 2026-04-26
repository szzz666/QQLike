package top.szzz666.QQLike.listener;

import top.szzz666.QQLike.tools.LikeData;
import top.szzz666.StarrySkyLink.qq.entity.Event;
import top.szzz666.StarrySkyLink.qq.listener.QQEventHandler;

import static top.szzz666.QQLike.config.MyConfig.*;
import static top.szzz666.StarrySkyLink.qq.send.Send.*;


public class QQListeners {

    @QQEventHandler
    public void onEvent(Event event) {
        if (event.getSub_type() != null && event.isNotMe()) {
            String text = event.getText();
            long user_id = event.getUser_id();
            if (text != null && trigger_key.contains(text)) {
                int usedCount = LikeData.getUsedCount(user_id);

                if (usedCount + like_times > like_times_per_day) {
                    String limitMsg = like_limit_msg.replace("%used%", String.valueOf(usedCount)).replace("%max%", String.valueOf(like_times_per_day));
                    if (event.isMineGroupMessage()) {
                        event.send(GroupAtReplyMsg(event.getGroup_id(), user_id, event.getMessage_id(), "\n" + limitMsg));
                    }
                    if (event.isPrivateMessage()) {
                        event.send(PrivateTextMsg(user_id, limitMsg));
                    }
                    return;
                }

                LikeData.addUsedCount(user_id, like_times);

                if (event.isMineGroupMessage()) {
                    event.send(GroupAtReplyMsg(event.getGroup_id(), user_id, event.getMessage_id(), "\n" + reply_msg));
                }
                if (event.isPrivateMessage()) {
                    event.send(PrivateTextMsg(user_id, reply_msg));
                }
                event.send(LikeMsg(user_id, like_times));
            }
        }
    }

}
