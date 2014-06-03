package org.svnrepoviewer.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.util.SVNEncodingUtil;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.google.common.base.Strings;

@Component
public class Connection {
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);
    private SVNRepository repository;
    private String repoRootUrl;
    private String repoUrl;
    private boolean connected;

    public ConnectionState connect(String repoUrl, String password) {
        connected = false;
        this.repoUrl = repoUrl;
        ConnectionState connectionState = new ConnectionState();
        if (Strings.isNullOrEmpty(repoUrl)) {
            return connectionState;
        }
        DAVRepositoryFactory.setup();

        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(SVNEncodingUtil.autoURIEncode(repoUrl)));
        } catch (SVNException e) {
            logger.error("failed to init repo", e);
            return connectionState;
        }
        ISVNAuthenticationManager authManager;
        if (!Strings.isNullOrEmpty(password)) {
            authManager = SVNWCUtil.createDefaultAuthenticationManager(System.getProperty("user.name"), password);
        } else {
            authManager = SVNWCUtil.createDefaultAuthenticationManager();
        }
        repository.setAuthenticationManager(authManager);

        try {
            String repoRoot = repository.getRepositoryRoot(true).toString();
            repoRootUrl = repoUrl.replace(repoRoot, "");
        } catch (SVNAuthenticationException e) {
            connectionState.setBadCredentials(true);
            return connectionState;
        } catch (SVNException e) {
            logger.error("failed to get repository root", e);
            return connectionState;
        }

        SVNNodeKind nodeKind;
        try {
            nodeKind = repository.checkPath("", -1);
        } catch (SVNException e) {
            logger.error("failed to check repo path", e);
            return connectionState;
        }
        if (nodeKind == SVNNodeKind.NONE) {
            logger.error("There is no entry at '" + repoUrl + "'.");
            connected = false;
            return connectionState;
        } else if (nodeKind == SVNNodeKind.FILE) {
            logger.error("The entry at '" + repoUrl + "' is a file while a directory was expected.");
            connected = false;
            return connectionState;
        }
        connected = true;
        return connectionState;
    }

    public List<Node> listEntries(String path) {
        List<Node> nodes = new ArrayList<>();
        if (!connected || Strings.isNullOrEmpty(path)) {
            return nodes;
        }
        String repoPath = repoRootUrl + path;
        Collection entries;
        try {
            entries = repository.getDir(repoPath, -1, null, (Collection) null);
        } catch (SVNException e) {
            logger.error("failed to get dir", e);
            return nodes;
        }
        for (Object entry1 : entries) {
            SVNDirEntry entry = (SVNDirEntry) entry1;

            Node node = new Node();
            nodes.add(node);
            String nodePath;
            if (path.endsWith("/")) {
                nodePath = path + entry.getName();
            } else {
                nodePath = path + "/" + entry.getName();
            }

            node.setPath(nodePath);
            node.setName(entry.getName());
            node.setRevision(entry.getRevision() + "");
            node.setDate(entry.getDate());
            node.setAuthor(entry.getAuthor());
            if (entry.getKind() == SVNNodeKind.DIR) {
                node.setLeaf(false);
            } else {
                node.setLeaf(true);
            }
        }
        Collections.sort(nodes);
        return nodes;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getRepoUrl() {
        return repoUrl;
    }
}
