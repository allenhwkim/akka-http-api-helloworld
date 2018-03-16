import akka.*;
import akka.actor.*;
import akka.http.javadsl.*;
import akka.http.javadsl.marshallers.jackson.*;
import akka.http.javadsl.model.*;
import akka.http.javadsl.server.*;
import akka.stream.*;
import akka.stream.javadsl.*;
import com.fasterxml.jackson.annotation.*;
import java.util.*;
import java.util.concurrent.*;
// import static akka.http.javadsl.server.PathMatchers.longSegment;

public class HelloWorld extends AllDirectives {

    public static void main(String[] args) throws Exception {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create("routes");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        //In order to access all directives we need an instance where the routes are define.
        HelloWorld app = new HelloWorld();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
            .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
            .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    private Route createRoute() {

        return route(
           get(() ->
              path("", () -> complete("<h1>Hello World From Akka-Http-Json</h1>")),
              path("json", () -> {
                  final CompletionStage<Optional<Item>> futureMaybeItem = fetchItem("World");
                  return onSuccess(futureMaybeItem, maybeItem ->
                      maybeItem.map(item -> completeOK(item, Jackson.marshaller()))
                  );
              })
           )
        );
    }

    private static class Item {
        final String id;
        final String name;

        @JsonCreator
        Item(@JsonProperty("id") String id, @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }
        public String getName() { return name; }
        public String getId() { return id; }
    }

    // (fake) async database query api
    private CompletionStage<Optional<Item>> fetchItem(String name) {
        return CompletableFuture.completedFuture(Optional.of(new Item("Hello", name)));
    }

}
