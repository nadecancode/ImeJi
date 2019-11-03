package me.allen.imeji.cache;

import me.allen.imeji.bean.ImeJiImage;

import java.util.function.Consumer;

public interface ICacheController {

    void saveToCache(ImeJiImage imeJiImage);

    void pullFromCache(String id, Consumer<ImeJiImage> cachedImage);

}
