package me.allen.imeji.webapp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.allen.imeji.ImeJi;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

@RequiredArgsConstructor
public class ImeJiWebApp {
    private final ImeJi imeJi;

    @SneakyThrows
    public void start() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(this.imeJi.getConfigurationProperties().getProperty("tomcat.port")));

        //https://stackoverflow.com/questions/11669507/embedded-tomcat-7-servlet-3-0-annotations-not-working
        String webappDirLocation = "src/main/webapp/";

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/embeddedTomcat",
                new File(webappDirLocation).getAbsolutePath());

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }

}
