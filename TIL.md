### 2023-01-20
서버에서 받은 model 값을 자바스크립트 파라미터로 입력하려고 했는데 요런식으로 작성해보니 오류가 발생했다.
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