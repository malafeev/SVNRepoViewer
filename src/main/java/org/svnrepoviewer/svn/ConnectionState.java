package org.svnrepoviewer.svn;


public class ConnectionState {
    private boolean badCredentials;
    private boolean connected;

    public boolean isBadCredentials() {
        return badCredentials;
    }

    public void setBadCredentials(boolean badCredentials) {
        this.badCredentials = badCredentials;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
