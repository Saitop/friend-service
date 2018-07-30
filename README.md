# SPGroup test

### Version Required:

* Java: 8
* Docker version 18.06.0-ce, build 0ffa825

-------------------------------------

### one command to run project

###### If it's first-time running, it would take a while to download images like mongo

```
./run.sh
```

to stop the services:
```
./stop.sh
```


--------------------------------------
### Build Step:

```
./gradlew build
```

-------------------------------------

### Run:
##### Get runtime parameters for configuration server according the active profile,
##### "local" would be used as default if no parameter.
##### port 8080 would be started by following command:

```
./gradlew bootRun -Dspring.profiles.active=local
```
or

```
./gradlew bootRun
```

