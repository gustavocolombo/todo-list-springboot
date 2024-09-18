package br.com.gustavocolombo.todolist.task.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gustavocolombo.todolist.task.model.TaskModel;
import java.util.List;


public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{
  List<TaskModel> findByUserId(UUID userId);
}
