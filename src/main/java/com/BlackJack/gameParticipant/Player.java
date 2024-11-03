package com.BlackJack.gameParticipant;

import com.BlackJack.gameProps.Card;
import com.BlackJack.gameProps.HandCards;
import com.BlackJack.handler.GetDecisionHandler;
import com.BlackJack.toolClass.SendMessage;
import com.BlackJack.toolClass.Obj2Json;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


@Data
public class Player {
   	private String pos;
    private String result;
    private HandCards handCards = new HandCards();
	private Channel channel;
    private BlockingQueue<String> decisionQueue = new LinkedBlockingQueue<>(); // 用于存放决策

    public Player (String pos, Channel channel){
        this.pos = pos;
        this.channel = channel;
        channel.pipeline().addLast(new GetDecisionHandler(this));
    }


    private String getDecision() {
        // 请求客户端决策，并等待响应
        try {
            JSONObject playerTurn = Obj2Json.playerTrunMsg(pos);
            SendMessage.toTarget(playerTurn,this);
            // 从队列中获取决策
            String operation = decisionQueue.poll(50, TimeUnit.SECONDS);// 设置超时时间
            if(operation!=null){
                return operation;
            }else {
                return "stand";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for client's decision.");
        }
        return null;
    }


    public void setDecision(String decision){
        decisionQueue.offer(decision); // 向队列中添加决策
    }

    public String turnActions(Dealer dealer){
        if (handCards.getSumValue()>21){
            //爆牌结束轮次
            return "boom";
        }

        while (getDecision().equals("hit")){
            Card newCard = dealer.dealCard();
            handCards.AddCards(newCard);

            //这里发消息
            //发送给玩家的消息
            JSONObject toTarget= Obj2Json.hitRes(this,newCard);
            SendMessage.toTarget(toTarget,this);

            if (handCards.getSumValue()>21){
                //爆牌结束轮次
                return "boom";
            }
        }
        //停牌后向所前端发送stand
        JSONObject toTarget = Obj2Json.standRes(pos);
        SendMessage.toTarget(toTarget,this);
        return "notBoom";
    }
}
