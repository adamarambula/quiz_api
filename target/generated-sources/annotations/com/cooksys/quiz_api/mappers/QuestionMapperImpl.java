package com.cooksys.quiz_api.mappers;

import com.cooksys.quiz_api.dtos.AnswerRequestDto;
import com.cooksys.quiz_api.dtos.AnswerResponseDto;
import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-30T14:05:46-0500",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 18.0.1 (Oracle Corporation)"
)
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public QuestionResponseDto entityToDto(Question entity) {
        if ( entity == null ) {
            return null;
        }

        QuestionResponseDto questionResponseDto = new QuestionResponseDto();

        questionResponseDto.setId( entity.getId() );
        questionResponseDto.setText( entity.getText() );
        questionResponseDto.setAnswers( answerMapper.entitiesToDtos( entity.getAnswers() ) );

        return questionResponseDto;
    }

    @Override
    public List<QuestionResponseDto> entitiesToDtos(List<Question> entities) {
        if ( entities == null ) {
            return null;
        }

        List<QuestionResponseDto> list = new ArrayList<QuestionResponseDto>( entities.size() );
        for ( Question question : entities ) {
            list.add( entityToDto( question ) );
        }

        return list;
    }

    @Override
    public Question dtoToEntity(QuestionRequestDto questionRequestDto) {
        if ( questionRequestDto == null ) {
            return null;
        }

        Question question = new Question();

        question.setText( questionRequestDto.getText() );
        question.setAnswers( answerRequestDtoListToAnswerList( questionRequestDto.getAnswers() ) );

        return question;
    }

    @Override
    public List<Question> dtosToEntities(List<QuestionResponseDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Question> list = new ArrayList<Question>( entities.size() );
        for ( QuestionResponseDto questionResponseDto : entities ) {
            list.add( questionResponseDtoToQuestion( questionResponseDto ) );
        }

        return list;
    }

    protected Answer answerRequestDtoToAnswer(AnswerRequestDto answerRequestDto) {
        if ( answerRequestDto == null ) {
            return null;
        }

        Answer answer = new Answer();

        answer.setText( answerRequestDto.getText() );
        if ( answerRequestDto.getCorrect() != null ) {
            answer.setCorrect( Boolean.parseBoolean( answerRequestDto.getCorrect() ) );
        }

        return answer;
    }

    protected List<Answer> answerRequestDtoListToAnswerList(List<AnswerRequestDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Answer> list1 = new ArrayList<Answer>( list.size() );
        for ( AnswerRequestDto answerRequestDto : list ) {
            list1.add( answerRequestDtoToAnswer( answerRequestDto ) );
        }

        return list1;
    }

    protected Answer answerResponseDtoToAnswer(AnswerResponseDto answerResponseDto) {
        if ( answerResponseDto == null ) {
            return null;
        }

        Answer answer = new Answer();

        answer.setId( answerResponseDto.getId() );
        answer.setText( answerResponseDto.getText() );

        return answer;
    }

    protected List<Answer> answerResponseDtoListToAnswerList(List<AnswerResponseDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Answer> list1 = new ArrayList<Answer>( list.size() );
        for ( AnswerResponseDto answerResponseDto : list ) {
            list1.add( answerResponseDtoToAnswer( answerResponseDto ) );
        }

        return list1;
    }

    protected Question questionResponseDtoToQuestion(QuestionResponseDto questionResponseDto) {
        if ( questionResponseDto == null ) {
            return null;
        }

        Question question = new Question();

        question.setId( questionResponseDto.getId() );
        question.setText( questionResponseDto.getText() );
        question.setAnswers( answerResponseDtoListToAnswerList( questionResponseDto.getAnswers() ) );

        return question;
    }
}
