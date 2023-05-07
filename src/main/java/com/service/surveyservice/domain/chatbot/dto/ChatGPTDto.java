package com.service.surveyservice.domain.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ChatGPTDto {
    @Getter
    @NoArgsConstructor
    public static class QuestionRequestDto implements Serializable {
        private String question;


        @Builder
        public QuestionRequestDto(String question) {
            this.question = question;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class AnotherQuestionRequestDto implements Serializable {
        private String question;
        private Object content;

        @Builder
        public AnotherQuestionRequestDto(String question, Object content) {
            this.question = question;
            this.content = content;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ChatGptRequestDto implements Serializable {

        private String model;
        private List<MultiChatMessageDto> messages;
        @JsonProperty("max_tokens")
        private Integer maxTokens;
        private Double temperature;
        @JsonProperty("top_p")
        private Double topP;

        @Builder
        public ChatGptRequestDto(String model, List<MultiChatMessageDto> messages,
                                 Integer maxTokens, Double temperature
        ) {
            this.model = model;
            this.messages = messages;
            this.maxTokens = maxTokens;
            this.temperature = temperature;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ChatGptResponseDto implements Serializable {

        private String id;
        private String object;
        private LocalDate created;
        private String model;
        private List<Choice> choices;

        @Builder
        public ChatGptResponseDto(String id, String object,
                                  LocalDate created, String model,
                                  List<Choice> choices) {
            this.id = id;
            this.object = object;
            this.created = created;
            this.model = model;
            this.choices = choices;
        }

        public ChatBotResponseDto toEntity() {
            try {
                JSONParser jsonParser = new JSONParser();
                //3. To Object
                Object obj = jsonParser.parse(this.choices.get(0).getMessage().getContent());

                //4. To JsonObject
                JSONObject jsonObj = (JSONObject) obj;

                List<String> answer = (List) jsonObj.get("answer");
                String type = (String) jsonObj.get("type");

                return ChatBotResponseDto.
                        builder().
                        answer(answer).
                        type(type).
                        build();
            } catch (ParseException |ClassCastException e) {
                return ChatBotResponseDto.
                        builder().
                        answer(Collections.singletonList("올바르지 않은 객관식 질문 양식입니다.")).
                        type("wrong").
                        build();
            }
        }
    }

        @Getter
        @NoArgsConstructor
        public static class Choice implements Serializable {

            private MultiChatMessageDto message;
            private Integer index;
            @JsonProperty("finish_reason")
            private String finishReason;

            @Builder
            public Choice(MultiChatMessageDto message, Integer index, String finishReason) {
                this.message = message;
                this.index = index;
                this.finishReason = finishReason;
            }
        }


        @Getter
        @NoArgsConstructor
        public static class ChatBotResponseDto {
            private List<String> answer;
            private String type;

            @Builder
            public ChatBotResponseDto(List<String> answer, String type) {
                this.answer = answer;
                this.type = type;
            }
        }

    @Getter
    @NoArgsConstructor
    public static class ChatReponse {
        private Object content;

        @Builder
        public ChatReponse(Object content) {
            this.content = content;
        }
    }

}