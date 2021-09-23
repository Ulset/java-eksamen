package no.kristiania;

import java.util.Random;

public class RandomThings {
    private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Random random = new Random();

    public String getRandomName(int length) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            out.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return out.toString();
    }

    public String getRandomEmail() {
        return getRandomName(6) + "@" + getRandomName(5) + ".com";
    }

    public String getRandomStatus() {
        String[] options = {"Påbegynt", "Ferdig", "Ikke klar", "Vanskelig"};
        int randomIndex = random.nextInt(options.length);
        return options[randomIndex];
    }
}
