package me.allen.imeji.webapp.fetch;

import lombok.SneakyThrows;

import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;
import me.geso.avans.ControllerBase;
import me.geso.avans.annotation.POST;
import me.geso.avans.annotation.PathParam;
import me.geso.webscrew.response.WebResponse;


public class ImeJiFetchController extends ControllerBase {

    @POST("/api/fetch/{imageId}")
    @SneakyThrows
    public WebResponse fetch(@PathParam("*") String imageId) {
        ThreadLocal<ImeJiImage> imeJiImage = new ThreadLocal<>();
        ImeJi.getInstance().getCacheController().pullFromCache(imageId, (imeJiImage::set));

        if (imeJiImage.get() == null) {
            imeJiImage.set(ImeJi.getInstance().getDatabaseController().fetchImejiImage(imageId));
        }

        if (imeJiImage.get() == null) return this.renderJSON(404, WebResponseConstant.IMAGE_DOES_NOT_EXIST);

        ImeJi.getInstance().getCacheController().saveToCache(imeJiImage.get()); //Copy one object to cache

        return this.renderJSON(200, new ImeJiImageMapper().toJsonObject(imeJiImage.get()));
    }

}
