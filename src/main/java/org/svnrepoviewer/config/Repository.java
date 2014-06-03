package org.svnrepoviewer.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Repository implements Comparable<Repository> {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repository repository = (Repository) o;

        return !(url != null ? !url.equals(repository.url) : repository.url != null);

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public int compareTo(Repository repository) {
        return url.compareTo(repository.url);
    }
}
