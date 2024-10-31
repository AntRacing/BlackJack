package com.BlackJack;

import com.BlackJack.handler.LoadInHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class GameServer {
    public static final Room room = new Room();//新建游戏室
    //public static List<Game> games = new ArrayList<>();
    public static BlockingQueue<Game> games = new LinkedBlockingQueue<>();
    private static ExecutorService gameExecutor = Executors.newFixedThreadPool(10);
    public static void start(){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        //初始化服务端
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //添加http编码解码器
                        pipeline.addLast(new HttpServerCodec());
                        //支持大数据流
                        pipeline.addLast(new ChunkedWriteHandler());
                        //http消息聚合
                        pipeline.addLast(new HttpObjectAggregator(1024*64));
                        //websocket支持
                        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                        //添加逻辑处理
                        pipeline.addLast(new LoadInHandler());
                    }
                });
        //绑定端口
        ChannelFuture future = bootstrap.bind(8080);

//        while (true){
//            try {
//                Game game = games.take();
//                game.startGame();
//                game.roundTrun();
//                game.settleGame();
//            }catch (Exception e){
//            }
//        }
        // 提交任务到线程池
        gameExecutor.submit(() -> {
            while (true) {
                try {
                    Game game = games.take();
                    gameExecutor.submit(() -> {
                        game.startGame();
                        game.roundTrun();
                        game.settleGame();
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // 阻塞主线程，直到所有玩家准备好

//        while (true){
//            try {
//                if (!room.allReady()) {
//                    System.out.println("Waiting for players to be ready...");
//                    room.getReadyLatch().await(); // 阻塞直到计数器为零
//                }
//                room.startGame();//开始游戏
//                Thread.currentThread().sleep(3000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                throw new RuntimeException("Interrupted while waiting for players to be ready.", e);
//            }
//        }

//            Game game = new Game(room.getPlayers());
//            game.startGame();
//            game.roundTrun();
//            game.settleGame();


        // 关闭EventLoopGroup
//        boss.shutdownGracefully();
//        worker.shutdownGracefully();
    }
}
