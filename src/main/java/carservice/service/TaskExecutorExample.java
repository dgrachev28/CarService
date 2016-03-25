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
            try {
                for (int i = 0; i < 100; i++) {
                    System.out.println(message + i);
                    sleep(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private TaskExecutor taskExecutor;

    public TaskExecutorExample(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void printMessages() {
        taskExecutor.execute(new MessagePrinterTask("Messageabc "));
    }
}