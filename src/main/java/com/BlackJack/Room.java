package com.BlackJack;
import com.BlackJack.gameParticipant.Player;
import io.netty.channel.Channel;
import lombok.Getter;

import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Room {
    private final List<Player> players = new ArrayList<>();

    private final int requiredPlayers = 1;//游戏需要的人数

    private Game game;

    private  CountDownLatch readyLatch = new CountDownLatch(requiredPlayers);

    public void startGame(){
        game = new Game(players);
        game.startGame();
        game.roundTrun();
        game.settleGame();
        //game.restartGame();
    }

    public synchronized void addPlayer(Player player) {
        if (player != null && !players.contains(player)) {
            players.add(player);
            readyLatch.countDown(); // 每添加一个玩家就减1
            notifyAll(); // 唤醒等待的线程
        }
    }

    public boolean allReady() {
        return readyLatch.getCount() == 0;
    }

    public synchronized void removePlayer(Player player) {
        if (players.remove(player)) {
            // 如果需要重新设置计数器，可以在这里重置readyLatch
            readyLatch= new CountDownLatch(requiredPlayers);
            notifyAll(); // 唤醒等待的线程
        }
    }

    public Player findPlayer(Channel channel){
        for(Player player : players){
            if(player.getChannel().equals(channel))
                return player;
        }
        return null;
    }

}