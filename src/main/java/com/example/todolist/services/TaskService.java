package com.example.todolist.services;

import com.example.todolist.models.Person;
import com.example.todolist.models.Task;
import com.example.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository1) {
        this.taskRepository = taskRepository1;
    }

    public List<Task> findAllByPersonId(int id) {
        List<Task> list = taskRepository.findAllByPersonId(id);
        return list;
      //  return null;
    }

    public Task findOne(int id) {
        Optional<Task> foundPerson = taskRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Task person) {
        taskRepository.save(person);
    }

    @Transactional
    public void rename(int id, Task task, Person person) {
        task.setId(id);
        task.setPerson(person);
        taskRepository.save(task);
    }

    @Transactional
    public void delete(int id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public void changeIsDone(int id, Task task, Person person){
        task.setId(id);
        task.setDone(!task.isDone());
        task.setPerson(person);
        taskRepository.save(task);
    }
}
