# Akka-HTTP Hello World
Minimal example of akka-http

## Install and  run
```
$ ##### Java Install           #####
$ brew cask install java8
$ java -version
$
$ ##### Intellij-Idea Install  #####
$ brew cask install intellij-idea
$ vi ~/.bash_profile
$ # then add    alias idea='open -a "`ls -dt /Applications/IntelliJ\ IDEA*|head -1`"'
$
$ ##### sbt Install            #####
$ brew install sbt    # if not installed
$ sbt about
$ sbt tasks
$
$ ##### Tutorial Install/Run   #####
$ git clone https://github.com/allenhwkim/akka-http-helloworld.git
$ cd play-helloworld
$ sbt compile run
$ curl http://localhost:8080/pet/1
{"id":1,"name":"cat"}
$ curl -X POST -H "Content-Type: application/json" -d '{"id": 2, "name": "puppy"}' http://localhost:8080/pet
{"id":2,"name":"puppy"}
$ curl http://localhost:8080/pet/2
{"id":2,"name":"puppy"}
```
