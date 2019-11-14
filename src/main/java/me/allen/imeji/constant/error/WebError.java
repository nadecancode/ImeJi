package me.allen.imeji.constant.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebError {
    private final int code;
    private final String message, description;
}
