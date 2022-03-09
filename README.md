## About

This project contains source code for dir hashing and comparing tool.
Release directory contains jar for app, for lib (depencency) and jlinked application.

## How to build

To build the project you need to move contents of every *src* file so the file tree looks like
```
.
├── pl.edu.pwr.app
│   ├── module-info.java
│   ├── pl
│   │   └── edu
│   │       └── pwr
│   │           └── app
│   │               ├── AppWindow.class
│   │               └── AppWindow.java
│   ├── pl.edu.pwr.app.iml
│   └── src
└── pl.edu.pwr.dir.hash
    ├── module-info.java
    ├── pl
    │   └── edu
    │       └── pwr
    │           └── dir_hash
    │               ├── ChecksumVisitor.java
    │               └── Snapshot.java
    ├── pl.edu.pwr.dir.hash.iml
    └── src
```
This is because `--module-source-path` requires this module structure.

Then, to compile every java class just run
```bash
cd source
javac --module pl.edu.pwr.app --module-source-path . -d outdir
```
This will build all project files and put them in outdir.

To run the project
```bash
java -p "modulepath1;modulepath2" -m pl.edu.pwr.app/pl.edu.pwr.app.AppWindow
```

If you also want to pack compiled classes to .jar files, you could run

```bash
jar -c -C pl.edu.pwr.app . >> app.jar
jar -c -C pl.edu.pwr.dir.hash . >> lib.jar
```

To list all projects dependencies just run
```bash
jdeps --module-path "C:\Users\ULTUX\IdeaProjects\lab1\out\production\pl.edu.pwr.app;C:\Users\ULTUX\IdeaProjects\lab1\out\production\pl.edu.pwr.dir.hash" --module pl.edu.pwr.app --list-deps
```

To create portable jre and link all required dependencies, run
```bash
jlink -p "C:\Users\ULTUX\IdeaProjects\lab1\out\production\pl.edu.pwr.app;C:\Users\ULTUX\IdeaProjects\lab1\out\production\pl.edu.pwr.dir.hash" --add-modules pl.edu.pwr.app,pl.edu.pwr.dir.hash,java.base,java.desktop --launcher start=pl.edu.pwr.app/pl.edu.pwr.app.AppWindow --output jlinkoutdirr
```
The `--add-modules` parameter is the list of modules used by this project. `--launcher` parameter creates runnable *start.bat* and *start* (bash) files that launch the app without the need of running it manually.
