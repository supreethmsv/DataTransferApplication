package com.example.filetransfer;

import com.example.filetransfer.Service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication
public class FileTransferApplication implements CommandLineRunner {
    @Resource
    StorageService storageService;
    public static void main(String[] args) {
        SpringApplication.run(FileTransferApplication.class, args);
    }

    @Override
   public void run(String ... args) throws Exception {
        storageService.init();
    }
}
