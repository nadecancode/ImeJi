package me.allen.imeji.mapper.impl;

import com.google.gson.JsonObject;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.mapper.IObjectMapper;
import org.jglue.fluentjson.JsonArrayBuilder;
import org.jglue.fluentjson.JsonBuilderFactory;

import java.util.ArrayList;
import java.util.List;

public class ImeJiImageMapper implements IObjectMapper<ImeJiImage> {

    @Override
    public JsonObject toJsonObject(ImeJiImage imeJiImage) {
        JsonArrayBuilder jsonArrayBuilder = JsonBuilderFactory
                .buildArray();
        imeJiImage.getEncodedImages().forEach(jsonArrayBuilder::add);

        return JsonBuilderFactory
                .buildObject()
                .add("id", imeJiImage.getId())
                .add("encodedImages", jsonArrayBuilder)
                .add("uploadTime", imeJiImage.getUploadedTime())
                .getJson();
    }

    @Override
    public ImeJiImage fromJsonObject(JsonObject jsonObject) {
        List<String> encodedImages = new ArrayList<>();

        if (!jsonObject.has("id") || !jsonObject.has("uploadedTime") || !jsonObject.has("encodedImages")) {
            return null;
        }

        jsonObject.getAsJsonArray("encodedImages").forEach(encodedElement -> {
            String encodedImage = encodedElement.getAsString();
            encodedImages.add(encodedImage);
        });

        ImeJiImage imeJiImage = new ImeJiImage(jsonObject.get("id").getAsString(), encodedImages);
        imeJiImage.setUploadedTime(jsonObject.get("uploadTime").getAsLong());

        return imeJiImage;
    }

}
