package com.BlackJack.gameParticipant;

import com.BlackJack.gameProps.Card;
import com.BlackJack.gameProps.HandCards;
import com.BlackJack.inTools.GroupMessageHandler;
import com.BlackJack.inTools.Obj2Json;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;


@Getter
public class Host {
    private HandCards handCards = new HandCards();

    public String turnActions(Dealer dealer){
        if (handCards.getSumValue()>21){
            //爆牌结束轮次
            return "boom";
        }

        while (getDecision().equals("hit")){
            Card newCard = dealer.getAllCards().dealCard();
            handCards.AddCards(newCard);
            if (handCards.getSumValue()>21){
                //爆牌结束轮次
//                System.out.println("庄家爆牌出局");
                GroupMessageHandler.toGroup("庄家爆牌，轮次结束");
                return "boom";
            }
            //这里发消息
            JSONObject obj= Obj2Json.hitRes(this,newCard);
            GroupMessageHandler.toGroup(obj);
        }

        GroupMessageHandler.toGroup("庄家轮次结束");
        return "notBoom";
    }

    private String getDecision () {
        if (handCards.getSumValue()<17){
            return "hit";
        }
        return "stay";
    }
}
