package com.luizatasks.repository;

import com.luizatasks.domain.entity.TaskLog;
import com.luizatasks.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
    List<TaskLog> findByUserOrderByCompletedAtDesc(User user);
}

