package com.huydong.resource_server;

import com.huydong.resource_server.config.ApplicationProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class ResourceServerApp {

    private static final Logger log = LoggerFactory.getLogger(ResourceServerApp.class);

    private final Environment env;

    public ResourceServerApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes resource_server.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ResourceServerApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
//        testDecodeJWT();
    }
    public static void testDecodeJWT(){
        String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxLUhQNkt0NFpjcVJsTWVaZE1EcWpvYUlHM2QxS2dhd280VDlwd0F1X0hnIn0.eyJleHAiOjE3MTQ0ODkyMTUsImlhdCI6MTcxNDQ4ODkxNSwiYXV0aF90aW1lIjoxNzE0NDg4OTE0LCJqdGkiOiI4YTk4NzY0Yy1mZDY1LTQ5YTItOWEyMi1kMjU2MWQ2NmMxZjciLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwOTAvcmVhbG1zL1VFVF9BdXRob3JpemF0aW9uX3NlcnZlciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJmYTcxYmY3Mi03N2RmLTQ5MTAtYmU1MS0yOGY0NjM5OTQ0MDMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzZXJ2aWNlQSIsInNlc3Npb25fc3RhdGUiOiI1YWJjNjY0My0zNzdmLTQ4MGUtYTc4YS1lM2M2M2Y4YTU2MGUiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6OTAwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLXVldF9hdXRob3JpemF0aW9uX3NlcnZlciIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsInNpZCI6IjVhYmM2NjQzLTM3N2YtNDgwZS1hNzhhLWUzYzYzZjhhNTYwZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6ImJ1aSBkb25nIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZG9uZyIsImdpdmVuX25hbWUiOiJidWkiLCJmYW1pbHlfbmFtZSI6ImRvbmciLCJlbWFpbCI6ImRvbmdAZ21haWwuY29tIn0.QUuazFmgcNpEI9CsYJwbH4I7ULjSdg5U1BGN-QQCrm0EPfUCo5LQkipzrwurbsd3fZXm8ti3OL-U8b_di7WRRZ4QqXEL4HBp5OuL64jtp_JI2H-nxYgUJe_6nTn1lyXul9-Bn929dMqS-sZT097cJbS1RtldMLASv0SaJWOexogN7qXZx-egYqLDNuM3GWgj_QgLDCqxC8dEEqtgefFoCjpU1RrU2q2V5XK0SiE3OpTr9CtlWRTa0jXjxSC-tdHuqNt86MlM6StcjyALmc75DGe0qes3qp-8UsYgwepq8uHnOooUthj29rMUBDvzMN0LPsjheW00rpX2GFelZuryZw";
        System.out.println("------------ Decode JWT ------------");
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        System.out.println("JWT Header : " + header);


        System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        System.out.println("JWT Body : "+body);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
