package com.secureshell.util;

import com.secureshell.command.CommandResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages background jobs for the shell.
 */
public class JobManager {
    private static JobManager instance;
    private final Map<Integer, JobInfo> jobs;
    private final AtomicInteger jobCounter;

    private JobManager() {
        this.jobs = new ConcurrentHashMap<>();
        this.jobCounter = new AtomicInteger(1);
    }

    public static JobManager getInstance() {
        if (instance == null) {
            instance = new JobManager();
        }
        return instance;
    }

    public int addJob(String command, Future<CommandResult> future) {
        int jobId = jobCounter.getAndIncrement();
        jobs.put(jobId, new JobInfo(jobId, command, future));
        return jobId;
    }

    public JobInfo getJob(int jobId) {
        return jobs.get(jobId);
    }

    public Map<Integer, JobInfo> getAllJobs() {
        return new ConcurrentHashMap<>(jobs);
    }

    public void removeJob(int jobId) {
        jobs.remove(jobId);
    }

    public void cleanupCompletedJobs() {
        jobs.entrySet().removeIf(entry -> {
            Future<CommandResult> future = entry.getValue().getFuture();
            return future.isDone();
        });
    }

    public static class JobInfo {
        private final int jobId;
        private final String command;
        private final Future<CommandResult> future;

        public JobInfo(int jobId, String command, Future<CommandResult> future) {
            this.jobId = jobId;
            this.command = command;
            this.future = future;
        }

        public int getJobId() { return jobId; }
        public String getCommand() { return command; }
        public Future<CommandResult> getFuture() { return future; }
        public boolean isDone() { return future.isDone(); }
    }
}
