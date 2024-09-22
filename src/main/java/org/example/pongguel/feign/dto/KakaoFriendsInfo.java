package org.example.pongguel.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KakaoFriendsInfo {
    @JsonProperty("elements")
    private List<Friend> elements;
    private int totalCount;
    private String beforeUrl;
    private String afterUrl;

    @Data
    public static class Friend {
        private Long id;

        @JsonProperty("uuid")
        private String uuid;
        private String profileNickname;
    }
}
