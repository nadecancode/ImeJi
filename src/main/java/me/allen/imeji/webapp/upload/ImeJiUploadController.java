package me.allen.imeji.webapp.upload;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.util.id.IdentifierGenerator;
import me.geso.avans.ControllerBase;
import me.geso.avans.annotation.POST;
import me.geso.webscrew.response.WebResponse;

import java.util.ArrayList;
import java.util.List;

public class ImeJiUploadController extends ControllerBase {

    @POST("/api/upload")
    @SneakyThrows
    public WebResponse upload() {
        StringBuilder contentBuilder = new StringBuilder();
        String line;
        while ((line = this.getServletRequest().getReader().readLine()) != null) {
            contentBuilder.append(line);
        }

        String content = contentBuilder.toString();

        if (content.isEmpty()) return this.renderJSON(400, WebResponseConstant.INCORRECT_CONTENT_FORM);

        JsonObject postObject = ImeJi.GSON.fromJson(contentBuilder.toString(), JsonObject.class);
        if (!postObject.get("encodedImages").isJsonArray()) return this.renderJSON(400, WebResponseConstant.INCORRECT_CONTENT_FORM);

        List<String> encodedImages = new ArrayList<>();
        postObject.get("encodedImages").getAsJsonArray()
                .forEach(jsonElement -> encodedImages.add(jsonElement.getAsString()));

        String identifier = IdentifierGenerator.getId();
        ImeJiImage imeJiImage = new ImeJiImage(identifier, encodedImages, System.currentTimeMillis());

        if (!postObject.get("postName").isJsonNull()) imeJiImage.setPostName(postObject.get("postName").getAsString());
        if (!postObject.get("postDescription").isJsonNull()) imeJiImage.setPostName(postObject.get("postDescription").getAsString());

        return this.renderJSON(200, WebResponseConstant.IMAGE_UPLOAD_SUCCESS);
    }

}
