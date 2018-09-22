package util.FileParsing;

import wrappers.EntriesList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileParser {
    private final String fileName;
    private final String parseToken;
    private EntriesList rawList;


    public FileParser(String fileName, String parseToken) {
        this.parseToken = parseToken;
        this.fileName = fileName;
        this.rawList = returnRawEntries(fileName, parseToken);
    }

    private EntriesList returnRawEntries(String fileName,
                                         String parseToken) {

        EntriesList entriesList = new EntriesList();

        List<String> entries = new ArrayList<>();

        entries = returnFileEntries(fileName, entries);

        for (String rawEntry : entries) {

            addEntry(parseToken, entriesList, rawEntry);
        }

        return entriesList;
    }

    private List<String> returnFileEntries(String fileName,
                                           List<String> entries) {

        try {
            entries = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {

            new IllegalArgumentException("Error Reading File : " + fileName);
        }
        return entries;
    }

    private void addEntry(String parseToken,
                          EntriesList entriesList,
                          String rawEntry) {

        List<String> entry = Arrays.asList(rawEntry.split(parseToken));
        List<String> cleanEntry = returnCleanList(entry);

        entriesList.add(cleanEntry);
    }

    private List<String> returnCleanList(List<String> entry) {
        List<String> entryList = new ArrayList<>();

        for (String element : entry) {
            entryList.add(element.trim());
        }
        return entryList;
    }


    public List<List<String>> getRawList() {
        return rawList;
    }
}
