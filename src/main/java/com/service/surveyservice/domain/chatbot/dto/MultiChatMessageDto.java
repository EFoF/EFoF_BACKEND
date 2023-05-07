package com.service.surveyservice.domain.chatbot.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiChatMessageDto {
    protected String role;
    private String content;
}

