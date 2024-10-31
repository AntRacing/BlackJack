package com.BlackJack.gameParticipant;

import com.BlackJack.gameProps.AllCards;
import com.BlackJack.gameProps.Card;
import lombok.Getter;


import java.util.List;

@Getter
public class Dealer {
    private AllCards allCards = new AllCards();

    public void firstTurn(Player player, Host host){
        //给玩家发牌
        player.getHandCards().AddCards(allCards.dealCard());
        player.getHandCards().AddCards(allCards.dealCard());

        //给庄家发
        Card newcard = allCards.dealCard();
        newcard.setUp(false);
        host.getHandCards().AddCards(newcard);//第一张牌反面朝下
        host.getHandCards().AddCards(allCards.dealCard());
    }
}
