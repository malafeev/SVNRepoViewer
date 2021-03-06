package org.svnrepoviewer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.svnrepoviewer.fx.Main;

/**
 * @author sergei.malafeev
 */
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableTransactionManagement
public class SVNApplication {
    private static final Logger logger = LoggerFactory.getLogger("stdout");
    private static int port;
    private static boolean quiet;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(port);
        factory.setSessionTimeout(0, TimeUnit.MINUTES);
        return factory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:~/.svnrepoviewer/db");
        dataSource.setUsername("user");
        dataSource.setPassword("pwd");
        return dataSource;
    }

    public static void main(String[] args) throws InterruptedException {
        if (!parseCLI(args)) {
            return;
        }
        ConfigurableApplicationContext context = SpringApplication.run(SVNApplication.class, args);
        logger.info("started server on port {}", port);
        if (!quiet) {
            Main.main(new String[]{String.valueOf(port)});
            context.close();
        }
    }

    private static boolean parseCLI(String[] args) {
        Options options = new Options();
        options.addOption("p", "port", true, "port number");
        options.addOption("q", "quiet", false, "don't open JavaFX browser");
        options.addOption("h", "help", false, "help");
        //options.addOption("s", "spring.output.ansi.enabled", true, "spring boot output");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            printUsage(options);
            return false;
        }

        if (cmd.hasOption("h")) {
            printUsage(options);
            return false;
        }

        String portStr = cmd.getOptionValue("p");
        if (portStr != null) {
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                logger.error("cannot parse port {}", portStr);
                return false;
            }
            if (port < 1 || port > 65534) {
                logger.error("port should be in range [1, 65534]");
                return false;
            }
        } else {
            port = findFreePort();
        }

        quiet = cmd.hasOption("q");
        return true;
    }

    private static int findFreePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printUsage(Options options) {
        new HelpFormatter().printHelp("svnrepoviewer", options);
    }
}
