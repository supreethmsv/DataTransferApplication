package com.example.filetransfer.Controller;

import com.example.filetransfer.Models.SqlInfo;
import com.example.filetransfer.Repository.SqlRepository;
import com.example.filetransfer.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

@Controller
public class FileController {
    @Autowired
    StorageService storageService;

    private final SqlRepository todoRepository;

    public FileController(SqlRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostMapping("/")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file, SqlInfo sqlInfo, Model model) throws Exception {
        model.addAttribute("file", file);
        model.addAttribute("sqlInfo", sqlInfo);
        ModelAndView modelAndView = new ModelAndView();
        Stream<String> streamData = storageService.getData(file);
        try {
            System.out.println(sqlInfo.isFirstRowAsHeader());
            boolean retVal = todoRepository.save(streamData, sqlInfo);
            model.addAttribute("retVal", retVal);
        } catch(Exception exception) {
            modelAndView.addObject("exception", exception);
            modelAndView.setViewName("exception");
            model.addAttribute("retVal", false);
            return modelAndView;
        }
        modelAndView.setViewName("fileUpload");
        return modelAndView;
    }

    @GetMapping("/greet")
    public String greet(Model model) throws IOException {

        /*model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));*/

        return "fileUpload";
    }
    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "fileUpload";
    }
}
