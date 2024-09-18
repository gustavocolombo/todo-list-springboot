package br.com.gustavocolombo.todolist.task.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gustavocolombo.todolist.task.model.TaskModel;
import br.com.gustavocolombo.todolist.task.repository.ITaskRepository;
import br.com.gustavocolombo.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var userIdFromRequest = ((UUID) request.getAttribute("userId"));
    taskModel.setUserId(userIdFromRequest);

    var currentDateTime = LocalDateTime.now();

    if (currentDateTime.isAfter(taskModel.getStartedAt()) || currentDateTime.isAfter(taskModel.getEndedAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início/fim deve ser maior que a atual");
    }

    if (taskModel.getStartedAt().isAfter(taskModel.getEndedAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser maior que a data de término");
    }

    var createdTask = this.taskRepository.save(taskModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
  }

  @GetMapping("/")
  public List<TaskModel> getTasks(HttpServletRequest request) {
    var userIdFromRequest = ((UUID) request.getAttribute("userId"));
    var tasks = this.taskRepository.findByUserId(userIdFromRequest);

    return tasks;
  }

  @PutMapping("/{id}")
  public ResponseEntity updateTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
    var task = taskRepository.findById(id).orElse(null);
    var userIdFromRequest = request.getAttribute("userId");

    if (!task.getUserId().equals(userIdFromRequest)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Para atualizar a tarefa precisa ser proprietário dela");
    }

    Utils.copyNonNullProperties(taskModel, task);

    var taskUpdated = taskRepository.save(task);

    return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
  }
} 
