package org.example.pongguel.feign.client;

import org.example.pongguel.feign.dto.KakaoFriendsInfo;
import org.example.pongguel.feign.dto.KakaoTalkSendResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sendKakaoTalkClient", url="${kakao.send_base_url}")
public interface SendKakaoTalkClient {

    // 나에게 보내기
    @PostMapping(value = "${kakao.send_me_url}", consumes = "application/x-www-form-urlencoded")
    KakaoTalkSendResponse sendMessage(@RequestHeader("Authorization") String authorization,
                                      @RequestParam("template_object") String templateObject);

    // 친구 목록 가져오기
    @GetMapping(value = "${kakao.user_friend_url}", consumes = "application/x-www-form-urlencoded")
    KakaoFriendsInfo getKakaoFriends(@RequestHeader("Authorization") String authorization);

    // 친구들에게 보내기
    @PostMapping(value = "${kakao.send_friends_url}", consumes = "application/x-www-form-urlencoded")
    KakaoTalkSendResponse sendMessageToFriends(@RequestHeader("Authorization") String authorization,
                                               @RequestParam("receiver_uuids") String receiverUuids,
                                               @RequestParam("template_object") String templateObject);
}

