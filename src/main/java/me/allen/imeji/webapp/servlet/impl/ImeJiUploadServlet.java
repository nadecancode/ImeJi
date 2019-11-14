package me.allen.imeji.webapp.servlet.impl;

import com.google.gson.JsonObject;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;
import me.allen.imeji.response.WebResponse;
import me.allen.imeji.webapp.servlet.ImeJiWebServlet;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.data.JSON;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import java.nio.charset.StandardCharsets;

public class ImeJiUploadServlet implements ImeJiWebServlet {

    @POST("/api/upload")
    public WebResponse upload(Req req, Resp resp) {
        if (!req.realIpAddress().equals("0:0:0:0:0:0:0:1")) { //I don't know but it's returning the IPv6 value so I'd just use IPv6 to check shits
            return WebResponseConstant.LOCAL_HOST_ONLY;
        }

        if (req.body() == null || req.body().length <= 0) { //Should not happen but just in case
            resp.code(401);
            return WebResponseConstant.POST_REQUEST_ONLY;
        }

        String body = new String(req.body(), StandardCharsets.UTF_8);

        JsonObject imejiObject;

        try {
            imejiObject = ImeJi.GSON.fromJson(body, JsonObject.class);
        } catch (Exception ex) {
            resp.code(400);
            return WebResponseConstant.INCORRECT_CONTENT_FORM;
        }

        //Over here, the imejiObject should no longer be null

        ImeJiImage imeJiImage = new ImeJiImageMapper().fromJsonObject(imejiObject);
        if (imeJiImage == null) {
            resp.code(400);
            return WebResponseConstant.INCORRECT_CONTENT_FORM;
        }

        ImeJi.getInstance()
                .getDatabaseController()
                .saveToQueue(imeJiImage);

        resp.code(200);
        return WebResponseConstant.IMAGE_UPLOAD_SUCCESS;
    }

    @GET("/api/upload")
    public WebResponse blockUpload() {
        return WebResponseConstant.POST_REQUEST_ONLY;
    }

}
