package com.service.surveyservice.domain.chatbot.service;

import com.service.surveyservice.domain.chatbot.dto.ChatGPTDto;
import com.service.surveyservice.domain.chatbot.dto.MultiChatMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
public class ChatGptService {

    private static RestTemplate restTemplate = new RestTemplate();
    
    @Value("${chatgpt.Authorization}")
    private String Authorization;
    @Value("${chatgpt.Bearer}")
    private String Bearer;
    @Value("${chatgpt.API_KEY}")
    private String API_KEY;
    @Value("${chatgpt.MODEL}")
    private String MODEL;
    @Value("${chatgpt.MEDIA_TYPE}")
    private String MEDIA_TYPE;
    @Value("${chatgpt.TEMPERATURE}")
    private Double TEMPERATURE;
    @Value("${chatgpt.URL}")
    private String URL;
    @Value("${chatgpt.MAX_TOKEN}")
    private Integer MAX_TOKEN;

    public HttpEntity<ChatGPTDto.ChatGptRequestDto> buildHttpEntity(ChatGPTDto.ChatGptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MEDIA_TYPE));
        headers.add(Authorization, Bearer + API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGPTDto.ChatGptResponseDto getResponse(HttpEntity<ChatGPTDto.ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGPTDto.ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                URL,
                chatGptRequestDtoHttpEntity,
                ChatGPTDto.ChatGptResponseDto.class);
        System.out.println("responseEntity = " + responseEntity.getBody());
        return responseEntity.getBody();
    }

    public ChatGPTDto.ChatReponse askQuestion(ChatGPTDto.QuestionRequestDto requestDto) {

        List<MultiChatMessageDto> messages = getMultiChatMessages();

        messages.add(new MultiChatMessageDto("user", requestDto.getQuestion() + "라는 질문에 대한 응답 항목 리스트 생성해줘"));
        ChatGPTDto.ChatBotResponseDto chatBotResponseDto = this.getResponse(
                this.buildHttpEntity(
                        new ChatGPTDto.ChatGptRequestDto(
                                MODEL,
                                messages,
                                MAX_TOKEN,
                                TEMPERATURE
                        )
                )
        ).toEntity();


        return ChatGPTDto.ChatReponse.builder()
                .content(chatBotResponseDto)
                .build();
    }


    public ChatGPTDto.ChatReponse askAnotherQuestion(ChatGPTDto.AnotherQuestionRequestDto anotherQuestionRequestDto) {

        List<MultiChatMessageDto> messages = getMultiChatMessages();

        messages.add(new MultiChatMessageDto("user", anotherQuestionRequestDto.getQuestion() + "라는 질문에 대한 응답 항목 리스트 생성해줘"));
        messages.add(new MultiChatMessageDto("assistant", anotherQuestionRequestDto.getContent().toString()));
        messages.add(new MultiChatMessageDto("user", "이전 질문에 대한 다른 답변 생성해줘"));
        ChatGPTDto.ChatBotResponseDto chatBotResponseDto = this.getResponse(
                this.buildHttpEntity(
                        new ChatGPTDto.ChatGptRequestDto(
                                MODEL,
                                messages,
                                MAX_TOKEN,
                                TEMPERATURE
                        )
                )
        ).toEntity();


        return ChatGPTDto.ChatReponse.builder()
                .content(chatBotResponseDto)
                .build();
    }


    private static List<MultiChatMessageDto> getMultiChatMessages() {
        List<MultiChatMessageDto> messages = new ArrayList<MultiChatMessageDto>();

        messages.addAll(Arrays.asList(
                new MultiChatMessageDto("system", "You are a helpful analyst who answers a list of survey questions according to the survey title on the survey platform"),
                new MultiChatMessageDto("user", "당신은 어떤 색을 좋아하나요? 라는 질문에 대한 응답 항목 리스트 생성해줘"),
                new MultiChatMessageDto("assistant", "{\"answer\" : [\"blue\",\"red\",\"white\",\"black\",\"pink\"],\"type\" : \"객관식\"}"),
                new MultiChatMessageDto("user", "당신의 mbti는 무엇인가요? 라는 질문에 대한 응답 항목 리스트 생성해줘"),
                new MultiChatMessageDto("assistant", "{\"answer\" : [\"INFJ\",\"ENFJ\",\"ENTP\",\"ENFP\",\"ENFJ\",\"ENTP\",\"ENTJ\"],\"type\" : \"객관식\"}"),
                new MultiChatMessageDto("user", "당신은 축구를 좋아하나요 라는 질문에 대한 응답 항목 리스트 생성해줘"),
                new MultiChatMessageDto("assistant", "{\"answer\" : [\"O\", \"X\"],\"type\" : \"찬부식\"}"),
                new MultiChatMessageDto("user", "저희 고객 서비스 담당자들이 얼마나 도움이 된다고 생각하십니까, 또는 도움이 안 된다고 생각하십니까? 라는 질문에 대한 응답 항목 리스트 생성해줘"),
                new MultiChatMessageDto("assistant", "{\"answer\" : [\"매우 도움이 되었음\", \"도움이 되었음\" ,\"도움이 되지도 안 되지도 않았음\" ,\"도움이 되지 않았음\", \"전혀 도움이 되지 않았음\"],\"type\" : \"객관식\"}"),
                new MultiChatMessageDto("user", "커피를 일주일에 얼마나 마시나요 라는 질문에 대한 응답 항목 리스트 생성해줘"),
                new MultiChatMessageDto("assistant", "{\"answer\" : [\"거의 먹지않음\", \"가끔\", \"자주\" , \"거의 매일\"],\"type\" : \"객관식\"}"),
                new MultiChatMessageDto("user", "다음 중 귀하께서 지금까지 사용해본 경험이 있는 음식배달 서비스를 선택해주세요 라는 질문에 대한 응답 항목 리스트 생성해줘"),
                new MultiChatMessageDto("assistant", "{\"answer\" : [\"배달의민족\", \"요기요\", \"배달통\", \"우버이츠\", \"카카오톡 주문하기\"],\"type\":\"객관식\"}"))
        );
        return messages;
    }
}