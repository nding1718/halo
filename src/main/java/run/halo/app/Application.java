package run.halo.app;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import run.halo.app.repository.base.BaseRepositoryImpl;

/**
 * Halo main class.
 *  SpringBootServletInitializer:
 *  SpringBootServletInitializer is an extension of WebApplicationInitializer which runs a SpringApplication from a
 *  traditional WAR archive deployed on a web container. This class binds Servlet, Filter and ServletContextInitializer
 *  beans from the application context to the server.
 *  Extending the SpringBootServletInitializer class also allows us to configure our application when it's run by the
 *  servlet container, by overriding the configure() method.
 *
 *
 * @author ryanwang
 * @date 2017-11-14
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@EnableJpaRepositories(basePackages = "run.halo.app.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class Application extends SpringBootServletInitializer {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        // Customize the spring config location
        System.setProperty("spring.config.additional-location", "file:${user.home}/.halo/,file:${user.home}/halo-dev/");

        // Run application
        context = SpringApplication.run(Application.class, args);
    }

    /**
     * Restart Application. Could be very handy if you want to
     *  (1) Reloading config files upon changing some parameter
     *  (2) Changing the currently active profile at runtime
     *  (3) Re-initializing the application context for any reason
     *
     *
     *  We have serveral method to restart the application
     *      <1> Restart by creating a new Context(IOC container)
     *          the method we are using now
     *      <2> Actuator's restart endpoint
     *      <3> Refresh the application context
     *      https://www.baeldung.com/java-restart-spring-boot-app
     */
    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        /**
         * As we can see in here, it's important to recreate the context in a separate non-daemon thread —
         * this way we prevent the JVM shutdown, triggered by the close method, from closing our application. Otherwise,
         * our application would stop since the JVM doesn't wait for daemon threads to finish before terminating them.
         *
         */
        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(Application.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        System.setProperty("spring.config.additional-location", "file:${user.home}/.halo/,file:${user.home}/halo-dev/");
        return application.sources(Application.class);
    }
}
