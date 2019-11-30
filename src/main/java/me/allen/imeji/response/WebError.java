package me.allen.imeji.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebError implements WebResponseEntity {
    private final int code;
    private final String message, description;
}
