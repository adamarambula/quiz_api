package com.cooksys.quiz_api.mappers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Quiz;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { QuestionMapper.class , AnswerMapper.class })
public interface QuizMapper {

  //Database (Entity) -> Application (DTO)
  QuizResponseDto entityToDto(Quiz entity);

  //Database -> Application
  List<QuizResponseDto> entitiesToDtos(List<Quiz> entities);

  //Application (DTO) -> Database (Entity)
  Quiz dtoToEntity(QuizRequestDto quizRequestDto);

}
