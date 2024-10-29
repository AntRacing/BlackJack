package com.BlackJack.gameParticipant;

import com.BlackJack.gameProps.Card;
import com.BlackJack.gameProps.HandCards;
import com.BlackJack.handler.GetDecisionHandler;
import com.BlackJack.inTools.GroupMessageHandler;
import com.BlackJack.inTools.Obj2Json;
import com.BlackJack.inTools.Result;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
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

//    public String getDecision(){
//        //发送消息 收到的结果是“hit”或者“stay”
//        return "stay";//测试就先让它无脑停牌
//    }
    public String getDecision() {

        // 请求客户端决策，并等待响应
//        channel.writeAndFlush(Result.success(playerTurn)).addListener((ChannelFutureListener) future -> {
//            if (!future.isSuccess()) {
//                System.err.println("Failed to send the request to the client.");
//                System.err.println(future.cause().getMessage()); // 输出异常信息
//            }
//        });

        try {
            JSONObject playerTurn = Obj2Json.playerTrunMsg(this);
            GroupMessageHandler.toGroup(playerTurn);
            // 从队列中获取决策
            String operation = decisionQueue.poll(150, TimeUnit.SECONDS);// 设置超时时间
            return operation;
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
            //System.out.println("player boom");
            return "boom";
        }

        while (getDecision().equals("hit")){
            Card newCard = dealer.getAllCards().dealCard();
            handCards.AddCards(newCard);

            //这里发消息
            //发送给玩家的消息
            JSONObject toTarget= Obj2Json.hitRes(this,newCard);
            channel.writeAndFlush(Result.success(toTarget));
            //发送给其他玩家的消息
            JSONObject toOther= Obj2Json.hitResOther(this);
            GroupMessageHandler.toOthers(toOther,this);

            if (handCards.getSumValue()>21){
                //爆牌结束轮次
                //System.out.println("player boom");
                //GroupMessageHandler.toGroup(JSONObject.toJSONString("玩家 "+this.pos+" 爆牌，轮次结束"));
                return "boom";
            }
        }
        //GroupMessageHandler.toGroup(JSONObject.toJSONString("玩家 "+this.pos+" 轮次结束"));
        return "notBoom";
    }
}
