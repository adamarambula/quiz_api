package com.cooksys.quiz_api.mappers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.entities.Question;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AnswerMapper.class })
public interface QuestionMapper {


  //Database (Entity) - > Application (DTO)
  //Single
  QuestionResponseDto entityToDto(Question entity);
  //List
  List<QuestionResponseDto> entitiesToDtos(List<Question> entities);

  //DTO -> Entity
  //Single
  Question dtoToEntity(QuestionRequestDto questionRequestDto);
  //List
  List<Question> dtosToEntities(List<QuestionResponseDto> entities);

}
