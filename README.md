# simpleWebProgram

to install docker :
```https://docs.docker.com/get-docker/```

to create a docker image :
```./gradlew bootBuildImage --imageName=hbs/sample-web```

to run app on docker :
```docker run -p 8080:8080 -t hbs/sample-web```

Now access : ```http://localhost:8080/```