package com.cooksys.quiz_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.cooksys.quiz_api.dtos.*;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.exceptions.BadRequestException;
import com.cooksys.quiz_api.exceptions.NotFoundException;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    /**
     * TODO: uSE boolean flags for ALL Delete methods
     * <p>
     * TODO: use Exception classes to handle all the errors
     * <p>
     * TODO: Need to check if Status codes are mandatory
     */

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    private final AnswerRepository answerRepository;

    /**
     * This method validations if the quiz id given is in the repository. If not it will throw an exception.
     *
     * @param quizId is provided by the end user via path variable.
     */
    private void validatingQuizInDatabase(Long quizId) {
        Optional<Quiz> quizCheck = quizRepository.findByIdAndDeletedFalse(quizId);
        if (quizCheck.isEmpty()) {
            throw new NotFoundException("Oops!! Quiz id " + quizId + " is not found. Please try again.");
        }
    }

    /**
     * This method validations if the question id given is in the repository
     * and question contains inside the quiz of questions. If not it will throw an exception.
     *
     * @param quizId     provided by the end user via path variable
     * @param questionId provided by the end user via path variable
     */
    private void validatingQuestionInDatabase(Long quizId, Long questionId) {
        Optional<Question> questionCheck = questionRepository.findByIdAndDeletedFalse(questionId);
        if (questionCheck.isEmpty()) {
            throw new NotFoundException("Oops!! Question id " + questionId + " is not found. Please try again.");
        }
        Quiz quiz = quizRepository.getById(quizId);
        //Contains all the questions ids of the quiz above
        List<Long> questionIds = new ArrayList<>();
        for (Question question : quiz.getQuestions()) {
            questionIds.add(question.getId());
        }
        //Checks if the question id that is given is inside the quiz of questions
        if (!questionIds.contains(questionId)) {
            throw new NotFoundException("Oops!! Question id " + questionId + " is not found. Please try again.");
        }
    }

    /**
     * Validates if all the required fields in the Quiz are inputted from the end user. If not it will throw an error.
     *
     * @param quizRequestDto this is coming from the end user
     */
    private void validatingAllRequiredQuizField(QuizRequestDto quizRequestDto) {

        if (quizRequestDto.getName() == null) {
            throw new BadRequestException("Quiz name is required");
        }
        if (quizRequestDto.getQuestions() == null || quizRequestDto.getQuestions().isEmpty()) {
            throw new BadRequestException("Questions are required");
        }
        for (QuestionRequestDto question : quizRequestDto.getQuestions()) {
            if (question.getText() == null) {
                throw new BadRequestException("Question name (Text) is required");
            }
            if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
                throw new BadRequestException("Answers are required");
            }
            for (AnswerRequestDto answer : question.getAnswers()) {
                if (answer.getText() == null && answer.getCorrect() == null) {
                    throw new BadRequestException("Answer (Text) name and correct are required");
                }
                if (answer.getText() == null) {
                    throw new BadRequestException("Answer (Text) is required");
                }
                if (answer.getCorrect() == null) {
                    throw new BadRequestException("Correct is required");
                }
            }
        }
    }

    /**
     * Validates if all the required fields in the Question are inputted from the end user. If not it will throw an error.
     *
     * @param questionRequestDto is coming from the user
     */
    public void validatingAllRequiredQuestionFields(QuestionRequestDto questionRequestDto) {

        if (questionRequestDto.getText() == null) {
            throw new BadRequestException("Question name is required");
        }

        List<AnswerRequestDto> answerRequestDtoList = questionRequestDto.getAnswers();

        if (answerRequestDtoList.isEmpty()) {
            throw new BadRequestException("Answers are required");
        }

        for (AnswerRequestDto answerRequestDto : answerRequestDtoList) {
            if (answerRequestDto.getText() == null && answerRequestDto.getCorrect() == null) {
                throw new BadRequestException("Answer (Text) name and correct are required");
            }
            if (answerRequestDto.getText() == null) {
                throw new BadRequestException("Answer (Text) is required");
            }
            if (answerRequestDto.getCorrect() == null) {
                throw new BadRequestException("Correct is required");
            }
        }
    }

    @Override
    public List<QuizResponseDto> getAllQuizzes() {
        return quizMapper.entitiesToDtos(quizRepository.findAllByDeletedFalse());
    }

    @Override
    public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
        //Validation of Quiz in Database
        validatingAllRequiredQuizField(quizRequestDto);

        List<QuestionRequestDto> questionCheck = quizRequestDto.getQuestions();

        //Converts quiz request DTO -> quiz Entity
        Quiz quizToSave = quizMapper.dtoToEntity(quizRequestDto);

        //Set field deleted to false.
        quizToSave.setDeleted(false);

        //Save QUIZ to populate the fields for questions
        quizRepository.saveAndFlush(quizToSave);

        //Iterate through the questions that were given.
        for (Question question : quizToSave.getQuestions()) {
            //Set QUESTION to correct QUIZ
            question.setQuiz(quizToSave);
            //Set Question deleted to false
            question.setDeleted(false);
            //Save QUESTION to populate the fields for ANSWERS
            questionRepository.saveAndFlush(question);
            for (Answer answer : question.getAnswers()) {
                //Set ANSWER to particular QUESTION
                answer.setQuestion(question);
                //Save ANSWER
                answerRepository.saveAndFlush(answer);
            }
        }
        return quizMapper.entityToDto(quizToSave);
    }

    @Override
    public void deleteQuiz(long id) {
        //Validation of Quiz in Database
        validatingQuizInDatabase(id);

        //Get the quiz from by the ID
        Quiz quizToDelete = quizRepository.getById(id);

        //Setting deleted to true
        quizToDelete.setDeleted(true);

        quizRepository.saveAndFlush(quizToDelete);

        /*
        //Goal -> First delete ANSWERS -> Then delete QUESTIONS -> Then delete QUIZ
        for (Question question : quizToDelete.getQuestions()) {
            for (Answer answer : question.getAnswers()) {
                answerRepository.delete(answer);
            }
            questionRepository.delete(question);
        }
        quizRepository.delete(quizToDelete);
         */
    }

    @Override
    public QuizResponseDto renameQuizName(Long id, String newName) {
        //Validation of Quiz in Database
        validatingQuizInDatabase(id);
        //Retrieve entity from quiz's id from quiz repository.
        Quiz quizToRename = quizRepository.getById(id);
        quizToRename.setName(newName);
        return quizMapper.entityToDto(quizRepository.saveAndFlush(quizToRename));
    }

    @Override
    public QuestionResponseDto getRandomQuestion(Long id) {
        //Validation of Quiz in Database
        validatingQuizInDatabase(id);

        Quiz getQuiz = quizRepository.getById(id);
        List<Question> getQuestions = getQuiz.getQuestions();
        //Generate a random number
        Random random = new Random();
        int randomNumber = random.nextInt(getQuestions.size());
        //Retrieve the value from the random number from the List.
        //return questionMapper.entityToDto(questionRepository.saveAndFlush(getQuestions.get(randomNumber)));
        return questionMapper.entityToDto(getQuestions.get(randomNumber));
    }

    @Override
    public QuizResponseDto addQuestionToQuiz(Long id, QuestionRequestDto questionRequestDto) {
        //Validation of Quiz in Database
        validatingQuizInDatabase(id);
        //Validation of Request Body of Question
        validatingAllRequiredQuestionFields(questionRequestDto);
        //Getting the QUIZ to add the QUESTION too
        Quiz addQuestionToQuiz = quizRepository.getById(id);
        //Current Question from QUIZ tht was provided
        List<Question> addQuestions = addQuestionToQuiz.getQuestions();
        //Creating a QUESTION Entity
        Question questionToSave = questionMapper.dtoToEntity(questionRequestDto);
        //Setting the QUESTION to the right QUIZ
        questionToSave.setQuiz(addQuestionToQuiz);
        //Saving the QUESTION to the question repository to populate the fields
        questionRepository.saveAndFlush(questionToSave);
        //Creating a ANSWER list
        List<Answer> answerToSave = questionToSave.getAnswers();
        //Iterate through the loop and saving each ANSWER to the above QUESTION
        //Then saving it to the answer repository
        for (Answer answer : answerToSave) {
            answer.setQuestion(questionToSave);
            answerRepository.saveAndFlush(answer);
        }
        //Adding the QUESTION to the List
        addQuestions.add(questionToSave);

        //Setting the new QUESTIONS list to the QUIZ
        addQuestionToQuiz.setQuestions(addQuestions);
        return quizMapper.entityToDto(quizRepository.saveAndFlush(addQuestionToQuiz));
    }

    @Override
    public QuestionResponseDto deleteQuestion(Long id, Long questionID) {
        //Validation of Quiz in Databases
        validatingQuizInDatabase(id);
        //Validation of Question in Database
        validatingQuestionInDatabase(id, questionID);

        //Retrieve the QUIZ
        Quiz quiz = quizRepository.getById(id);
        //Storing the deleted QUESTION
        Question questionDeleted = null;
        //Iterate through the QUESTIONS
        for (Question question : quiz.getQuestions()) {
            if (question.getId() == questionID) {
                //Save deleted QUESTIONS
                questionDeleted = question;
                for (Answer answer : question.getAnswers()) {
                    //Need to delete the ANSWERS b/c they are attached
                    answerRepository.delete(answer);
                }
                //Delete the QUESTION
                questionRepository.delete(question);
            }
        }
        return questionMapper.entityToDto(questionDeleted);
    }
}