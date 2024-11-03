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
        player.getHandCards().AddCards(dealCard());
        player.getHandCards().AddCards(dealCard());

        //给庄家发
        Card newcard = dealCard();
        newcard.setUp(false);
        host.getHandCards().AddCards(newcard);//第一张牌反面朝下
        host.getHandCards().AddCards(dealCard());
    }


    /**
     * 发牌方法，从牌堆中随机抽取一张牌并移除
     * @return 抽取的牌
     */
    public Card dealCard() {
        List<Card> deck = allCards.getDeck();
        if (deck.isEmpty()) {
            allCards.createAllCards();
            //throw new IllegalStateException("No more cards in the deck!");
        }
        // 移除并返回第一张牌
        return deck.removeFirst();
    }
}
