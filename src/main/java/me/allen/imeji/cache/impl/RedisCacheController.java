package me.allen.imeji.cache.impl;

import co.aikar.taskchain.TaskChain;
import com.google.gson.JsonObject;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.cache.ICacheController;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

public class RedisCacheController implements ICacheController {

    private final ImeJi imeJi;

    private Jedis jedis;

    public RedisCacheController(ImeJi imeJi) {
        this.imeJi = imeJi;
        String host = this.imeJi.getConfigurationProperties().getProperty("redis.host");
        String port = this.imeJi.getConfigurationProperties().getProperty("redis.port");
        this.jedis = new Jedis(new HostAndPort(host, Integer.parseInt(port)));
    }

    @Override
    public void saveToCache(ImeJiImage imeJiImage) {
        TaskChain<String> taskChain = this.imeJi
                .getTaskFactory()
                .newChain();

        taskChain
                .syncFirst(() -> new ImeJiImageMapper().toJsonObject(imeJiImage).toString())
                .asyncLast((mapped) -> this.jedis.set(imeJiImage.getId().toLowerCase(), mapped))
                .execute();
    }

    @Override
    public void pullFromCache(String id, Consumer<ImeJiImage> cachedImageConsumer) {
        this.imeJi
                .getTaskFactory()
                .newChain()
                .async(() -> {
                    ImeJiImage cachedImage = null;

                    String s = this.jedis.get(id.toLowerCase());
                    if (!s.isEmpty()) {
                        JsonObject jsonObject = ImeJi.GSON.fromJson(s, JsonObject.class);
                        if (jsonObject != null) cachedImage = new ImeJiImageMapper().fromJsonObject(jsonObject);
                    }

                    cachedImageConsumer.accept(cachedImage);
                })
                .execute();
    }

}
