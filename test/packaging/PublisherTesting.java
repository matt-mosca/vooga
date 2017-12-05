package packaging;

public class PublisherTesting {

    public static void main(String[] args) {
        try {
            Publisher publisher = new Publisher("VOOGA");
            String fileLink = publisher.uploadFile("application/zip",
                    "data/games/jar-package-testing.jar");
            System.out.println("Share your game with this link: " + fileLink);
        } catch (Exception e) {
            // No good
            e.printStackTrace();
        }
    }
}
