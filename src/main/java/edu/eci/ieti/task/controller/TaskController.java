package edu.eci.ieti.task.controller;

import edu.eci.ieti.task.data.Task;
import edu.eci.ieti.task.dto.TaskDto;
import edu.eci.ieti.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 *
 * @author BJM19
 */
@RestController
@RequestMapping( "/v1/task" )
public class TaskController {

    private final TaskService taskService;

    public TaskController(@Autowired TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> all(){
        try{
            return ResponseEntity.ok().body(taskService.all());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<Task> findById( @PathVariable String id )
    {

        try{
            return ResponseEntity.ok().body(taskService.findById(id));
        }catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<Task> create( @RequestBody TaskDto taskDto )
    {
        try{
            Random rand = new Random(); //instance of random class
            int upperbound = 100;
            //generate random values from 0-100
            int int_random = rand.nextInt(upperbound);
            String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                    +"lmnopqrstuvwxyz!@#$%&";
            Random rnd = new Random();
            StringBuilder sb = new StringBuilder(int_random);
            for (int i = 0; i < 5; i++)
                sb.append(chars.charAt(rnd.nextInt(chars.length())));

            Task task = new Task(sb.toString(),taskDto.getName(),taskDto.getDescription(),
                    taskDto.getStatus(), taskDto.getAssignedTo(), taskDto.getDueDate(), taskDto.isCreated());
            return ResponseEntity.ok().body(taskService.create(task));
        }catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping( "/{id}" )
    public ResponseEntity<Task> update( @RequestBody TaskDto taskDto, @PathVariable String id )
    {
        try{
            Task task = taskService.findById(id);
            if( task != null){
                task.setName(taskDto.getName());
                task.setDescription(taskDto.getDescription());
                task.setStatus(taskDto.getStatus());
                task.setAssignedTo(taskDto.getAssignedTo());
                task.setDueDate(taskDto.getAssignedTo());
                task.setCreated(taskDto.isCreated());
                return ResponseEntity.ok().body(taskService.update(task, id));
            }
            return ResponseEntity.status(404).build();
        }catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<Boolean> delete( @PathVariable String id )
    {
        try{
            taskService.deleteById(id);
            boolean delete = taskService.findById(id) == null ? true : false;
            return ResponseEntity.ok().body(delete);
        }catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }
}
