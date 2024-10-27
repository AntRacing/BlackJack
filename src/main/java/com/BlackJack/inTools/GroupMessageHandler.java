package com.BlackJack.inTools;

import com.BlackJack.gameParticipant.Player;
import com.alibaba.fastjson2.JSONObject;

import java.nio.charset.StandardCharsets;

import static com.BlackJack.GameServer.room;


public class GroupMessageHandler {

    public static void toGroup(String msg){
        for (Player player : room.getPlayers()) {
            player.getChannel().writeAndFlush(Result.success(msg));
        }
    }

    public static void toGroup(JSONObject obj){
        for (Player player : room.getPlayers()) {
            player.getChannel().writeAndFlush(Result.success(obj.toJSONString()));
        }

//        String json = obj.toJSONString();
//        //转成btye数组   utf-8模式
//        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
//        for (Player player : room.getPlayers()) {
//            player.getChannel().writeAndFlush(bytes);
//        }
    }





//    public static void toPlayer(JSONObject json, String pos){
//        for (Player player : room.getPlayers()) {
//            if(player.getPos().equals(pos)){
//                player.getChannel().writeAndFlush(Result.success(json));
//            }
//        }
//    }
}
