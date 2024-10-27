package com.BlackJack;

import com.BlackJack.gameParticipant.Dealer;
import com.BlackJack.gameParticipant.Host;
import com.BlackJack.gameParticipant.Player;
import com.BlackJack.inTools.GroupMessageHandler;
import com.BlackJack.inTools.Obj2Json;
import com.alibaba.fastjson2.JSONObject;

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

    public void startGame(){
        //广播 whoTrun ： gameStart
        GroupMessageHandler.toGroup(Obj2Json.startGameMsg().toJSONString());
        dealer.firstTurn(players, host);
        //广播 玩家和庄家的结果(先随便搞一下)
        JSONObject obj = Obj2Json.initialTrunJobj(players,host);
        GroupMessageHandler.toGroup(obj);
    }

    public void roundTrun(){
        for( Player player : players ){
            //广播 whotTrun : player.getPos()
            String result = player.turnActions(dealer);
            System.out.println("玩家"+player.getPos()+"行动结束");
            if( result.equals("boom") ){
                //发送出局消息 关闭channel
                //player.getChannel().writeAndFlush(Result.fail("boom"));
                //player.getChannel().close();
                System.out.println("玩家"+player.getPos()+"爆牌出局");
                //players.remove(player);
            }
        }
        //庄家行动
        host.turnActions(dealer);
        System.out.println("庄家行动结束");
    }

    public void settleGame(){
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
        GroupMessageHandler.toGroup(result);
    }

}
