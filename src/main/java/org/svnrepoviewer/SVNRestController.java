package org.svnrepoviewer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.svnrepoviewer.dao.RepositoryDao;
import org.svnrepoviewer.domain.Repository;
import org.svnrepoviewer.svn.Connection;
import org.svnrepoviewer.svn.ConnectionState;
import org.svnrepoviewer.svn.Node;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

@RestController
public class SVNRestController {
    private static final String PRODUCE_TYPE = "application/json;charset=UTF-8";
    @Autowired
    private Connection connection;
    @Autowired
    private RepositoryDao repositoryDao;
    private Clipboard clpbrd;


    @ResponseBody
    @RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = PRODUCE_TYPE)
    public List<Node> get(@RequestParam String node) {
        if (!connection.isConnected()) {
            return new ArrayList<>();
        }
        return connection.listEntries(node);
    }

    @ResponseBody
    @RequestMapping(value = "/repositories", method = RequestMethod.GET, produces = PRODUCE_TYPE)
    public List<Repository> getRepositories() {
        return repositoryDao.getAll();
    }

    @ResponseBody
    @RequestMapping(value = "/setRepository", method = RequestMethod.POST, produces = PRODUCE_TYPE)
    public ConnectionState setRepository(@RequestParam("repository") String repoUrl, @RequestParam("password") String password) {
        Repository repository = new Repository();
        repository.setUrl(repoUrl);
        return connection.connect(repoUrl, password);
    }

    @ResponseBody
    @RequestMapping(value = "/newRepository", method = RequestMethod.POST, produces = PRODUCE_TYPE)
    public void newRepository(@RequestParam("repository") String repoUrl) {
        if (repoUrl.trim().isEmpty()) {
            return;
        }
        Repository repository = repositoryDao.get(repoUrl);
        if (repository == null) {
            repository = new Repository();
            repository.setUrl(repoUrl);
            repositoryDao.save(repository);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/editRepository", method = RequestMethod.POST, produces = PRODUCE_TYPE)
    public void editRepository(@RequestParam("oldUrl") String oldUrl, @RequestParam("newUrl") String newUrl) {
        Repository repository = repositoryDao.get(oldUrl);
        repository.setUrl(newUrl);
        repositoryDao.save(repository);
    }

    @RequestMapping(value = "deleteRepositories", method = RequestMethod.POST, produces = PRODUCE_TYPE)
    public void deleteRepositories(@RequestParam("repositories") String[] repoUrls) {
        for (String repoUrl : repoUrls) {
            repositoryDao.delete(repositoryDao.get(repoUrl));
        }
    }

    @RequestMapping(value = "copyPath", method = RequestMethod.POST, produces = PRODUCE_TYPE)
    public void copyPath(@RequestParam("path") String path) {
        String fullPath = connection.getRepoUrl() + path.replaceAll("/[/]+", "/");
        Platform.runLater(() -> {
            if (clpbrd == null) {
                clpbrd = Clipboard.getSystemClipboard();
            }
            ClipboardContent content = new ClipboardContent();
            content.putString(fullPath);
            clpbrd.setContent(content);
        });


    }
}
