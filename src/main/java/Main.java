public class Main {

    public static void main(String[] args) {

        ServerConfig config = new ServerConfig("properties.json");

        System.out.println("HGQL homepage server initiated at: http://localhost:" + config.getPort());

        Controller.start(config);
    }
}
