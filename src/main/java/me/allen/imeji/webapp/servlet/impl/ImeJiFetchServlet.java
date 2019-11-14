package me.allen.imeji.webapp.servlet.impl;

import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.response.WebError;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;
import me.allen.imeji.response.WebResponse;
import me.allen.imeji.webapp.servlet.ImeJiWebServlet;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.data.JSON;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;


public class ImeJiFetchServlet implements ImeJiWebServlet {

    @GET("/api/fetch/{id}")
    public WebResponse onFetch(Req req, Resp resp, String id) {
        if (!req.realIpAddress().equals("0:0:0:0:0:0:0:1")) { //I don't know but it's returning the IPv6 value so I'd just use IPv6 to check shits
            return WebResponseConstant.LOCAL_HOST_ONLY;
        }

        if (id == null) {
            return WebResponseConstant.IMAGE_DOES_NOT_EXIST;
        }

        try {
            ImeJiImage imeJiImage = ImeJi.getInstance()
                    .getDatabaseController()
                    .fetchImejiImageSync(id);

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
    public WebResponse blockNonParameterGet(Resp resp) {
        resp.code(404);
        return WebResponseConstant.IMAGE_DOES_NOT_EXIST;
    }

    @POST("/api/fetch/{id}")
    public WebResponse blockPost(Resp resp, String id) {
        resp.code(401);
        return WebResponseConstant.GET_REQUEST_ONLY;
    }

    @POST("/api/fetch")
    public WebResponse blockPost(Resp resp) {
        resp.code(401);
        return WebResponseConstant.GET_REQUEST_ONLY;
    }
}
