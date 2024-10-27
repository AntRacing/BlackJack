package com.BlackJack.inTools;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class Result {
    public static TextWebSocketFrame success(String text){

        return new TextWebSocketFrame(JSON.toJSONString(text));
    }

    public static TextWebSocketFrame success(JSONObject obj){

        return new TextWebSocketFrame(JSON.toJSONString(obj));
    }

    public static TextWebSocketFrame fail(String text){
        return new TextWebSocketFrame(JSON.toJSONString("出现异常:"+text));
    }

    public static TextWebSocketFrame answer(String text){
        return new TextWebSocketFrame("回复:"+JSON.toJSONString(text));
    }
}
