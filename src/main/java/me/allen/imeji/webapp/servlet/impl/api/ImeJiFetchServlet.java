package me.allen.imeji.webapp.servlet.impl.api;

import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.response.WebError;
import me.allen.imeji.response.WebResponseEntity;
import me.allen.imeji.webapp.servlet.ImeJiWebServlet;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;


public class ImeJiFetchServlet implements ImeJiWebServlet {

    @GET("/api/fetch/{id}")
    public WebResponseEntity onFetch(Req req, Resp resp, String id) {
        req.async();

        if (id == null) {
            return WebResponseConstant.IMAGE_DOES_NOT_EXIST;
        }

        try {
            ImeJiImage imeJiImage = ImeJi.getInstance()
                    .getCacheController()
                    .pullFromCacheSync(id);

            if (imeJiImage == null) {
                imeJiImage = ImeJi.getInstance()
                        .getDatabaseController()
                        .fetchImejiImageSync(id);
            }

            if (imeJiImage == null) {
                resp.code(404);
                return WebResponseConstant.IMAGE_DOES_NOT_EXIST;
            } else {
                ImeJi.getInstance()
                        .getCacheController()
                        .saveToCache(imeJiImage);
                return imeJiImage;
            }
        } catch (Exception ex) {
            return new WebError(500,
                    "Internal Server Error",
                    ex.getMessage());
        }
    }

    @GET("/api/fetch")
    public WebResponseEntity blockNonParameterGet(Resp resp) {
        resp.code(404);
        return WebResponseConstant.IMAGE_DOES_NOT_EXIST;
    }

    @POST("/api/fetch/{id}")
    public WebResponseEntity blockPost(Resp resp, String id) {
        resp.code(401);
        return WebResponseConstant.GET_REQUEST_ONLY;
    }

    @POST("/api/fetch")
    public WebResponseEntity blockPost(Resp resp) {
        resp.code(401);
        return WebResponseConstant.GET_REQUEST_ONLY;
    }
}
