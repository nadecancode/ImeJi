package me.allen.imeji.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.cache.ICacheController;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class BuiltInCacheController implements ICacheController {

    private final ImeJi imeJi;

    private Cache<String, ImeJiImage> imageLoadingCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Override
    public void saveToCache(ImeJiImage imeJiImage) {
        this.imageLoadingCache.put(imeJiImage.getId().toLowerCase(), imeJiImage);
    }

    @Override
    public void pullFromCache(String id, Consumer<ImeJiImage> cachedImageConsumer) {
        cachedImageConsumer.accept(this.imageLoadingCache.getIfPresent(id.toLowerCase()));
    }

}
