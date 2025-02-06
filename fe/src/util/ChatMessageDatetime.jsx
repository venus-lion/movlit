import React from 'react';
import {parseISO, format, differenceInMinutes, isToday} from 'date-fns';
import {ko} from 'date-fns/locale';

const formatCustomDate = (dateString) => {
    // ISO 문자열을 Date 객체로 변환
    const date = parseISO(dateString);
    const now = new Date();
    // 현재 시간과 주어진 날짜의 차이를 분 단위로 계산
    const diffMinutes = differenceInMinutes(now, date);

    // 예를 들어, 2분 이하이면 '방금 전', 3분이면 '3분전'으로 처리
    if (diffMinutes < 3) {
        return '방금 전';
    }

    // 날짜가 오늘이면 '오늘'로 표시 (리터럴 텍스트는 작은따옴표로 감싸줍니다)
    if (isToday(date)) {
        return '오늘';
    }

    // 날짜가 올해인지 확인 (올해이면 연도 표시 생략)
    const isThisYear = date.getFullYear() === now.getFullYear();
    // 그 외의 경우에는 날짜를 원하는 형식으로 포맷 (예: 2025년 2월 5일 오전 8:18)
    if (isThisYear) {
        // 올해: "월 일 오전/오후 시간:분" 형식
        return format(date, "M월 d일 a h:mm", {locale: ko});
    } else {
        // 올해가 아니면: "yyyy년 월 일 오전/오후 시간:분" 형식
        return format(date, "yyyy년 M월 d일 a h:mm", {locale: ko});
    }

};

const ChatMessageDatetime = (dateString) => {
    return formatCustomDate(dateString);
};

export default ChatMessageDatetime;
