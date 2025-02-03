# deliveroo-cron-parser

### Main Class : `Technical Task - Cron Expression Parser`

### Setup
1. gradle version : 8.10
2. JVM version (used in this) : 23.0.2 (Homebrew 23.0.2) it would be compatible with (java 8 and later versions)

### How to run it
- build the jar of this programme. Use below command to create the jar.<br>
  `./gradlew clean build`
- It will create the jar in `build/libs/` directory by the name `Parser-1.0-SNAPSHOT.jar`
- To run the the jar use the below command<br>
  `java -cp build/libs/Parser-1.0-SNAPSHOT.jar org.example.Main <cron-string>`
- Alternatively this can be run as
  `./gradlew run --args="<cron-string>"`


### Sample command and corresponding output
#### Command
`java -cp build/libs/Parser-1.0-SNAPSHOT.jar org.example.Main "*/15 0 1,15 * 1-5 /usr/bin/find"`
#### Alternate Command
`./gradlew run --args="'*/15 0 1,15 * 1-5 /usr/bin/find'"`
#### Output
```
minute          0 15 30 45
hour            0
day of month    1 15
month           1 2 3 4 5 6 7 8 9 10 11 12
day of week     1 2 3 4 5
command         /usr/bin/find
```