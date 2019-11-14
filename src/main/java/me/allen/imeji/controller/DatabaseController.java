package me.allen.imeji.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class DatabaseController {

    private final ImeJi imeJi;

    @Getter
    private ConcurrentLinkedQueue<ImeJiImage> imeJiQueue = new ConcurrentLinkedQueue<>();

    public void saveToQueue(ImeJiImage imeJiImage) {
        this.imeJiQueue.add(imeJiImage);
    }

    /*
       Only use for sync access because the webapp is async already
     */
    public ImeJiImage fetchImejiImageSync(String imejiId) {
        return  this.imeJi.getTinyORM().single(ImeJiImage.class)
                .where("id=?", imejiId)
                .execute()
                .orElse(null);
    }

    public void fetchImejiImage(String imejiId, Consumer<ImeJiImage> consumer) {
        this.imeJi
                .getTaskFactory()
                .newChain()
                .async(() -> consumer.accept(this.imeJi.getTinyORM().single(ImeJiImage.class)
                        .where("id=?", imejiId)
                        .execute()
                        .orElse(null)))
                .execute();
    }
}
