package org.svnrepoviewer.config;


import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

@Component
public class ConfigDao {
    private Config config;
    private final ObjectMapper mapper = new ObjectMapper();
    private final File configFile;
    private static final Logger logger = LoggerFactory.getLogger(ConfigDao.class);

    public ConfigDao() {
        configFile = new File(System.getProperty("user.home") + "/.svnrepoviewer/svnrepoviewer.json");

        if (configFile.exists()) {
            try {
                config = mapper.readValue(configFile, Config.class);
            } catch (IOException e) {
                logger.error("cannot read config file", e);
            }
        } else {
            try {
                Files.createParentDirs(configFile);
            } catch (IOException e) {
                logger.error("cannot create dir: {}", configFile.getParentFile().getAbsolutePath());
            }
        }
        if (config == null) {
            config = new Config();
        }
    }

    public synchronized void save() {
        try {
            mapper.writer().withDefaultPrettyPrinter().writeValue(configFile, config);
        } catch (IOException e) {
            logger.error("cannot save config file", e);
        }
    }

    public synchronized Set<Repository> getRepositories() {
        return config.getRepositories();
    }


    public synchronized boolean addRepository(Repository repository) {
        return config.addRepository(repository);
    }

    public synchronized void deleteRepositoryByUrl(String url) {
        config.deleteRepository(findRepositoriesByUrl(url));
    }

    public synchronized Repository findRepositoriesByUrl(String url) {
        for (Repository repository : config.getRepositories()) {
            if (repository.getUrl().equals(url)) {
                return repository;
            }
        }
        return null;
    }
}
