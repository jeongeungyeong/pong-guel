package org.example.pongguel.feign.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.feign.client.SendKakaoTalkClient;
import org.example.pongguel.feign.dto.KakaoFriendsInfo;
import org.example.pongguel.feign.dto.KakaoTalkSendResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoTalkService {
    private final SendKakaoTalkClient sendKakaoTalkClient;
    private final ObjectMapper objectMapper;

    // 나에게 퐁글(책) 공유하기
    public boolean sendKakaoTalkToMe(String accessToken, String title,
                                     String description,
                                     String imageUrl,
                                     String webUrl){
        try{
            // 본문 객체 형성
            String templateObjectJson = createTemplateObjectJson(title,description,imageUrl,webUrl);
            // feign 사용해서 본문 담고 보내기
            KakaoTalkSendResponse response = sendKakaoTalkClient.sendMessage("Bearer " + accessToken,templateObjectJson);
            return handleResponse(response, title);
        } catch (Exception e){
            return handleException(e,"나에게 카카오톡 메시지 전송 오류");
        }
    }

    // 친구에게 퐁글(책) 공유하기
    public boolean sendKakaoTalkToFriends(String accessToken, String title, String description,
                                                String imageUrl, String webUrl) {
        try {
            List<String> selectedFriendUuids = getSelectedFriendUuids(accessToken);
            if (selectedFriendUuids.isEmpty()) {
                log.warn("선택된 친구가 없습니다.");
                return false;
            }

            String templateObjectJson = createTemplateObjectJson(title, description, imageUrl, webUrl);
            String receiverUuidsJson = objectMapper.writeValueAsString(selectedFriendUuids);

            KakaoTalkSendResponse response = sendKakaoTalkClient.sendMessageToFriends(
                    "Bearer " + accessToken, receiverUuidsJson, templateObjectJson);

            return handleResponse(response, "선택한 친구에게");
        } catch (Exception e) {
            return handleException(e, "친구에게 메시지 전송");
        }
    }
    // 본문 객체 생성하기
    private String createTemplateObjectJson(String title, String description, String imageUrl, String webUrl) throws JsonProcessingException {
        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "feed"); // 피드 타입
        templateObject.put("content", Map.of(
                "title", title,
                "description", description,
                "image_url", imageUrl,
                "link", Map.of("web_url", webUrl, "mobile_web_url", webUrl)
        ));
        templateObject.put("buttons", List.of(Map.of(
                "title", "퐁글이 자세히 보기",
                "link", Map.of("web_url", webUrl, "mobile_web_url", webUrl)
        )));
        return objectMapper.writeValueAsString(templateObject);
    }
    // 사용자의 친구 목록 가져오기
    private List<String> getSelectedFriendUuids(String accessToken) {
        // 친구목록 카카오로부터 받아오기
        KakaoFriendsInfo kakaoFriendsInfo = sendKakaoTalkClient.getKakaoFriends("Bearer " + accessToken);
        log.info("친구 목록 조회 완료. 총 친구 수: {}", kakaoFriendsInfo.getElements().size());
        // 친구목록 중 상위 3명만 임시 선택
        return kakaoFriendsInfo.getElements().stream()
                .limit(3)
                .map(KakaoFriendsInfo.Friend::getUuid)
                .collect(Collectors.toList());
    }
    // 카카오톡 보내기와 관련된 오류 (특정 응답코드에 따른 처리로 우선 서비스단에서 해결)
    private boolean handleResponse(KakaoTalkSendResponse response, String context) {
        if (response.resultCode() == 0) {
            log.info("{} 메시지 전송에 성공했습니다.", context);
            return true;
        } else {
            log.error("{} 메시지 전송에 실패했습니다. 에러코드: {}", context, response.resultCode());
            return false;
        }
    }

    private boolean handleException(Exception e, String context) {
        log.error("{} 중 오류가 발생했습니다.", context, e);
        return false;
    }
}
