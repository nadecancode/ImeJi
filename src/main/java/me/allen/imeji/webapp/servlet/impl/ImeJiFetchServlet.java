package me.allen.imeji.webapp.servlet.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.constant.error.WebError;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;
import me.allen.imeji.webapp.servlet.ImeJiWebServlet;
import org.rapidoid.data.JSON;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.setup.On;


import java.net.InetAddress;

public class ImeJiFetchServlet implements ImeJiWebServlet {

    @Override
    public void start() {
        On.get("/api/fetch/{id}")
                .json((Req req, Resp resp) -> {
                    if (!req.realIpAddress().equals("0:0:0:0:0:0:0:1")) { //I don't know but it's returning the IPv6 value so I'd just use IPv6 to check shits
                        return WebResponseConstant.LOCAL_HOST_ONLY;
                    }

                    String id = req.param("id", null);
                    if (id != null) {
                        try {
                            ImeJiImage imeJiImage = ImeJi.getInstance()
                                    .getDatabaseController()
                                    .fetchImejiImageSync(id);

                            if (imeJiImage == null) {
                                resp.code(404);
                                return WebResponseConstant.IMAGE_DOES_NOT_EXIST;
                            } else {
                                ImeJi.getInstance()
                                        .getCacheController()
                                        .saveToCache(imeJiImage);
                                return new ImeJiImageMapper().toJsonObject(imeJiImage);
                            }
                        } catch (Exception ex) {
                            return new WebError(500,
                                    "Internal Server Error",
                                    ex.getMessage());
                        }
                    } else {
                        resp.code(404);
                        return WebResponseConstant.IMAGE_DOES_NOT_EXIST;
                    }
                });
    }

}
