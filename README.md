# SPGroup test

### Version Required:

* Java: 8

-------------------------------------

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

