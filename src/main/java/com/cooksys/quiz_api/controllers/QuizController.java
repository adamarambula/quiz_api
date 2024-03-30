package com.cooksys.quiz_api.controllers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    public List<QuizResponseDto> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping
    public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
        return quizService.createQuiz(quizRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable long id){
        quizService.deleteQuiz(id);
    }

    @PatchMapping("/{id}/rename/{newName}")
    public QuizResponseDto renameQuizName(@PathVariable Long id, @PathVariable String newName){
        return quizService.renameQuizName(id, newName);
    }

    @GetMapping("/{id}/random")
    public QuestionResponseDto getRandomQuestion(@PathVariable Long id){
       return quizService.getRandomQuestion(id);
    }

    @PatchMapping("/{id}/add")
    public QuizResponseDto addQuestionToQuiz(@PathVariable Long id, @RequestBody QuestionRequestDto questionRequestDto){
        return quizService.addQuestionToQuiz(id, questionRequestDto);
    }

    @DeleteMapping("/{id}/delete/{questionID}")
    public QuestionResponseDto deleteQuestion(@PathVariable Long id, @PathVariable Long questionID){
        return quizService.deleteQuestion(id, questionID);
    }
}
