package br.com.gustavocolombo.todolist.task.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
  
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private UUID userId;
  private String description;

  @Column(nullable = true)
  private boolean done;

  @Column(length = 50)
  private String title;

  private LocalDateTime startedAt;
  private LocalDateTime endedAt;
  private String priority;
  
  @CreationTimestamp
  private LocalDateTime createdAt;

  public void setTitle (String title) throws Exception {
    if (title.length() > 50) {
      throw new Exception("O campo title pode ter até 50 caracteres");
    }
    this.title = title;
  }
}
