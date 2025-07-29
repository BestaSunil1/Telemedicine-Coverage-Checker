package com.example.demo.service;

import java.util.Map;

public class CreateMeetingRequest {
    private String topic;
    private int duration;
    private String start_time;
    private String timezone;
    private int type = 2; // Scheduled Meeting

    private final Map<String, Object> settings = Map.of(
            "host_video", true,
            "participant_video", true,
            "join_before_host", true,
            "mute_upon_entry", true,
            "waiting_room", false
    );

    public CreateMeetingRequest(String topic, int duration, String start_time, String timezone) {
        this.topic = topic;
        this.duration = duration;
        this.start_time = start_time;
        this.timezone = timezone;
    }

    // Getters only (JSON serialization)
    public String getTopic() { return topic; }
    public int getDuration() { return duration; }
    public String getStart_time() { return start_time; }
    public String getTimezone() { return timezone; }
    public int getType() { return type; }
    public Map<String, Object> getSettings() { return settings; }
}