package com.example.filetransfer.Controller;
import com.example.filetransfer.Models.Todo;
import com.example.filetransfer.Repository.SqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ToDoController {

    private final SqlRepository todoRepository;

    public ToDoController(SqlRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    @PostMapping("/yes")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTodo(@RequestBody Todo todo) {

        //todoRepository.save(todo);
        return "fileUpload";
        //return todoRepository.save(todo);
       // System.out.println("upload done");
        //return "fileUpload1";
    }

    //@GetMapping("/no")
    //public Iterable<Todo> getTodos() {
       // return todoRepository.findAll();
    //}
}
