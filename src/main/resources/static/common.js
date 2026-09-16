// ===== 공통 스크립트 =====

var gEmp = null;


/** 화면 진입 시 세션 확인 */
function fnCheckSession(callback) {

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/session", true);

    xhr.onload = function() {

        if (xhr.status != 200) {
            location.href = "/login.html";
            return;
        }

        var res = JSON.parse(xhr.responseText);

        if (!res.success) {
            alert("로그인이 필요합니다.");
            location.href = "/login.html";
            return;
        }

        gEmp = res.data;
        fnDrawHeader();

        if (callback) callback();
    };

    xhr.send();
}


/** 상단에 로그인 정보 표시 */
function fnDrawHeader() {

    var div = document.getElementById("header");
    if (div == null) return;

    div.innerHTML =
        "<span>" + gEmp.empNm + " (" + gEmp.empNo + ") | " +
        "영업점 " + gEmp.branchCode + "</span>" +
        "<button onclick='fnLogout()' style='margin-left:12px;'>로그아웃</button>";
}


/** 로그아웃 */
function fnLogout() {

    if (!confirm("로그아웃 하시겠습니까?")) return;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/logout", true);
    xhr.onload = function() {
        location.href = "/login.html";
    };
    xhr.send();
}


/** 공통 응답 처리 (세션 만료 자동 감지) */
function fnHandleRes(xhr, onSuccess) {

    if (xhr.status != 200) {
        alert("서버 통신에 실패했습니다.");
        return;
    }

    var res = JSON.parse(xhr.responseText);

    if (!res.success) {
        // 세션 만료면 로그인 화면으로
        if (res.code == "L003") {
            alert("세션이 만료되었습니다. 다시 로그인해 주세요.");
            location.href = "/login.html";
            return;
        }
        alert(res.message + "\n(" + res.code + ")");
        return;
    }

    onSuccess(res.data);
}


/** 공통 포맷 */
function fnAmountFormat(amt) {
    if (!amt) return "0";
    return String(Number(amt)).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function fnDateFormat(dt) {
    if (!dt || dt.length != 8) return dt;
    return dt.substr(0,4) + "-" + dt.substr(4,2) + "-" + dt.substr(6,2);
}

function fnTimeFormat(tm) {
    if (!tm || tm.length != 6) return tm;
    return tm.substr(0,2) + ":" + tm.substr(2,2) + ":" + tm.substr(4,2);
}

/** 공통코드를 셀렉트박스에 채우기 */
function fnLoadCode(grpCode, elId) {

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/code?grpCode=" + grpCode, true);

    xhr.onload = function() {
        fnHandleRes(xhr, function(list) {

            var sel = document.getElementById(elId);
            sel.innerHTML = "";

            for (var i = 0; i < list.length; i++) {
                var opt = document.createElement("option");
                opt.value = list[i].code;
                opt.text  = list[i].codeNm;
                sel.appendChild(opt);
            }
        });
    };

    xhr.send();
}

function fnDrawHeader() {

    var div = document.getElementById("header");
    if (div == null) return;

    div.innerHTML =
        "<a href='/main.html' style='margin-right:12px;'>메뉴</a>" +
        "<span>" + gEmp.empNm + " (" + gEmp.empNo + ") | " +
        "영업점 " + gEmp.branchCode + "</span>" +
        "<button onclick='fnLogout()' style='margin-left:12px;'>로그아웃</button>";
}