public class Main {

    public static void main(String[] args) {

        ServerConfig config = new ServerConfig("properties.json");

        System.out.println("HGQL homepage server initiated on port: " + config.getPort());

        Controller.start(config);
    }
}
