package org.svnrepoviewer.config;

import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ConfigDaoTest {
    private ConfigDao configDao;

    @BeforeMethod
    public void setUp() {
        configDao = new ConfigDao();
    }


    @Test
    public void testAddAndGetRepositories() throws Exception {
        assertFalse(configDao.addRepository(null));

        Repository repository = new Repository();
        assertFalse(configDao.addRepository(repository));

        repository.setUrl("just a test url");
        assertTrue(configDao.addRepository(repository));
        assertFalse(configDao.addRepository(repository));
        assertTrue(configDao.getRepositories().contains(repository));
    }

    @Test
    public void testSaveAndDeleteRepository() {
        Set<Repository> originRepos = configDao.getRepositories();
        configDao = new ConfigDao();

        configDao.save();

        Repository repository = new Repository();
        String url = "just a test url";
        repository.setUrl(url);
        configDao.addRepository(repository);
        configDao.save();

        configDao = new ConfigDao();
        assertTrue(configDao.getRepositories().contains(repository));

        configDao.deleteRepositoryByUrl(url);
        configDao.save();
        configDao = new ConfigDao();
        assertFalse(configDao.getRepositories().contains(repository));

        configDao = new ConfigDao();
        for (Repository repo : originRepos) {
            configDao.addRepository(repo);
        }
        configDao.save();

        configDao = new ConfigDao();
        for (Repository repo : originRepos) {
            assertTrue(configDao.getRepositories().contains(repo));
        }
        assertFalse(configDao.getRepositories().contains(repository));
    }
}
