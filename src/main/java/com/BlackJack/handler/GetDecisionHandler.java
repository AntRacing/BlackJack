package com.BlackJack.handler;

import com.BlackJack.gameParticipant.Player;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class GetDecisionHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final Player player ;

    public GetDecisionHandler(Player player) {
        this.player = player;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        JSONObject json = JSONObject.parseObject(frame.text());
        if (json.containsKey("operation")) {
            System.out.println("收到operation");
            String operation = json.getString("operation");
            if ("hit".equals(operation) || "stay".equals(operation)) {
                player.setDecision(operation);
            }
        }
    }

}
