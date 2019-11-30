package me.allen.imeji.constant;

import me.allen.imeji.response.WebError;

public class WebResponseConstant {

    public static final WebError LOCAL_HOST_ONLY = new WebError(
            403,
            "Unauthorized Request",
            "This type of requests are only allowed from localhost"
    );

    public static final WebError GET_REQUEST_ONLY = new WebError(
            401,
            "Incorrect Request Type",
            "The web server only allows GET request, please check your request type."
    );

    public static final WebError POST_REQUEST_ONLY = new WebError(
            401,
            "Incorrect Request Type",
            "The web server only allows POST request, please check your request type."
    );

    public static final WebError INCORRECT_CONTENT_FORM = new WebError(
        400,
        "Bad Content",
            "Please check your input form for server to correctly read it"
    );

    public static final WebError IMAGE_DOES_NOT_EXIST = new WebError(
        404,
        "Image Doesn't Exist",
            "The specified image cannot be found on server, maybe it has expired or deleted?"
    );

    public static final WebError UNAUTHORIZED_REQUEST = new WebError(
            403,
            "Unauthorized Request",
            "The request was not yet been authorized by the server."
    );
}
