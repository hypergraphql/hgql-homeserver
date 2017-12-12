import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.io.InputStream;

import static spark.Spark.*;

public class Controller {

    public static void start(ServerConfig config) {

        port(config.getPort());

        get("/", (req, res) -> {

            String content = req.headers("accept");

            HttpResponse<String> response = Unirest.get(config.getGitpage())
                    .header("Accept", content)
                    .asString();

            res.type(content);

            res.status(200);

            return(response.getBody());
        });

        get("/hypergraphql", (req, res) -> {

            res.redirect("/");

            return null;
        });

        for (GraphQLConfig hgql : config.getServices()) {

            post("/hypergraphql/demo/" + hgql.getGraphql() , (req, res) -> {

                System.out.println("GraphQL call at: " + req.pathInfo());

                String content = req.headers("accept");

                HttpResponse<InputStream> response = Unirest.post("http://localhost:" + hgql.getPort() + "/graphql")
                        .header("Accept", content)
                        .body(req.bodyAsBytes())
                        .asBinary();

                res.type(content);

                res.status(200);

                return(response.getBody());
            });
        }

        get("/hypergraphql/*", (req, res) -> {

            String content = req.headers("accept");

            HttpResponse<InputStream> response = Unirest.get(config.getGitdomain() + req.pathInfo())
                    .header("Accept", content)
                    .asBinary();

            res.type(content);

            res.status(200);

            return(response.getBody());
        });

    }
}
