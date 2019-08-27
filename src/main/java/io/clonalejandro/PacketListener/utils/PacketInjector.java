package io.clonalejandro.PacketListener.utils;

import io.netty.channel.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by alejandrorioscalera
 * On 21/9/17
 *
 * -- SOCIAL NETWORKS --
 *
 * GitHub: https://github.com/clonalejandro or @clonalejandro
 * Website: https://clonalejandro.me/
 * Twitter: https://twitter.com/clonalejandro11/ or @clonalejandro11
 * Keybase: https://keybase.io/clonalejandro/
 *
 * -- LICENSE --
 *
 * All rights reserved for clonalejandro ©PacketListener 2017 / 2018
 */

public abstract class PacketInjector {


    /** SMALL CONSTRUCTORS **/

    private final ChannelPipeline pipeline;
    private final Channel channel;

    private boolean debug;

    public PacketInjector(final ChannelPipeline pipeline, Channel channel){
        this.pipeline = pipeline;
        this.channel = channel;
    }

    public abstract void aIOnRead(ChannelHandlerContext context, Object packet);
    public abstract void aIOnWrite(ChannelHandlerContext context, Object packet, ChannelPromise promise);

    /** REST **/

    public void injectPlayer(final Player player){
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler(){

            @Override
            public void channelRead(ChannelHandlerContext context, Object packet) throws Exception{
                if (debug)
                    Bukkit.getConsoleSender().sendMessage(translator("&b&lPacketListener> &f" + packet.toString()));
                aIOnRead(context, packet);
                super.channelRead(context, packet);
            }


            @Override
            public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception{
                if (debug)
                    Bukkit.getConsoleSender().sendMessage(translator("&b&lPacketListener> &e" + packet.toString()));
                aIOnWrite(context, packet, promise);
                super.write(context, packet, promise);
            }


        };

        pipeline.addBefore("Packet_Handler", player.getName(), channelDuplexHandler);
    }


    public void uninjectPlayer(final Player player){
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }


    /** OTHERS **/

    /**
     * This method clean and return string formed with colors
     * @param str
     * @return
     */
    private String translator(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }


}