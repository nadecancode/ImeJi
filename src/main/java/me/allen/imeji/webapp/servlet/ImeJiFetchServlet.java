package me.allen.imeji.webapp.servlet;

import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.constant.WebResponseConstant;
import me.allen.imeji.mapper.impl.ImeJiImageMapper;
import me.allen.imeji.util.WebUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

@WebServlet(
        name = "FetchServlet",
        urlPatterns = "/api/fetch/*",
        loadOnStartup = 1
)
public class ImeJiFetchServlet extends HttpServlet {

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Reached 1");
        if (!WebUtil.getClientIpAddress(request).equals(InetAddress.getLocalHost().getHostAddress())) {
            response.setStatus(403);
            WebUtil.sendJson(response, WebResponseConstant.LOCAL_HOST_ONLY);
            return;
        }

        String[] pathInfo = request.getPathInfo().split("/");
        if (pathInfo.length < 4) { //The ImeJi post id is not provided in the request, automatically blocks it
            response.setStatus(404);
            WebUtil.sendJson(response, WebResponseConstant.IMAGE_DOES_NOT_EXIST);
            return;
        }

        String id = pathInfo[3];
        ImeJi.getInstance()
                .getDatabaseController()
                .fetchImejiImage(id, imeJiImage -> {
                    if (imeJiImage == null) {
                        response.setStatus(404);
                        WebUtil.sendJson(response, WebResponseConstant.IMAGE_DOES_NOT_EXIST);
                        return;
                    }

                    ImeJi.getInstance()
                            .getCacheController()
                            .saveToCache(imeJiImage);

                    response.setStatus(200);
                    WebUtil.sendJson(response, new ImeJiImageMapper().toJsonObject(imeJiImage));
                });
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(401);
        WebUtil.sendJson(response, WebResponseConstant.GET_REQUEST_ONLY);
    }

}
