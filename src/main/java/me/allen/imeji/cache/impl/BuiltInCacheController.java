package me.allen.imeji.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.cache.ICacheController;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class BuiltInCacheController implements ICacheController {

    private final ImeJi imeJi;

    private Cache<String, JsonObject> imageLoadingCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Override
    public void saveToCache(ImeJiImage imeJiImage) {
        this.imageLoadingCache.put(imeJiImage.getId().toLowerCase(), new ImeJiImageMapper().toJsonObject(imeJiImage));
    }

    @Override
    public void pullFromCache(String id, Consumer<ImeJiImage> cachedImageConsumer) {
        this.imeJi
                .getTaskFactory()
                .newChain()
                .async(() -> cachedImageConsumer.accept(pullFromCacheSync(id)));
    }

    @Override
    public ImeJiImage pullFromCacheSync(String id) {
        JsonObject jsonObject = this.imageLoadingCache.getIfPresent(id.toLowerCase());
        ImeJiImage cachedImage = null;

        if (jsonObject != null) cachedImage = new ImeJiImageMapper().fromJsonObject(jsonObject);

        return cachedImage;
    }

}
