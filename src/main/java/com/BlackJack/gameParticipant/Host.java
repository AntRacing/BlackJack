package com.BlackJack.gameParticipant;

import com.BlackJack.gameProps.Card;
import com.BlackJack.gameProps.HandCards;
import com.BlackJack.toolClass.SendMessage;
import com.BlackJack.toolClass.Obj2Json;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

//import static com.BlackJack.GameServer.room;


@Getter
public class Host {
    private HandCards handCards = new HandCards();

    public String turnActions(Dealer dealer,Player target){
        if (handCards.getSumValue()>21){
            //爆牌结束轮次
            return "boom";
        }

        JSONObject playerTurn = Obj2Json.playerTrunMsg("host");
        SendMessage.toTarget(playerTurn,target);

        while (getDecision().equals("hit")){
            Card newCard = dealer.getAllCards().dealCard();
            handCards.AddCards(newCard);

            //这里发消息
            JSONObject obj= Obj2Json.hitRes(this,newCard);
            SendMessage.toTarget(obj,target);

            if (handCards.getSumValue()>21){
                //爆牌结束轮次
                return "boom";
            }
        }

        JSONObject toTarget = Obj2Json.standRes("host");
        SendMessage.toTarget(toTarget,target);

        return "notBoom";
    }

    private String getDecision () {
        try {
                Thread.currentThread().sleep(1500);
                if (handCards.getSumValue()<17){
                    return "hit";
                }
                return "stand";

        }catch (Exception e){
            System.out.println("等待出错");
        }
        return "stand";
    }
}
