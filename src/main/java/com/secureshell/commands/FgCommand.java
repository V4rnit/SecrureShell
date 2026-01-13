package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.util.JobManager;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FgCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: fg <job_id>");
        }

        try {
            int jobId = Integer.parseInt(args[0]);
            JobManager jobManager = JobManager.getInstance();
            JobManager.JobInfo job = jobManager.getJob(jobId);

            if (job == null) {
                return CommandResult.failure("Job not found: " + jobId);
            }

            Future<?> future = job.getFuture();
            if (future.isDone()) {
                return CommandResult.success("Job " + jobId + " already completed");
            }

            // Wait for job to complete
            future.get(30, TimeUnit.SECONDS);
            return CommandResult.success("Job " + jobId + " completed");
        } catch (NumberFormatException e) {
            return CommandResult.failure("Invalid job ID: " + args[0]);
        } catch (Exception e) {
            return CommandResult.failure("Error bringing job to foreground: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "fg";
    }

    @Override
    public String getDescription() {
        return "Bring background job to foreground";
    }
}
