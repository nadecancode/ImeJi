package me.allen.imeji.bean.form;

import lombok.Data;
import me.allen.imeji.bean.ImeJiImage;

import java.util.List;

@Data
public class ImeJiInsertForm {

    public ImeJiInsertForm(ImeJiImage imeJiImage) {
        this.id = imeJiImage.getId();
        this.encodedImages = imeJiImage.getEncodedImages();
        this.uploadedTime = imeJiImage.getUploadedTime();
    }

    private String id;
    private List<String> encodedImages;
    private long uploadedTime;
    private int views, likes;
}
