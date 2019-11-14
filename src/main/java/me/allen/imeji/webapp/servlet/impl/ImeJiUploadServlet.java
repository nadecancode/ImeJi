package me.allen.imeji.webapp.servlet.impl;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;
import me.allen.imeji.util.WebUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

@WebServlet(
        name = "UploadServlet",
        urlPatterns = {
                "/api/upload"
        }
)

public class ImeJiUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        /*
        response.setStatus(401);
        WebUtil.sendJson(response, WebResponseConstant.POST_REQUEST_ONLY);

         */
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        /*
        if (!WebUtil.getClientIpAddress(request).equals(InetAddress.getLocalHost().getHostAddress())) {
            response.setStatus(403);
            WebUtil.sendJson(response, WebResponseConstant.LOCAL_HOST_ONLY);
            return;
        }

        String content = WebUtil.getContentFromPost(request);
        if (content == null) { //Should not happen but just in case
            response.setStatus(401);
            WebUtil.sendJson(response, WebResponseConstant.POST_REQUEST_ONLY);
            return;
        }

        JsonObject imejiObject = ImeJi.GSON.fromJson(content, JsonObject.class);
        if (imejiObject == null) {
            response.setStatus(400);
            WebUtil.sendJson(response, WebResponseConstant.INCORRECT_CONTENT_FORM);
            return;
        }

        ImeJiImage imeJiImage = new ImeJiImageMapper().fromJsonObject(imejiObject);
        if (imeJiImage == null) {
            response.setStatus(400);
            WebUtil.sendJson(response, WebResponseConstant.INCORRECT_CONTENT_FORM);
            return;
        }

        response.setStatus(200);
        WebUtil.sendJson(response, WebResponseConstant.IMAGE_UPLOAD_SUCCESS);

        ImeJi.getInstance()
                .getDatabaseController()
                .saveToQueue(imeJiImage);

         */
    }

}
