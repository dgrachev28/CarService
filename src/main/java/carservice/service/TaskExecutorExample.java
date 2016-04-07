package carservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class TaskExecutorExample {

    @Autowired
    private TicketGenerator ticketGenerator;

    private TaskExecutor taskExecutor;

    public TaskExecutorExample(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void startGeneratingTickets() {
        taskExecutor.execute(ticketGenerator);
    }
}