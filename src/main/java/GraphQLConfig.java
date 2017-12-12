import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphQLConfig {

    private Integer port;
    private String graphql;
    private String graphiql;

    @JsonCreator
    public GraphQLConfig(@JsonProperty("port") Integer port,
                         @JsonProperty("graphql") String graphql,
                         @JsonProperty("graphiql") String graphiql
    ) {
        this.port = port;
        this.graphql = graphql;
        this.graphiql = graphiql;
    }

    public Integer getPort() {
        return port;
    }
    public String getGraphql() {
        return graphql;
    }
    public String getGraphiql() {
        return graphiql;
    }
}
