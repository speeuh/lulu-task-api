package com.luizatasks.service;

import com.luizatasks.domain.entity.Task;
import com.luizatasks.domain.entity.TaskLog;
import com.luizatasks.domain.entity.User;
import com.luizatasks.domain.enums.TaskRecurrence;
import com.luizatasks.domain.enums.TaskStatus;
import com.luizatasks.dto.request.TaskRequest;
import com.luizatasks.dto.response.TaskResponse;
import com.luizatasks.mapper.TaskMapper;
import com.luizatasks.repository.TaskLogRepository;
import com.luizatasks.repository.TaskRepository;
import com.luizatasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskLogRepository taskLogRepository;
    private final TaskMapper taskMapper;
    
    @Transactional
    public List<TaskResponse> getUserTasks(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Task> tasks = taskRepository.findByUserAndActiveTrue(user);
        
        // Reset recurring tasks if needed
        for (Task task : tasks) {
            if (shouldResetTask(task)) {
                task.setStatus(TaskStatus.PENDING);
                task.setCompletedAt(null);
                taskRepository.save(task);
            }
        }
        
        // Ordena as tarefas: concluídas por completedAt DESC, pendentes por ID DESC
        return tasks.stream()
                .sorted((a, b) -> {
                    // Tarefas concluídas ordenadas por data de conclusão (mais recente primeiro)
                    if (a.getStatus() == TaskStatus.COMPLETED && b.getStatus() == TaskStatus.COMPLETED) {
                        if (a.getCompletedAt() == null && b.getCompletedAt() == null) return 0;
                        if (a.getCompletedAt() == null) return 1;
                        if (b.getCompletedAt() == null) return -1;
                        return b.getCompletedAt().compareTo(a.getCompletedAt());
                    }
                    // Tarefas pendentes ordenadas por ID (mais novo primeiro)
                    if (a.getStatus() == TaskStatus.PENDING && b.getStatus() == TaskStatus.PENDING) {
                        return b.getId().compareTo(a.getId());
                    }
                    // Pendentes antes de concluídas
                    return a.getStatus().compareTo(b.getStatus());
                })
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponse> getPendingTasks(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return taskRepository.findByUserAndStatusAndActiveTrue(user, TaskStatus.PENDING).stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TaskResponse completeTask(Authentication authentication, Long taskId) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This task does not belong to the current user");
        }
        
        // Check if task should be reset first
        if (shouldResetTask(task)) {
            task.setStatus(TaskStatus.PENDING);
            task.setCompletedAt(null);
        }
        
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new RuntimeException("Task is already completed for this period");
        }
        
        // Update task status
        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        
        task = taskRepository.save(task);
        
        // Update user points
        user.setPoints(user.getPoints() + task.getPoints());
        userRepository.save(user);
        
        // Create immutable log
        TaskLog log = TaskLog.builder()
                .task(task)
                .user(user)
                .taskTitle(task.getTitle())
                .pointsEarned(task.getPoints())
                .build();
        taskLogRepository.save(log);
        
        return taskMapper.toResponse(task);
    }
    
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .points(request.getPoints())
                .status(TaskStatus.PENDING)
                .recurrence(request.getRecurrence() != null ? request.getRecurrence() : com.luizatasks.domain.enums.TaskRecurrence.NONE)
                .user(user)
                .active(true)
                .build();
        
        task = taskRepository.save(task);
        return taskMapper.toResponse(task);
    }
    
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPoints(request.getPoints());
        task.setRecurrence(request.getRecurrence() != null ? request.getRecurrence() : com.luizatasks.domain.enums.TaskRecurrence.NONE);
        
        task = taskRepository.save(task);
        return taskMapper.toResponse(task);
    }
    
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setActive(false);
        taskRepository.save(task);
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllActiveTasks() {
        return taskRepository.findByActiveTrue().stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Checks if a recurring task should be reset based on its recurrence type
     * @param task The task to check
     * @return true if the task should be reset, false otherwise
     */
    private boolean shouldResetTask(Task task) {
        // Only reset if task is completed and has recurrence
        if (task.getStatus() != TaskStatus.COMPLETED || 
            task.getRecurrence() == TaskRecurrence.NONE ||
            task.getCompletedAt() == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime completedAt = task.getCompletedAt();
        
        switch (task.getRecurrence()) {
            case DAILY:
                // Reset if it's a new day (after midnight)
                return !completedAt.toLocalDate().equals(now.toLocalDate());
                
            case WEEKLY:
                // Reset if it's a new week (Monday is the first day)
                LocalDate completedDate = completedAt.toLocalDate();
                LocalDate currentDate = now.toLocalDate();
                LocalDate startOfCompletedWeek = completedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate startOfCurrentWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                return !startOfCompletedWeek.equals(startOfCurrentWeek);
                
            case MONTHLY:
                // Reset if it's a new month
                return completedAt.getMonth() != now.getMonth() || 
                       completedAt.getYear() != now.getYear();
                
            default:
                return false;
        }
    }
}

