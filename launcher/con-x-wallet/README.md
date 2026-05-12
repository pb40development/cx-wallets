## con-x-wallet

This project is relying on a git submodule for the super-user-seed extension. In case the `extensions/super-user-seed-extension` folder is empty on your local system, please run 

```shell
git submodule update --init --recursive
```

In order to create a local docker image, please run (from the project root folder): 

```shell
./gradlew :launcher:con-x-wallet:dockerize
```