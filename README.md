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
diary/
├── pom.xml                                # Maven build descriptor
├── data/                                  # App data files (runtime)
│   ├── users.json
│   └── diary.json
├── src/
│   ├── main/
│   │   ├── java/                          # Application source code
│   │   │   └── com/
│   │   │       └── diary/
│   │   │           ├── Main.java
│   │   │           ├── manager/
│   │   │           │   └── DiaryManager.java
│   │   │           ├── util/
│   │   │           │   ├── EncryptionUtil.java
│   │   │           │   └── DiaryRead.java
│   │   │           └── model/
│   │   │               ├── DiaryEntry.java
│   │   │               └── User.java
│   │   └── resources/                     # Non-code app resources
│   │       └── log4j2.xml                 # Example (logging config)
│   └── test/                              # Unit & integration tests
│       ├── java/
│       │   └── com/
│       │       └── diary/
│       │           ├── UserTest.java
│       │           ├── DiaryEntryTest.java
│       │           └── EncryptionUtilTest.java
│       └── resources/
│           └── test-data.json  
└── target/                                
    ├── classes/                           
    ├── test-classes/                      
    ├── diary-1.0-SNAPSHOT.jar             
    └── surefire-reports/   

## Link to repository
https://github.com/NTNU-IDI/mappe-2025-BrageSS

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
