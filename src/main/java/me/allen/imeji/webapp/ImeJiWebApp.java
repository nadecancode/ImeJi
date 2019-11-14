package me.allen.imeji.webapp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import me.allen.imeji.webapp.servlet.ImeJiWebServlet;
import me.allen.imeji.webapp.servlet.impl.ImeJiFetchServlet;
import org.rapidoid.setup.On;

@RequiredArgsConstructor
public class ImeJiWebApp {
    private final ImeJi imeJi;

    @SneakyThrows
    public void start() {
        On.port(Integer.parseInt(this.imeJi.getConfigurationProperties().getProperty("web.port")));
        this.registerServlet(new ImeJiFetchServlet());
    }

    private void registerServlet(ImeJiWebServlet servlet) {
        servlet.start();
    }
}
