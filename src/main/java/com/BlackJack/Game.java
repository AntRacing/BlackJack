package com.BlackJack;

import com.BlackJack.gameParticipant.Dealer;
import com.BlackJack.gameParticipant.Host;
import com.BlackJack.gameParticipant.Player;
import com.BlackJack.toolClass.SendMessage;
import com.BlackJack.toolClass.Obj2Json;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Game {
    List<Player> players;
    Dealer dealer;
    Host host;

    public Game(List<Player> players){
        this.players = players;
        dealer = new Dealer();
        host = new Host();
    }

    public Game(Player player){
        players = new ArrayList<>();
        players.add(player);
        dealer = new Dealer();
        host = new Host();
    }

    public void startGame(){
        //轮流单播
        for(Player player:players){
            SendMessage.toTarget(Obj2Json.playerInitMsg(player), player);
        }

        //广播 whoTrun ： gameStart
        SendMessage.toTarget(Obj2Json.startGameMsg(),players.get(0));
        dealer.firstTurn(players, host);
        //广播 玩家和庄家的首轮结果
        JSONObject obj = Obj2Json.initialTrunJobj(players, host);
        SendMessage.toTarget(obj, players.get(0));
    }

    public void roundTrun(){
        for( Player player : players ){
            //玩家轮流行动
            String result = player.turnActions(dealer);
            if( result.equals("boom") ){
                //这里可能涉及到一些筹码计算
                System.out.println("玩家"+player.getPos()+"爆牌出局");
            }else {
                System.out.println("玩家"+player.getPos()+"行动结束");
            }
        }
        //庄家行动
        host.turnActions(dealer,players.get(0));
        System.out.println("庄家行动结束");
    }

    public void settleGame(){
        try {//等待
            Thread.currentThread().sleep(1500);
        }catch (Exception e){}

        Boolean hostBoom = host.getHandCards().getSumValue()>21;
        for( Player player : players ){
            if(player.getHandCards().getSumValue() > 21){
                System.out.println(player.getPos()+ " lose");
                player.setResult("lose");
            }else if(hostBoom ||
               player.getHandCards().getSumValue() > host.getHandCards().getSumValue()
            ){
                //将结果加入json
                System.out.println(player.getPos()+ " win");
                player.setResult("win");
            } else if (player.getHandCards().getSumValue() == host.getHandCards().getSumValue()) {
                //将结果加入json
                System.out.println(player.getPos()+ " push");
                player.setResult("push");
            }else{
                //将结果加入json
                System.out.println(player.getPos()+ " lose");
                player.setResult("lose");
            }
        }
        //广播结果
        JSONObject result = Obj2Json.settleTrunJobj(players, host);
        SendMessage.toTarget(result,players.get(0));

    }

    public void restartGame(){
        for( Player player : players ){
            player.restart();
        }
    }

}
