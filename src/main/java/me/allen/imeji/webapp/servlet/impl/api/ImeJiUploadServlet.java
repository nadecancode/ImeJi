package me.allen.imeji.webapp.servlet.impl.api;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.response.WebResponse;
import me.allen.imeji.response.WebResponseEntity;
import me.allen.imeji.webapp.servlet.ImeJiWebServlet;
import org.apache.commons.codec.binary.Base64;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ImeJiUploadServlet implements ImeJiWebServlet {

    @POST("/api/upload")
    public WebResponseEntity upload(Req req, Resp resp) {
        req.async();

        if (req.body() == null || req.body().length <= 0) { //Should not happen but just in case
            resp.code(401);
            return WebResponseConstant.POST_REQUEST_ONLY;
        }

        if (!ImeJi.getInstance().getImeJiWebApp().getAuthorizationController().hasSession(req)) {
            resp.code(403);
            return WebResponseConstant.UNAUTHORIZED_REQUEST;
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

        if (!imejiObject.has("encodedImages")) {
            resp.code(400);
            return WebResponseConstant.INCORRECT_CONTENT_FORM;
        }

        List<String> encodedObjects = Lists.newArrayList();

        for (JsonElement imageElement : imejiObject.getAsJsonArray("encodedImages")) {
            if (!imageElement.isJsonObject()) {
                resp.code(400);
                return WebResponseConstant.INCORRECT_CONTENT_FORM;
            }

            JsonObject imageObject = imageElement.getAsJsonObject();
            String encodedImage = imageObject.getAsString();
            if (!Base64.isBase64(encodedImage)) {
                resp.code(400);
                return WebResponseConstant.INCORRECT_CONTENT_FORM;
            }

            encodedObjects.add(encodedImage);
        }

        ImeJiImage imeJiImage = ImeJiImage.create(encodedObjects);
        if (imeJiImage == null) {
            resp.code(400);
            return WebResponseConstant.INCORRECT_CONTENT_FORM;
        }

        ImeJi.getInstance()
                .getDatabaseController()
                .saveToQueue(imeJiImage);

        resp.code(200);
        return new WebResponse(
                200,
                "Image Upload Success",
                "The specified image has been uploaded to the server",
                imeJiImage.getId()
        );
    }

    @GET("/api/upload")
    public WebResponseEntity blockUpload() {
        return WebResponseConstant.POST_REQUEST_ONLY;
    }

}
