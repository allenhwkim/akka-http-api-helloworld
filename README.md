# Akka-HTTP Hello World
Minimal example of akka-http

## Install and  run
```
$ ##### Tutorial Install/Run   #####
$ git clone https://github.com/allenhwkim/akka-http-helloworld.git
$ cd akka-http-helloworld
$ sbt compile run
$ curl http://localhost:8080/pet/1
{"id":1,"name":"cat"}
$ curl -X POST -H "Content-Type: application/json" -d '{"id": 2, "name": "puppy"}' http://localhost:8080/pet
{"id":2,"name":"puppy"}
$ curl http://localhost:8080/pet/2
{"id":2,"name":"puppy"}
```
