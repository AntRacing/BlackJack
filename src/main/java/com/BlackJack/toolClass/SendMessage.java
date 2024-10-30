package com.BlackJack.toolClass;

import com.BlackJack.gameParticipant.Player;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import static com.BlackJack.GameServer.room;


public class SendMessage {

    public static void toGroup(String msg) {
        try {
            //Thread.currentThread().sleep(3000);
            for (Player player : room.getPlayers()) {
                player.getChannel().writeAndFlush(toFrame(msg));
            }
        }catch (Exception e){
            System.out.println("等待出错");
        }

    }

    public static void toGroup(JSONObject obj){
        try {
            //Thread.currentThread().sleep(3000);
            for (Player player : room.getPlayers()) {
                player.getChannel().writeAndFlush(toFrame(obj));
            }
        }catch (Exception e){
            System.out.println("等待出错");
        }
    }

    public static void toTarget(String msg, Player target){
        try {
            //Thread.currentThread().sleep(3000);
            for (Player player : room.getPlayers()) {
                if(player.equals(target)){
                    player.getChannel().writeAndFlush(toFrame(msg));
                    return;
                }
            }
        }catch (Exception e){
            System.out.println("等待出错");
        }
    }

    public static void toTarget(JSONObject obj, Player target){
        try {
            //Thread.currentThread().sleep(3000);
            for (Player player : room.getPlayers()) {
                if(player.equals(target)){
                    player.getChannel().writeAndFlush(toFrame(obj));
                    return;
                }
            }
        }catch (Exception e){
            System.out.println("等待出错");
        }

    }


    public static void toOthers(String msg, Player target){
        for (Player player : room.getPlayers()) {
            if(!player.equals(target)){
                player.getChannel().writeAndFlush(toFrame(msg));
            }
        }
    }

    public static void toOthers(JSONObject obj, Player target){
        for (Player player : room.getPlayers()) {
            if(!player.equals(target)){
                player.getChannel().writeAndFlush(toFrame(obj));
            }
        }

    }

    private static TextWebSocketFrame toFrame(JSONObject obj){

        return new TextWebSocketFrame(JSON.toJSONString(obj));
    }

    private static TextWebSocketFrame toFrame(String text){

        return new TextWebSocketFrame(text);
    }

}
