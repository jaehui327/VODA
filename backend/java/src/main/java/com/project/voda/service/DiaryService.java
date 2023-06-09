package com.project.voda.service;

import com.project.voda.dto.DiaryDetailResponseDto;

public interface DiaryService {

  // 일기 쓰기

  // 일기 삭제
  Boolean removeDiary(Long diarySeq);
  // 휴지통 삭제

  // 일기 내용, 주요감정
  DiaryDetailResponseDto getDiaryDetail(Long diarySeq);
}
