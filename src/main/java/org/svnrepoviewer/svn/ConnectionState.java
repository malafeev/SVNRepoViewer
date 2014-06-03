package org.svnrepoviewer.svn;


public class ConnectionState {
    private boolean badCredentials;

    public boolean isBadCredentials() {
        return badCredentials;
    }

    public void setBadCredentials(boolean badCredentials) {
        this.badCredentials = badCredentials;
    }
}
