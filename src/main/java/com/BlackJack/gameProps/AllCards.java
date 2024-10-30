package com.BlackJack.gameProps;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class AllCards {
    List<Card> deck;

    public AllCards(){
        createAllCards();
    }

    private void createAllCards(){
        List<String> suits = Arrays.asList("spades", "hearts", "diamonds", "clubs"); // 英文花色
        List<String> values = Arrays.asList(
                "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"); // 牌面值

        deck = new ArrayList<>();

        for (String suit : suits) {
            for (String value : values) {
                Card card = new Card(value, suit);
                deck.add(card);
            }
        }

        shuffleDeck();
    }


    /**
     * 洗牌方法
     */
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * 发牌方法，从牌堆中随机抽取一张牌并移除
     * @return 抽取的牌
     */
    public Card dealCard() {
        if (deck.isEmpty()) {
            createAllCards();
            //throw new IllegalStateException("No more cards in the deck!");
        }
        // 移除并返回第一张牌
        return deck.removeFirst();
    }

}