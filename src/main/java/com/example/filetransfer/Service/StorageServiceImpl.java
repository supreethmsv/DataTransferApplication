package com.example.filetransfer.Service;

import org.springframework.core.io.Resource;
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
    public void init() throws IOException {
        if(!Files.exists(directory))
        Files.createDirectory(directory);
    }

    @Override
    public void save(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        System.out.println(file.getName());
        Path targetDirectory = directory.resolve(file.getName());


        if(!Files.exists(targetDirectory)) Files.copy(inputStream, targetDirectory);
        System.out.println(targetDirectory.resolveSibling(file.getOriginalFilename()).toString());
        if(!Files.exists(targetDirectory)) Files.move(targetDirectory, targetDirectory.resolveSibling(file.getOriginalFilename()));
        Stream<String> streamData = Files.lines(targetDirectory.resolveSibling(file.getOriginalFilename()));

        streamData.forEach(System.out::println);
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String file) {
        return null;
    }

    @Override
    public Resource loadResource(String file) {
        return null;
    }

    @Override
    public Stream<String> getData(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        System.out.println(file.getOriginalFilename());
        Path targetDirectory = directory.resolve(file.getOriginalFilename());


        if(!Files.exists(targetDirectory)) Files.copy(inputStream, targetDirectory);
        System.out.println(targetDirectory.resolveSibling(file.getOriginalFilename()).toString());
        if(!Files.exists(targetDirectory)) Files.move(targetDirectory, targetDirectory.resolveSibling(file.getOriginalFilename()));
        Stream<String> streamData = Files.lines(targetDirectory.resolveSibling(file.getOriginalFilename()));
        //streamData.forEach(System.out::println);
        return streamData;
    }
}
