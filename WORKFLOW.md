# Harmony workflow

This file contains explainations about the workflow and organisation of this repository.

## Branch convention

This repository will have two branches: master and development.
  - The master branch will always be in a stable state. This will be the production area
  - The development branch will be where we will be coding. There is no guarantee that feature and code will work in this branch.

Once a version is considered stable in the development branch, it will be merged into the master branch and tagged according to tag convention rules.

## Commit convention

Each commit must be prefixed with the part of the project involved. For instance, a commit for the client app will be:

`[CLIENT]: Added logout buton`

Other examples:

```
[SERVER]: Fixed uncatched exception in the encrypt method
[PROTOCOL]: Updated handshake keys procedure
[DOCUMENTATION]: Added description on user model
```

## Tag convention

Tags will represent the version of Harmony client and server on the master branch. We will use the semantic versioning to keep things as simple as possible.

> https://semver.org/

## Building the project

Inside the script folder, a script called `build.sh` will automatically compile all source file from both client and server and output them in a build folder.

## Dependencies

Every dependencies used in this project are precompiled in the libraries folder

```
Name:    <lib name>
Version: <version>
Author:  <author>
Link:    <link>
Sha256:  <hash>
---
Name:    <lib name>
Version: <version>
Author:  <author> 
Link:    <link>  
Sha256:  <hash>
---
...
```

## Project Architecture

```
├── assets              # Contains assets used in README file
├── client-source       # Contains client source files
├── documentation       # Contains implementation details and explainations
├── libraries           # Contains every external libraries
├── LICENSE.md          # MIT license
├── README.md           # 
├── scripts             # Contains useful scripts
├── server-source       # Contains server source files
└── WORKFLOW.md         # Explains how the project is structured
```
