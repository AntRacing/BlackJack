package com.BlackJack.gameProps;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HandCards {

    List<Card> handCards;
    int sumValue;

    public HandCards(){
        handCards = new ArrayList<>();
    }

    public void AddCards(Card card){
        handCards.add(card);
        sumValue=getTotalValue();
    }
    // 清空所有的卡片
    public void clear() {
        handCards.clear();
    }

    private int getTotalValue() {
        int total = 0;
        int aces = 0;
        for (Card card : handCards) {
            if ("A".equals(card.getValue())) {
                aces++;
                total += 11; // 先假设所有的 A 都是 11 分
            } else {
                total += card.getValueAsNumber();
            }
        }

        // 调整 A 的值，避免超过 21 分
        while (total > 21 && aces > 0) {
            total -= 10; // 把一个 A 的值从 11 改为 1
            aces--;
        }

        return total;
    }

}
