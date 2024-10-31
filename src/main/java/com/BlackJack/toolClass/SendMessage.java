package com.BlackJack.toolClass;

import com.BlackJack.gameParticipant.Player;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;



public class SendMessage {


    public static void toTarget(String msg, Player target){
        try {
            //Thread.currentThread().sleep(3000);
            target.getChannel().writeAndFlush(toFrame(msg));
        }catch (Exception e){
            System.out.println("等待出错");
        }
    }

    public static void toTarget(JSONObject obj, Player target){
        try {
            //Thread.currentThread().sleep(3000);
            target.getChannel().writeAndFlush(toFrame(obj));
        }catch (Exception e){
            System.out.println("等待出错");
        }

    }


    private static TextWebSocketFrame toFrame(JSONObject obj){

        return new TextWebSocketFrame(JSON.toJSONString(obj));
    }

    private static TextWebSocketFrame toFrame(String text){

        return new TextWebSocketFrame(text);
    }

}
