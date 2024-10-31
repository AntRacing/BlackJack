package com.BlackJack;

import com.BlackJack.gameParticipant.Dealer;
import com.BlackJack.gameParticipant.Host;
import com.BlackJack.gameParticipant.Player;
import com.BlackJack.toolClass.SendMessage;
import com.BlackJack.toolClass.Obj2Json;
import com.alibaba.fastjson2.JSONObject;



public class Game {
    private Player player;
    private Dealer dealer;
    private Host host;

    public Game(Player player){
        this.player=player;
        dealer = new Dealer();
        host = new Host();
    }

    public void startGame(){
        //播报身份
        SendMessage.toTarget(Obj2Json.playerInitMsg(player), player);

        //广播 whoTrun ： gameStart
        SendMessage.toTarget(Obj2Json.startGameMsg(),player);
        dealer.firstTurn(player, host);
        //广播 玩家和庄家的首轮结果
        JSONObject obj = Obj2Json.initialTrunJobj(player, host);
        SendMessage.toTarget(obj, player);
    }

    public void roundTrun(){
        //玩家行动
        player.turnActions(dealer);
        System.out.println("玩家"+player.getPos()+"行动结束");


        //庄家行动
        host.turnActions(dealer,player);
        System.out.println("庄家行动结束");
    }

    public void settleGame(){
        //等待
        try {
            Thread.currentThread().sleep(1500);
        }catch (Exception e){}
        //结果判定
        Boolean hostBoom = host.getHandCards().getSumValue()>21;
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

        //广播结果
        JSONObject result = Obj2Json.settleTrunJobj(player, host);
        SendMessage.toTarget(result,player);

    }


}
