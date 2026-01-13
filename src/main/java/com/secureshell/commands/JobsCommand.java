package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.util.JobManager;

import java.util.Map;

public class JobsCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        JobManager jobManager = JobManager.getInstance();
        jobManager.cleanupCompletedJobs();
        
        Map<Integer, JobManager.JobInfo> jobs = jobManager.getAllJobs();
        
        if (jobs.isEmpty()) {
            return CommandResult.success("No background jobs");
        }

        StringBuilder output = new StringBuilder();
        for (JobManager.JobInfo job : jobs.values()) {
            String status = job.isDone() ? "Done" : "Running";
            output.append("[").append(job.getJobId()).append("] ")
                  .append(status).append("  ").append(job.getCommand()).append("\n");
        }
        
        return CommandResult.success(output.toString().trim());
    }

    @Override
    public String getName() {
        return "jobs";
    }

    @Override
    public String getDescription() {
        return "List background jobs";
    }
}
