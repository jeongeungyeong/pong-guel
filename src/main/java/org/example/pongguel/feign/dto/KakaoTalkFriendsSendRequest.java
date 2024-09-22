package org.example.pongguel.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoTalkFriendsSendRequest extends KakaoTalkSendRequest{
    @JsonProperty("receiver_uuids")
    private List<String> receiverUuids;

    public KakaoTalkFriendsSendRequest(TemplateObject template_object, List<String> receiverUuids) throws JsonProcessingException {
        super(template_object);
        this.receiverUuids = receiverUuids;
    }
}
