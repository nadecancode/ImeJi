package me.allen.imeji.webapp.servlet.impl.page;

import me.allen.imeji.webapp.servlet.ImeJiWebServlet;
import org.rapidoid.annotation.GET;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.u.U;

public class ImeJiImageViewServlet implements ImeJiWebServlet {

    @GET("/image/{id}")
    public Object view(Req req, Resp resp, String id) {
        resp.view("image.html");
        resp.mvc();
        return U.map();
    }

}
