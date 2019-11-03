package me.allen.imeji.bean.form;

import lombok.Data;
import me.allen.imeji.bean.ImeJiImage;

import java.util.List;

@Data
public class ImeJiInsertForm {

    public ImeJiInsertForm(ImeJiImage imeJiImage) {
        this.id = imeJiImage.getId();
        this.encodedImages = imeJiImage.getEncodedImages();
        this.postName = imeJiImage.getPostName();
        this.postDescription = imeJiImage.getPostDescription();
        this.expiry = imeJiImage.getExpiry();
        this.uploadedTime = imeJiImage.getUploadedTime();
        this.views = imeJiImage.getViews();
        this.likes = imeJiImage.getLikes();
    }

    private String id;
    private List<String> encodedImages;
    private String postName, postDescription;
    private long expiry, uploadedTime;
    private int views, likes;
}
