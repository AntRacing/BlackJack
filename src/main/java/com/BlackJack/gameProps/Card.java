package com.BlackJack.gameProps;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private String value;
    private String suit;
    private boolean isUp;
    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
        this.isUp = true; // 默认设置为正面朝上
    }
    public int getValueAsNumber() {
        switch (value) {
            case "J":
            case "Q":
            case "K":
                return 10;
            case "A":
                return 11; // 先假设A为11，如果总和超过21，则需要调整
            default:
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return 0; // 或者抛出异常，取决于如何处理错误
                }
        }
    }
}
