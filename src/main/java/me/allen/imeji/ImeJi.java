package me.allen.imeji;

import co.aikar.taskchain.TaskChainFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import me.allen.imeji.bean.ImeJiImage;
import me.allen.imeji.cache.ICacheController;
import me.allen.imeji.cache.impl.BuiltInCacheController;
import me.allen.imeji.cache.impl.RedisCacheController;
import me.allen.imeji.controller.DatabaseController;
import me.allen.imeji.task.ImeJiTaskFactory;
import me.allen.imeji.thread.ImeJiPrimaryThread;
import me.allen.imeji.thread.ImeJiSaveThread;
import me.allen.imeji.webapp.ImeJiWebApp;
import me.geso.tinyorm.TinyORM;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

@Getter
public class ImeJi {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private TinyORM tinyORM;
    private DatabaseController databaseController;
    private ICacheController cacheController;

    private Properties configurationProperties = new Properties();
    private ImeJiPrimaryThread primaryThread = new ImeJiPrimaryThread();
    private TaskChainFactory taskFactory;

    @Getter
    private static ImeJi instance;

    public ImeJi() {
        instance = this;
        this.start();
    }

    @SneakyThrows
    public void start() {
        this.configurationProperties.load(ImeJi.class.getClassLoader().getResourceAsStream("config.properties"));

        this.startDatabase();
        this.startTasks();
        this.startCacheService();
        this.startWebApp();
    }

    @SneakyThrows
    private void startWebApp() {
        new ImeJiWebApp(this).start();
    }

    @SneakyThrows
    private void startTasks() {
        this.taskFactory = ImeJiTaskFactory.create();
        new ImeJiSaveThread(this).start();
    }

    @SneakyThrows
    private void startDatabase() {
        this.databaseController = new DatabaseController(this);

        String user = this.configurationProperties.getProperty("mysql.user");
        String pass = this.configurationProperties.getProperty("mysql.password");
        String port = this.configurationProperties.getProperty("mysql.port");
        String host = this.configurationProperties.getProperty("mysql.host");
        String database = this.configurationProperties.getProperty("mysql.database");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://{host}:{port}/{database}"
                .replace("{host}", host)
                .replace("{port}", port)
                .replace("{database}", database)
        );
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pass);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        Connection readConnection = hikariDataSource.getConnection();
        readConnection.setReadOnly(true);

        Connection writeConnection = hikariDataSource.getConnection();
        writeConnection.setReadOnly(false);

        this.tinyORM = new TinyORM(readConnection, writeConnection);
    }

    private void startCacheService() {
        if (this.configurationProperties.getProperty("redis.enabled").equalsIgnoreCase("true")) {
            this.cacheController = new RedisCacheController(this);
        } else {
            this.cacheController = new BuiltInCacheController(this);
        }
    }
}
