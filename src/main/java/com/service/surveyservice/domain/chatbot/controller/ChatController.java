package com.service.surveyservice.domain.chatbot.controller;

import com.service.surveyservice.domain.chatbot.dto.ChatGPTDto;
import com.service.surveyservice.domain.chatbot.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/chatbot")
@Slf4j
public class ChatController {


    private final ChatGptService chatGptService;

    @PostMapping(value = "/option")
    public ChatGPTDto.ChatReponse getOptionList(@RequestBody ChatGPTDto.QuestionRequestDto questionRequestDto) {
        ChatGPTDto.ChatReponse chatReponseData = chatGptService.askQuestion(questionRequestDto);
        return chatReponseData;
    }


    @PostMapping(value = "/anotherOption")
    public ChatGPTDto.ChatReponse getAnotherOptionList(@RequestBody ChatGPTDto.AnotherQuestionRequestDto anotherQuestionRequestDto) {

        log.info(anotherQuestionRequestDto.getQuestion());
        log.info(anotherQuestionRequestDto.getContent().toString());

        ChatGPTDto.ChatReponse chatReponseData = chatGptService.askAnotherQuestion(anotherQuestionRequestDto);

        return chatReponseData;
    }


}
