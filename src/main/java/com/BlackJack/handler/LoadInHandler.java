package com.BlackJack.handler;

import com.BlackJack.Game;
import com.BlackJack.GameServer;
import com.BlackJack.Room;
import com.BlackJack.gameParticipant.Player;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

import static com.BlackJack.GameServer.games;


public class LoadInHandler extends ChannelInboundHandlerAdapter {


    @Override
//  public void channelRegistered(ChannelHandlerContext ctx) {
    public void channelActive(ChannelHandlerContext ctx) {
//        Room room = GameServer.room;
//        if (!room.allReady()){
//            String pos = "player0" + (room.getPlayers().size()+1);
//            Player player = new Player(pos, ctx.channel());
//            GameServer.room.addPlayer(player);
//            System.out.println("newPlayer "+pos+" 加入room");
//        } else {
//            ctx.channel().writeAndFlush(new TextWebSocketFrame("出现异常:房间已满员"));
//            ctx.close();
//        }
        try {
            Player player = new Player("player01", ctx.channel());
            Game game = new Game(player);
            games.put(game);
            ctx.fireChannelActive();
        }catch (Exception e){

        }

//        game.startGame();
 //       game.roundTrun();
//        game.settleGame();
//        ctx.fireChannelRegistered();
    }
/*
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Room room = GameServer.room;
        Player player = room.findPlayer(ctx.channel());
        if (!room.allReady() && player != null) {
        if (player != null) {
            room.removePlayer(player);
            System.out.println("Player " + player.getPos() + " 离开room");
        }

            System.out.println("Player " + player.getPos() + " 离开room");
            ctx.fireChannelUnregistered();
        }
    }
*/
}
