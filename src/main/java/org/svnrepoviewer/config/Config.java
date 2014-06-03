package org.svnrepoviewer.config;


import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;

public class Config {
    private Set<Repository> repositories = new TreeSet<>();

    public boolean addRepository(Repository repository) {
        if (repository == null) {
            return false;
        }
        if (Strings.isNullOrEmpty(repository.getUrl())) {
            return false;
        }
        return this.repositories.add(repository);
    }

    public void deleteRepository(Repository repository) {
        repositories.remove(repository);
    }

    public Set<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(Set<Repository> repositories) {
        this.repositories = repositories;
    }
}
