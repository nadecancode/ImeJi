package me.allen.imeji.webapp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.webapp.servlet.impl.ImeJiFetchServlet;
import me.allen.imeji.webapp.servlet.impl.ImeJiUploadServlet;
import org.rapidoid.setup.App;
import org.rapidoid.setup.On;

@RequiredArgsConstructor
public class ImeJiWebApp {
    private final ImeJi imeJi;

    @SneakyThrows
    public void start() {
        On.port(Integer.parseInt(this.imeJi.getConfigurationProperties().getProperty("web.port")));
        App.beans(
                new ImeJiFetchServlet(),
                new ImeJiUploadServlet()
        );
    }
}
