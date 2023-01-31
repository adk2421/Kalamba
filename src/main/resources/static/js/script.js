
function checkName() {
    let name = document.getElementById("summonerName");
    const regex = /[^0-9A-Za-z가-힣]/ig;

    if (name.value == "")
        alert("소환사 이름을 입력해주세요.");
    else if (regex.test(name.value) === true)
        alert("소환사 이름을 제대로 입력해주세요.");
    else {
        document.getElementById("exeModal").click();
        document.getElementById("summonerSearchForm").submit();
    }
}