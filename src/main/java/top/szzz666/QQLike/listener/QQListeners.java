package top.szzz666.QQLike.listener;

import top.szzz666.StarrySkyLink.qq.entity.Event;
import top.szzz666.StarrySkyLink.qq.listener.QQEventHandler;
import top.szzz666.StarrySkyLink.websocket.Wss;

import static top.szzz666.QQLike.config.MyConfig.*;
import static top.szzz666.StarrySkyLink.qq.send.Send.GroupAtReplyMsg;
import static top.szzz666.StarrySkyLink.qq.send.Send.LikeMsg;
import static top.szzz666.StarrySkyLink.qq.tools.QQIfUtils.isMineGroupMessage;
import static top.szzz666.StarrySkyLink.qq.tools.QQIfUtils.isNotMe;

public class QQListeners {

    @QQEventHandler
    public void onEvent(Event event) {
        if (event.getSub_type() != null && isNotMe(event)) {
            String text = event.getText();
            long user_id = event.getUser_id();
            if (isMineGroupMessage(event) && text != null) {
                if (trigger_key.contains(text)) {
                    Wss.QQbroadcast(null,
                            GroupAtReplyMsg(event.getGroup_id(), user_id, event.getMessage_id(), "\n" + reply_msg));
                    Wss.QQbroadcast(null, LikeMsg(user_id, like_times));
                }
            }
        }
    }

}
