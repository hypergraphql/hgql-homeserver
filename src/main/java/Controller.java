import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

            post("/hypergraphql/service/" + hgql.getGraphql() , (req, res) -> {

                String content = req.headers("accept");

                try {
                    HttpResponse<InputStream> response = Unirest.post("http://localhost:" + hgql.getPort() + "/" + hgql.getGraphql())
                            .header("Accept", content)
                            .body(req.bodyAsBytes())
                            .asBinary();

                    res.type(content);

                    res.status(200);

                    return(response.getBody());

                } catch (Exception e) {
                    String emptyHGQLreponse = "This demo service appears to be down at the moment. Sorry!";
                    return emptyHGQLreponse;
                }

            });

            get("/hypergraphql/service/" + hgql.getGraphql(), (req, res) -> {

                String content = req.headers("accept");

                HttpResponse<InputStream> response = Unirest.get("http://localhost:" + hgql.getPort() + "/" + hgql.getGraphql())
                        .header("Accept", "application/turtle")
                        .asBinary();

                res.type(content);

                res.status(200);

                return(response.getBody());
            });

        }

        for (GraphQLConfig hgql : config.getServices()) {

            get("/hypergraphql/service/" + hgql.getGraphiql(), (req, res) -> {

                Map<String, String> model = new HashMap<>();

                model.put("template", String.valueOf("/hypergraphql/service/" + hgql.getGraphql()));

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

            res.status(200);

            return(response.getBody());
        });

    }
}
