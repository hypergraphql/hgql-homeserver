
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

        String propertiesObjectString  = "";

        try {
            HttpResponse<String> response = Unirest.get(propertyFilepath)
                    .asString();

            propertiesObjectString = response.getBody().toString();

            System.out.println(propertiesObjectString);

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        try {
            ServerConfig config = mapper.readValue(propertiesObjectString, ServerConfig.class);

            this.port = config.port;
            this.gitpage = config.gitpage;
            this.gitdomain = config.gitdomain;
            this.services = config.services;

        } catch (IOException e) {
            e.printStackTrace();
        }






    }


}


