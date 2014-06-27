## SVNRepoViewer

Java based svn repository viewer.

It runs on embedded Tomcat server and uses JavaFX browser to access UI.

Can be also accessed via "http://localhost:\<port\>" 

### Options:

- -p, --port \<port number\> 
- -q, --quiet - don't open JavaFX browser 

If port is not specified then random free port is used.

## Building from Source

### Prerequisites

- JDK 8
- Gradle 

### Supported OS

- Linux
- OS X

### Installation

In the directory that contains the build.gradle run

     gradle install
     
It will build, test and install application to *~/svnrepoviewer* folder.
 

### To run

Start SVNRepoViewer: 

    ~/svnrepoviewer/bin/svnrepoviewer

## License 
GNU GPLv3.
Copyright (C) 2014 Sergei Malafeev <sergeymalafeev@gmail.com>
