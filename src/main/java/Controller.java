import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Controller {

    public static void start(ServerConfig config) {

        port(config.getPort());

        for (GraphQLConfig hgql : config.getServices()) {

            post("/service/" + hgql.getGraphql() , (req, res) -> {

                String accept = req.headers("accept");

                if (!accept.contains("application/json")) {
                    res.status(415);
                    return("This demo service does not support the requested media type.");
                }

                try {
                    HttpResponse<InputStream> response = Unirest.post("http://localhost:" + hgql.getPort() + "/graphql")
                            .header("Accept", "application/json")
                            .body(req.bodyAsBytes())
                            .asBinary();

                    res.type("application/json");

                    res.status(response.getStatus());

                    return(response.getBody());

                } catch (Exception e) {
                    String emptyHGQLreponse = "This demo service appears to be down at the moment. Sorry!";
                    return emptyHGQLreponse;
                }

            });

            get("/service/" + hgql.getGraphql(), (req, res) -> {

                String accept = req.headers("accept");

                if (!accept.contains("application/turtle") && !accept.contains("text/html")) {
                    res.status(415);
                    return("This demo service does not support the requested media type.");
                }

                HttpResponse<InputStream> response = Unirest.get("http://localhost:" + hgql.getPort() + "/graphql")
                        .header("Accept", "application/turtle")
                        .asBinary();

                res.type("text/plain");

                res.status(200);

                return(response.getBody());
            });

            get("/service/" + hgql.getGraphiql(), (req, res) -> {

                Map<String, String> model = new HashMap<>();

                model.put("template", String.valueOf("/service/" + hgql.getGraphql()));

                return new VelocityTemplateEngine().render(
                        new ModelAndView(model, "graphiql.vtl")
                );
            });

        }

        get("/hypergraphql/*", (req, res) -> {

            String content = req.headers("accept");

            HttpResponse<InputStream> response = Unirest.get(config.getGitdomain() + req.pathInfo())
                    .header("Accept", content)
                    .asBinary();

            res.type(content);

            res.status(response.getStatus());

            return(response.getRawBody());

        });

        get("*", (req, res) -> {

            String accept = req.headers("accept");

            HttpResponse<String> response = Unirest.get(config.getGitpage() + req.pathInfo())
                    .header("Accept", accept)
                    .asString();

            res.type("text/html");

            res.status(response.getStatus());

            return(response.getBody());

        });

    }
}
