package com.BlackJack.gameProps;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class AllCards {
    private List<Card> deck;

    public AllCards(){
        createAllCards();
    }

    public void createAllCards(){
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



}