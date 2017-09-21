package io.clonalejandro;

import io.clonalejandro.utils.PacketInjector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import me.clonalejandro.ReflectionAPI.ReflectionAPI;
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

public abstract class PacketListener extends PacketInjector {


    /** SMALL CONSTRUCTORS **/

    public PacketListener(Player player) throws Exception {
        super(PacketListener.pipeline(player), PacketListener.channel(player));
    }

    public abstract void actionInjector();


    /** REST **/

    @Override
    public void aI(){
        actionInjector();
    }


    /** OTHERS **/

    /**
     * This method define and return a pipeline
     * @param player
     * @return
     * @throws Exception
     */
    private static ChannelPipeline pipeline(Player player) throws Exception{
        try {
            final Class<?> playerClazz = player.getClass();
            final Object handle = playerClazz.getMethod("getHandle").invoke(player),
                         playerConnection = handle.getClass().getField("playerConnection").get(handle),
                         networkManager = playerConnection.getClass().getField("networkManager").get(playerConnection),
                         channel = networkManager.getClass().getDeclaredField("channel").get(networkManager);

            return (ChannelPipeline) channel.getClass().getDeclaredMethod("pipeline").invoke(channel);
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * This method define and return a Channel
     * @param player
     * @return
     * @throws Exception
     */
    private static Channel channel(Player player) throws Exception{
        try {
            final Class<?> playerClazz = player.getClass();
            final Object handle = playerClazz.getMethod("getHandle").invoke(player),
                         playerConnection = handle.getClass().getField("playerConnection").get(handle),
                         networkManager = playerConnection.getClass().getField("networkManager").get(playerConnection);

            return (Channel) networkManager.getClass().getDeclaredField("channel").get(networkManager);
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


}