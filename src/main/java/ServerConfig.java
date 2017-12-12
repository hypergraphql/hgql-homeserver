
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerConfig {

    private Integer port;
    private String gitpage;
    private String gitdomain;
    private List<GraphQLConfig> services;


    public Integer getPort() {
        return port;
    }

    public String getGitpage() {
        return gitpage;
    }

    public String getGitdomain() {
        return gitdomain;
    }

    public List<GraphQLConfig> getServices() {
        return services;
    }

    @JsonCreator
    private ServerConfig(
            @JsonProperty("port") Integer port,
            @JsonProperty("gitpage") String gitpage,
            @JsonProperty("gitdomain") String gitdomain,
            @JsonProperty("services") List<GraphQLConfig> services
    ) {
        this.port = port;
        this.gitpage = gitpage;
        this.gitdomain = gitdomain;
        this.services = services;
    }


    public ServerConfig(String propertyFilepath) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            ServerConfig config = mapper.readValue(new File(propertyFilepath), ServerConfig.class);

            this.port = config.port;
            this.gitpage = config.gitpage;
            this.gitdomain = config.gitdomain;
            this.services = config.services;

        } catch (IOException e) {
            e.printStackTrace();
        }






    }


}


