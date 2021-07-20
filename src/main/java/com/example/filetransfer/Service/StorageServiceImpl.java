package com.example.filetransfer.Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path directory = Paths.get("uploadFolder");
    @Override
    /* Create the file in the directory
     */
    public void init() throws IOException {
        if(!Files.exists(directory))
        Files.createDirectory(directory);
    }

    @Override
    /* Gets the data from the multipart file
     * Returns - Stream data
     * throws - IOException
     */
    public Stream<String> getData(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Path targetDirectory = directory.resolve(file.getOriginalFilename());
        if(!Files.exists(targetDirectory)) Files.copy(inputStream, targetDirectory);
        if(!Files.exists(targetDirectory)) Files.move(targetDirectory, targetDirectory.resolveSibling(file.getOriginalFilename()));
        Stream<String> streamData = Files.lines(targetDirectory.resolveSibling(file.getOriginalFilename()));
        return streamData;
    }
}
