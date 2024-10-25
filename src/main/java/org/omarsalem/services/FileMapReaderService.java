package org.omarsalem.services;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileMapReaderService implements MapReaderService {
    private final char[][] map;

    public FileMapReaderService(final String filePath) {
        try {
            final Path mapPath = Path.of(filePath);
            final List<String> lines = Files.readAllLines(mapPath);
            map = new char[lines.size()][];
            for (int i = 0; i < lines.size(); i++) {
                map[i] = lines.get(i).toCharArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public char[][] getMap() {
        return map;
    }
}
