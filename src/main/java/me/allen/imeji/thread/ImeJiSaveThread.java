package me.allen.imeji.thread;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.bean.form.ImeJiInsertForm;

import java.util.Queue;

@RequiredArgsConstructor
public class ImeJiSaveThread extends Thread {

    private final ImeJi imeJi;

    @Override
    @SneakyThrows
    public void run() {
        Queue<ImeJiImage> imeJiImages = this.imeJi.getDatabaseController().getImeJiQueue();
        while (!imeJiImages.isEmpty()) {
            ImeJiImage image = imeJiImages.poll();
            ImeJiInsertForm imeJiInsertForm = new ImeJiInsertForm(image);
            this.imeJi.getTinyORM()
                    .insert(ImeJiImage.class)
                    .valueByBean(imeJiInsertForm)
                    .execute();

            this.imeJi.getCacheController().saveToCache(image);

            Thread.sleep(100L);
        }
    }

}
