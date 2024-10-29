package com.BlackJack.inTools;

import com.BlackJack.gameParticipant.Host;
import com.BlackJack.gameParticipant.Player;
import com.BlackJack.gameProps.Card;
import com.BlackJack.gameProps.HandCards;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;


import java.util.List;

public class Obj2Json {



    public static JSONObject startGameMsg(){
        JSONObject jsonObject = new JSONObject();
        // 向 JSON 对象中添加键值对
        jsonObject.put("whoTurn", "startGame");

        return jsonObject;
    }
    public static JSONObject playerInitMsg(Player player){
        JSONObject jsonObject = new JSONObject();
        // 向 JSON 对象中添加键值对
        jsonObject.put("you", player.getPos());

        return jsonObject;
    }

    public static JSONObject playerTrunMsg(Player player){
        JSONObject jsonObject = new JSONObject();
        // 向 JSON 对象中添加键值对
        jsonObject.put("whoTurn", player.getPos());

        return jsonObject;
    }

    public static  JSONObject hitRes(Player player, Card newCard){
        JSONObject topObj = new JSONObject();

        topObj.put("whoTurn",player.getPos());
        topObj.put("operation","hit");
        JSONObject hitRes = card2Json(newCard);
        topObj.put("hitRes",hitRes);
        topObj.put("sumValue",player.getHandCards().getSumValue());
        topObj.put("isBust",player.getHandCards().getSumValue()>21);

        return  topObj;
    }

    public static  JSONObject hitResOther(Player player){
        JSONObject topObj = new JSONObject();

        topObj.put("whoTurn",player.getPos());
        topObj.put("operation","hit");
        topObj.put("isBust",player.getHandCards().getSumValue()>21);

        return  topObj;
    }

    public static  JSONObject hitRes(Host host, Card newCard){
        JSONObject topObj = new JSONObject();

        topObj.put("whoTurn","host");
        topObj.put("operation","hit");
        JSONObject hitRes = card2Json(newCard);
        topObj.put("hitRes",hitRes);
        topObj.put("sumValue",host.getHandCards().getSumValue());
        topObj.put("isBust",host.getHandCards().getSumValue()>21);

        return  topObj;
    }


    public static JSONObject initialTrunJobj(List<Player> players, Host host){
        JSONObject topObj = new JSONObject();
        JSONObject initCards = new JSONObject();
        for(Player player : players){
            JSONObject playerObj = new JSONObject();
            playerObj = player2Jobj(player);
            initCards.put(player.getPos(),playerObj);
        }
        initCards.put("host", host2Jobj(host));

        topObj.put("whoTurn","initCards");
        topObj.put("initCards",initCards);
        return topObj;
    }

    public static JSONObject settleTrunJobj(List<Player> players, Host host){
        JSONObject topObj = new JSONObject();
        JSONObject gameResult = new JSONObject();
        for(Player player : players){
            JSONObject playerObj = player2Jobj(player);
            playerObj.put("result",player.getResult());
            gameResult.put(player.getPos(),playerObj);
        }
        gameResult.put("host", host2Jobj(host));

        topObj.put("whoTurn","settle");
        topObj.put("gameResult",gameResult);
        return topObj;
    }

    private static JSONObject card2Json(Card card){
        JSONObject cardObject = new JSONObject();
        cardObject.put("faceValue", card.getValue());
        cardObject.put("color", card.getSuit());
        cardObject.put("isUp", card.isUp());
        return cardObject;
    }

    private static JSONArray cards2JArry(List<Card> cards){
        JSONArray cardsArray = new JSONArray();
        for (Card card : cards) {
            cardsArray.add(card2Json(card));
        }
        return cardsArray;
    }

    private static JSONObject player2Jobj(Player player) {
        HandCards handCards = player.getHandCards();
        List<Card> cards = handCards.getHandCards();

        JSONObject playerobj = new JSONObject();
        playerobj.put("sumValue",handCards.getSumValue());
        JSONArray cardsArray = cards2JArry(cards);
        playerobj.put("cards",cardsArray);

        return playerobj;

    }

    private static JSONObject host2Jobj(Host host) {
        HandCards handCards = host.getHandCards();
        List<Card> cards = handCards.getHandCards();

        JSONObject hostObj = new JSONObject();
        hostObj.put("sumValue",handCards.getSumValue());
        JSONArray cardsArray = cards2JArry(cards);
        hostObj.put("cards",cardsArray);

        return hostObj;
    }

}
