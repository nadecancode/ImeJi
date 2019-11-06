package me.allen.imeji.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;

import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
public class DatabaseController {

    private final ImeJi imeJi;

    @Getter
    private ConcurrentLinkedQueue<ImeJiImage> imeJiQueue = new ConcurrentLinkedQueue<>();

    public void saveToQueue(ImeJiImage imeJiImage) {
        this.imeJiQueue.add(imeJiImage);
    }

    public ImeJiImage fetchImejiImage(String imejiId) {
        return this.imeJi.getTinyORM().single(ImeJiImage.class)
                .where("id=?", imejiId)
                .execute()
                .orElse(null);
    }
}
