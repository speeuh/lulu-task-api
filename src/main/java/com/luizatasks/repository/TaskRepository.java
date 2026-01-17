package com.luizatasks.repository;

import com.luizatasks.domain.entity.Task;
import com.luizatasks.domain.entity.User;
import com.luizatasks.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAndActiveTrue(User user);
    List<Task> findByUserAndStatusAndActiveTrue(User user, TaskStatus status);
    List<Task> findByActiveTrue();
}

