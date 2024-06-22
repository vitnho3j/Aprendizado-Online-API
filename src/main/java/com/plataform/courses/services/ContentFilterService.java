package com.plataform.courses.services;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContentFilterService {
    private Set<String> badWords;

    public ContentFilterService() {
        badWords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("badwords.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(line.toLowerCase());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean containsBadWord(List<String> fields) {
        for (String field : fields) {
            for (String bad_word : badWords) {
                if (field.contains(bad_word)){
                    System.out.println("A field: " + field + " deu positivo para a bad word: " + bad_word);
                    return true;
                }
            }
        }
        return false;
    }
}