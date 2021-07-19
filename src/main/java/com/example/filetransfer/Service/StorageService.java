package com.example.filetransfer.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init() throws IOException;
    void save(MultipartFile file) throws IOException;
    Stream<Path> loadAll();
    Path load(String file);
    Resource loadResource(String file);
    Stream<String> getData(MultipartFile file) throws IOException;
}
