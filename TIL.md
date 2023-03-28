### 2023-03-28
피들스틱 이미지 정보 불러올 시, 데이터 리스트 항목에선 "FiddleSticks"으로 표기.
요청 데이터는 "Fiddlesticks"으로 입력해야 정상 작동하므로 로직 추가

### 2023-01-20
서버에서 받은 model 값을 자바스크립트 파라미터로 입력 중 오류 발생
```
<div class="match-detail" th:onclick="'clickedCard(' + ${info} + ')'">
```
<br>

에러
> Only variable expressions returning numbers or booleans are allowed in this context, any other datatypes are not trusted in the context of this expression, including Strings or any other object that could be rendered as a text literal.
<br>

해결방법
```
<div class="match-detail" th:onclick="clickedCard([[${info}]])">
```