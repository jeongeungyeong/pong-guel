package org.example.pongguel.feign.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class KakaoTalkSendRequest {
    private String template_object;

    public KakaoTalkSendRequest(TemplateObject templateObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.template_object = objectMapper.writeValueAsString(templateObject);
    }

    @Getter
    public static class TemplateObject {
        private String object_type;
        private Content content;
        private List<Button> buttons;

        public TemplateObject(String objectType, Content content, List<Button> buttons) {
            this.object_type = objectType;
            this.content = content;
            this.buttons = buttons;
        }
    }

    @Getter
    public static class Content {
        private String title;
        private String description;
        private String image_url;
        private int image_width;
        private int image_height;
        private Link link;

        public Content(String title, String description, String imageUrl, int imageWidth, int imageHeight, Link link) {
            this.title = title;
            this.description = description;
            this.image_url = imageUrl;
            this.image_width = imageWidth;
            this.image_height = imageHeight;
            this.link = link;
        }

        @Getter
        public static class Link {
            private String web_url;
            private String mobile_web_url;

            public Link(String webUrl, String mobileWebUrl) {
                this.web_url = webUrl;
                this.mobile_web_url = mobileWebUrl;
            }
        }
    }

    @Getter
    public static class Button {
        private String title;
        private Link link;

        public Button(String title, Link link) {
            this.title = title;
            this.link = link;
        }

        @Getter
        public static class Link {
            private String web_url;
            private String mobile_web_url;
            private String android_execution_params;
            private String ios_execution_params;

            public Link(String webUrl, String mobileWebUrl, String androidExecutionParams, String iosExecutionParams) {
                this.web_url = webUrl;
                this.mobile_web_url = mobileWebUrl;
                this.android_execution_params = androidExecutionParams;
                this.ios_execution_params = iosExecutionParams;
            }
        }
    }
}
