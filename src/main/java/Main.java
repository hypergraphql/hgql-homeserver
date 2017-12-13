public class Main {

    public static void main(String[] args) {

        ServerConfig config = new ServerConfig("https://semantic-integration.github.io/hypergraphql/sources/homepage-server.json");

        System.out.println("HGQL homepage server initiated at: http://localhost:" + config.getPort());

        Controller.start(config);
    }
}
