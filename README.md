## SVNRepoViewer

Java based svn repository viewer.

It runs on embedded Tomcat server and uses JavaFX browser to access UI.

Can be also accessed via "http://localhost:\<port\>" 

### Options:

- -p, --port \<port number\> 
- -q, --quiet - don't open JavaFX browser 

If port is not specified then random free port is used.

### Prerequisites

- JDK 8
- Gradle (only needed if building from source)

### Supported OS

- Linux
- OS X

## To Run

Download svnrepoviewer-XX.zip

https://github.com/malafeev/SVNRepoViewer/releases

Start SVNRepoViewer

     ./bin/svnrepoviewer

## Building from Source

### Installation

In the directory that contains the build.gradle run

     gradle install
     
It will build, test and install application to `~/svnrepoviewer` folder.

### To run

Start SVNRepoViewer: 

    ~/svnrepoviewer/bin/svnrepoviewer
    
### Fat Jar

To build one jar containing all dependencies run:

    gradle build

It will create fat jar in `build/libs` folder. To start SVNRepoViewer run: `java -jar build/libs/svnrepoviewer-0.1.jar`    

## License 
GNU GPLv3.
Copyright (C) 2014 Sergei Malafeev <sergeymalafeev@gmail.com>
