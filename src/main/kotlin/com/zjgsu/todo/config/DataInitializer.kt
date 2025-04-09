package com.zjgsu.todo.config

import com.zjgsu.todo.model.*
import com.zjgsu.todo.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.OffsetDateTime

@Configuration
@Profile("dev")
class DataInitializer {
    
    @Bean
    fun initDatabase(
        todoRepository: TodoRepository,
        categoryRepository: CategoryRepository,
        tagRepository: TagRepository,
        memoRepository: MemoRepository,
        reminderRepository: ReminderRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            // Create categories
            val workCategory = categoryRepository.save(Category(name = "Work", color = "#FF5733"))
            val personalCategory = categoryRepository.save(Category(name = "Personal", color = "#33FF57"))
            val studyCategory = categoryRepository.save(Category(name = "Study", color = "#3357FF"))
            
            // Create tags
            val urgentTag = tagRepository.save(Tag(name = "Urgent"))
            val importantTag = tagRepository.save(Tag(name = "Important"))
            val routineTag = tagRepository.save(Tag(name = "Routine"))
            
            // Create memos
            val projectMemo = memoRepository.save(
                Memo(
                    content = "Project details and specifications for the new API.",
                    attachments = mutableListOf("https://example.com/spec.pdf")
                )
            )
            
            val meetingMemo = memoRepository.save(
                Memo(content = "Meeting notes from the team discussion.")
            )
            
            // Create todos
            val projectTodo = Todo(
                title = "Complete API project",
                description = "Implement the new RESTful API for the todo application",
                status = TodoStatus.IN_PROGRESS,
                dueDate = OffsetDateTime.now().plusDays(5),
                categoryId = workCategory.id,
                memoId = projectMemo.id
            )
            projectTodo.tags.add(importantTag)
            projectTodo.tags.add(urgentTag)
            todoRepository.save(projectTodo)
            
            val meetingTodo = Todo(
                title = "Team meeting",
                description = "Discuss project progress with the team",
                status = TodoStatus.PENDING,
                dueDate = OffsetDateTime.now().plusDays(1),
                categoryId = workCategory.id,
                memoId = meetingMemo.id
            )
            meetingTodo.tags.add(routineTag)
            todoRepository.save(meetingTodo)
            
            val gymTodo = Todo(
                title = "Go to the gym",
                description = "Workout session at the fitness center",
                status = TodoStatus.PENDING,
                dueDate = OffsetDateTime.now().plusHours(6),
                categoryId = personalCategory.id
            )
            todoRepository.save(gymTodo)
            
            val studyTodo = Todo(
                title = "Study Kotlin",
                description = "Learn about coroutines and flows",
                status = TodoStatus.PENDING,
                dueDate = OffsetDateTime.now().plusDays(3),
                categoryId = studyCategory.id
            )
            studyTodo.tags.add(importantTag)
            todoRepository.save(studyTodo)
            
            // Create reminders
            reminderRepository.save(
                Reminder(
                    todoId = projectTodo.id!!,
                    time = OffsetDateTime.now().plusDays(4),
                    notifyMethod = NotifyMethod.EMAIL
                )
            )
            
            reminderRepository.save(
                Reminder(
                    todoId = meetingTodo.id!!,
                    time = OffsetDateTime.now().plusHours(23),
                    notifyMethod = NotifyMethod.PUSH
                )
            )
            
            reminderRepository.save(
                Reminder(
                    todoId = gymTodo.id!!,
                    time = OffsetDateTime.now().plusHours(5),
                    notifyMethod = NotifyMethod.SMS
                )
            )
        }
    }
} 