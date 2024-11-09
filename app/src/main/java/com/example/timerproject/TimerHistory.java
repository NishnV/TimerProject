package com.example.timerproject;

public class TimerHistory {
    private String duration;
    private String endTime;
    private String selectedSound;  // Property for selected sound

    // Constructor
    public TimerHistory(String duration, String endTime, String selectedSound) {
        this.duration = duration;
        this.endTime = endTime;
        this.selectedSound = selectedSound;
    }

    // Getters and Setters
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSelectedSound() {
        return selectedSound;
    }

    public void setSelectedSound(String selectedSound) {
        this.selectedSound = selectedSound;
    }
}