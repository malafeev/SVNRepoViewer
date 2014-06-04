package org.svnrepoviewer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

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
import org.svnrepoviewer.fx.Main;

/**
 * @author sergei.malafeev
 */
@ComponentScan
@EnableAutoConfiguration
public class SVNApplication {
    private static final Logger logger = LoggerFactory.getLogger(SVNApplication.class);
    private static int port;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(port);
        factory.setSessionTimeout(0, TimeUnit.MINUTES);
        return factory;
    }

    public static void main(String[] args) throws InterruptedException {
        parseCLI(args);
        ConfigurableApplicationContext context = SpringApplication.run(SVNApplication.class, args);
        Main.main(new String[]{port + ""});
        context.close();
    }

    private static void parseCLI(String[] args) {
        Options options = new Options();
        options.addOption("p", "port", true, "port number");

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String portStr = cmd.getOptionValue("p");
            if (portStr != null) {
                try {
                    port = Integer.parseInt(portStr);
                } catch (NumberFormatException e) {
                    logger.error("cannot parse port {}", portStr);
                    System.exit(-1);
                }
                if (port < 1 || port > 65534) {
                    logger.error("port should be in range [1, 65534]");
                    System.exit(-1);
                }
            } else {
                port = findFreePort();
            }
        } catch (ParseException e) {
            logger.error(e.getMessage());
            printUsage(options);
            System.exit(-1);
        }
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
