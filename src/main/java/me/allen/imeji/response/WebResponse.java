package me.allen.imeji.response;

import lombok.Data;

@Data
public class WebResponse implements WebResponseEntity {
    private final int code;
    private final String title;
    private final String message;

    private final String attachment;
}
