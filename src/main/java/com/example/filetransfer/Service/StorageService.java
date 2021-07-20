package com.example.filetransfer.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.stream.Stream;

public interface StorageService {
    void init() throws IOException;
    Stream<String> getData(MultipartFile file) throws IOException;
}
