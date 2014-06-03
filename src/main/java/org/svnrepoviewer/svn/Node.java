package org.svnrepoviewer.svn;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class Node implements Comparable<Node> {
    @JsonProperty("text")
    private String name;
    @JsonProperty("id")
    private String path;
    @JsonProperty("revision")
    private String revision;
    @JsonProperty("leaf")
    private boolean isLeaf;
    private Date date;
    private String author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int compareTo(Node o) {
        if (!this.isLeaf && o.isLeaf) {
            return -1;
        } else if (this.isLeaf && !o.isLeaf) {
            return 1;
        }
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (isLeaf != node.isLeaf) return false;
        if (author != null ? !author.equals(node.author) : node.author != null) return false;
        if (date != null ? !date.equals(node.date) : node.date != null) return false;
        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (path != null ? !path.equals(node.path) : node.path != null) return false;
        return !(revision != null ? !revision.equals(node.revision) : node.revision != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (revision != null ? revision.hashCode() : 0);
        result = 31 * result + (isLeaf ? 1 : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}
