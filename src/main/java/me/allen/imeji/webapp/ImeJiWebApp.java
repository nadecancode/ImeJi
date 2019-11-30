package me.allen.imeji.webapp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.controller.web.WebAuthorizationController;
import me.allen.imeji.webapp.servlet.impl.api.ImeJiFetchServlet;
import me.allen.imeji.webapp.servlet.impl.api.ImeJiUploadServlet;
import me.allen.imeji.webapp.servlet.impl.page.ImeJiImageViewServlet;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.setup.App;
import org.rapidoid.setup.On;
import org.rapidoid.u.U;

@RequiredArgsConstructor
public class ImeJiWebApp {
    private final ImeJi imeJi;

    @Getter
    private WebAuthorizationController authorizationController;

    @SneakyThrows
    public void start() {
        String url = this.imeJi.getConfigurationProperties().getProperty("server.url");
        int port = Integer.parseInt(url.split(":")[2]);
        String authorizationKey = this.imeJi.getConfigurationProperties().getProperty("authorization.key");
        this.authorizationController = new WebAuthorizationController(authorizationKey);

        On.port(port);
        App.beans(
                new ImeJiFetchServlet(),
                new ImeJiUploadServlet(),
                new ImeJiImageViewServlet()
        );


        On.page("/").view("index")
                .mvc((Req req, Resp resp) -> {
                    if (this.authorizationController.hasSession(req)) this.authorizationController.removeSession(req);
                    this.authorizationController.setSession(req);

                    return U.map("url", url, "doka", Boolean.parseBoolean(this.imeJi.getConfigurationProperties().getProperty("doka", "false")));
                });
    }
}
