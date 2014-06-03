package org.svnrepoviewer.fx;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

public class Browser extends Region {
    public static final int PREF_WIDTH = 950;
    public static final int PREF_HEIGHT = 700;

    private final WebView browser;

    public Browser(int port) {
        browser = new WebView();
        browser.getEngine().load("http://localhost:" + port);
        getChildren().add(browser);
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(browser, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return PREF_WIDTH;
    }

    @Override
    protected double computePrefHeight(double width) {
        return PREF_HEIGHT;
    }
}
