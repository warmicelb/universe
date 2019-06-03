package com.ice.universe.common.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.ice.universe.common.utils.HttpUtils;
import java.util.Collections;

/**
 * 群聊钉钉机器人消息推送
 * @author: ice
 * @create: 2019/1/18
 **/
public class RobotMessagePush {

    private static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=a1910d6e1681adaded687a1e282c424f946dcbcdda38ed21112aa3e7c2f96643";

    /**
     * 通过钉钉机器人推送群消息通知(讨论组：y+x+l)
     * @param message
     */
    public static void pushMessage(String message){
//        Map<String,String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json; charset=utf-8");
        JSONObject params = new JSONObject();
        params.put("msgtype","text");
        params.put("text", Collections.singletonMap("content",message));
        HttpUtils.doPostJSON(WEBHOOK_TOKEN, params);
    }
}
