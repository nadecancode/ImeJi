package me.allen.imeji.cache.impl;

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
        this.jedis.set(imeJiImage.getId().toLowerCase(), new ImeJiImageMapper().toJsonObject(imeJiImage).toString());
    }

    @Override
    public void pullFromCache(String id, Consumer<ImeJiImage> cachedImage) {
        cachedImage.accept(new ImeJiImageMapper().fromJsonObject(ImeJi.GSON.fromJson(this.jedis.get(id.toLowerCase()),  JsonObject.class)));
    }

}
