package me.allen.imeji.bean;

import lombok.*;
import me.geso.tinyorm.Row;
import me.geso.tinyorm.annotations.*;

import java.util.List;

@RequiredArgsConstructor
@Table("images")
@Data
public class ImeJiImage extends Row<ImeJiImage> {

    @PrimaryKey
    private final String id;

    @Column
    private String postName;

    @Column
    private String postDescription;

    @CsvColumn
    private final List<String> encodedImages;

    @CreatedTimestampColumn
    private final long uploadedTime;

    @Column
    private long expiry = -1L;

    @Column
    private int likes = 0;

    @Column
    private int views = 0;

}
