package me.allen.imeji.bean;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.allen.imeji.response.WebResponseEntity;
import me.allen.imeji.util.id.IdentifierGenerator;
import me.geso.tinyorm.Row;
import me.geso.tinyorm.annotations.CreatedTimestampColumn;
import me.geso.tinyorm.annotations.CsvColumn;
import me.geso.tinyorm.annotations.PrimaryKey;
import me.geso.tinyorm.annotations.Table;

import java.util.List;

@RequiredArgsConstructor
@Table("images")
@Data
public class ImeJiImage extends Row<ImeJiImage> implements WebResponseEntity {

    @PrimaryKey
    private final String id;

    @CsvColumn
    private final List<String> encodedImages;

    @CreatedTimestampColumn
    private long uploadedTime;

    public static ImeJiImage create(List<String> encodedImages) {
        ImeJiImage imeJiImage = new ImeJiImage(
                IdentifierGenerator.getId(),
                encodedImages
        );

        imeJiImage.setUploadedTime(System.currentTimeMillis());

        return imeJiImage;
    }
}
