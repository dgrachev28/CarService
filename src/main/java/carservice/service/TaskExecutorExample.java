package carservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class TaskExecutorExample {

    @Autowired
    public TicketGenerator ticketGenerator;

    private class MessagePrinterTask extends Thread {

        private String message;

        public MessagePrinterTask(String message) {
            this.message = message;
        }

        public void run() {
            ticketGenerator.run();

        }

    }

    private TaskExecutor taskExecutor;

    public TaskExecutorExample(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void printMessages() {
        taskExecutor.execute(ticketGenerator);
    }
}