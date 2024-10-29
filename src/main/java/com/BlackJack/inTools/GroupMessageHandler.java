package com.BlackJack.inTools;

import com.BlackJack.gameParticipant.Player;
import com.alibaba.fastjson2.JSONObject;

import static com.BlackJack.GameServer.room;


public class GroupMessageHandler {

    public static void toGroup(String msg){
        for (Player player : room.getPlayers()) {
            player.getChannel().writeAndFlush(Result.success(msg));
        }
    }

    public static void toGroup(JSONObject obj){
        for (Player player : room.getPlayers()) {
            player.getChannel().writeAndFlush(Result.success(obj));
        }

    }

    public static void toOthers(String msg, Player target){
        for (Player player : room.getPlayers()) {
            if(!player.equals(target)){
                player.getChannel().writeAndFlush(Result.success(msg));
            }
        }
    }

    public static void toOthers(JSONObject obj, Player target){
        for (Player player : room.getPlayers()) {
            if(!player.equals(target)){
                player.getChannel().writeAndFlush(Result.success(obj));
            }
        }

    }

}
