""""
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/NzRaDbQp)
# Portfolio project IDATT1003
This file uses Mark Down syntax. For more information see [here](https://www.markdownguide.org/basic-syntax/).
STUDENT NAME = "Brage Sebastian Sandli-Løvgren"  
STUDENT ID = "157280"

## Project description
A Diary, where multiple authors can log inn, write a diary, and post it. 
every one can see eachothers posts, time posted, location, mood and other descriptors. 
but the content is avaliable only to the author.
All stored diaries have their main content encrypted upon storing, and decrypted when being read.

## Project structure
I am using MAVEN
```
diary/
├── pom.xml
├── LICENSE.txt
├── README.md
├── data/
│   ├── diary.json
│   ├── location.json
│   ├── mood.json
│   └── user.json
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── diary/
│   │               ├── Main.java
│   │               ├── manager/
│   │               │   ├── DiaryManager.java
│   │               │   ├── EntryManager.java
│   │               │   └── UserManager.java
│   │               ├── model/
│   │               │   ├── DiaryEntry.java
│   │               │   ├── Locations.java
│   │               │   ├── Moods.java
│   │               │   └── User.java
│   │               └── util/
│   │                   ├── DiaryRead.java
│   │                   ├── EncryptionUtil.java
│   │                   └── Interfaces.java
│   └── tests/
│       └── UnitTests.java
├── test/
└── target/
    ├── classes/
    ├── test-classes/
    ├── surefire-reports/
    └── (build artifacts)  
```

## Link to repository
https://github.com/NTNU-IDI/mappe-2025-BrageSSL

## How to run the project
You need:
Maven installed
Java  25 installed

Open the console commands and navigate to teh diary root folder
write the commands: 
mvn clean compile
mvn exec:java. 

the last one should build and run the programm

## How to run the tests


## References
[//]: # (TODO: Include references here, if any. For example, if you have used code from the course book, include a reference to the chapter.
Or if you have used code from a website or other source, include a link to the source.)
"""
