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
                .add("postName", imeJiImage.getPostName())
                .add("postDescription", imeJiImage.getPostDescription())
                .add("uploadTime", imeJiImage.getUploadedTime())
                .add("expiry", imeJiImage.getExpiry())
                .add("likes", imeJiImage.getLikes())
                .add("views", imeJiImage.getViews())
                .getJson();
    }

    @Override
    public ImeJiImage fromJsonObject(JsonObject jsonObject) {
        List<String> encodedImages = new ArrayList<>();
        jsonObject.getAsJsonArray("encodedImages").forEach(encodedElement -> {
            String encodedImage = encodedElement.getAsString();
            encodedImages.add(encodedImage);
        });

        ImeJiImage imeJiImage = new ImeJiImage(jsonObject.get("id").getAsString(), encodedImages, jsonObject.get("uploadTime").getAsLong());
        imeJiImage.setExpiry(jsonObject.get("expiry").getAsLong());
        imeJiImage.setPostName(jsonObject.get("postName").isJsonNull() ? null : jsonObject.get("postName").getAsString());
        imeJiImage.setPostDescription(jsonObject.get("postDescription").isJsonNull() ? null : jsonObject.get("postDescription").getAsString());
        imeJiImage.setLikes(jsonObject.get("likes").getAsInt());
        imeJiImage.setViews(jsonObject.get("views").getAsInt());

        return imeJiImage;
    }

}
