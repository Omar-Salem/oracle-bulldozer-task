package org.omarsalem.services;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileMapReaderServiceTest {

    @Test
    void map_file_read_and_parsed_from_disk() {
        //Arrange
        final String smallMapPath = this.getClass().getResource("/small_map.txt").getFile();
        final FileMapReaderService target = new FileMapReaderService(smallMapPath);

        //Act
        final char[][] map = target.getMap();

        //Assert
        final char[][] expected = {
                {'x', 'y'},
                {'z', 'w'}
        };
        assertTrue(Arrays.deepEquals(expected, map));
    }
}