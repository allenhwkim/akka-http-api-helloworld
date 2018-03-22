import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static akka.http.javadsl.server.Directives.*;
import static akka.http.javadsl.unmarshalling.StringUnmarshallers.INTEGER;

public class HelloWorld {

    public static Route appRoute() {

        Map<Integer, Pet> pets = new ConcurrentHashMap<>();
        pets.put(0, new Pet(0, "dog"));
        pets.put(1, new Pet(1, "cat"));

        return
            route(
                path("", () -> complete("Hello Pets World, visit /pet/1")),
                pathPrefix("pet", () ->
                    path(INTEGER, petId -> route(
                        get(() -> {
                            Pet pet = pets.get(petId);
                            return (pet == null) ? reject() : complete(StatusCodes.OK, pet, Jackson.<Pet>marshaller());
                        })
                    ))
                ),
                path("pet", () ->
                    post(() ->
                        entity(Jackson.unmarshaller(Pet.class), thePet -> {
                                pets.put(thePet.getId(), thePet);
                                return complete(StatusCodes.OK, thePet, Jackson.<Pet>marshaller());
                            }
                        )
                    )
                )
            );
    }

    public static void main(String[] args) throws IOException {
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final ConnectHttp host = ConnectHttp.toHost("127.0.0.1", 8080);

        Http.get(system).bindAndHandle(appRoute().flow(system, materializer), host, materializer);

        System.console().readLine("Visit http://localhost:8080/pet/1, Type RETURN to exit...");
        system.terminate();
    }
}