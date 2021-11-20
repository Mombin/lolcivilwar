let $registerUserId
    , $registerPwd
    , $registerPwdChk
    , $registerEmail
    , $idValidationMessage
    , $pwdValidationMessage
    , $pwdChkValidationMessage
    , $emailValidationMessage
    , $registerBtn;

$(document).ready(function() {
    bindObject();
    bindEvent();
});

function bindObject() {
    $registerUserId = $("#registerUserId");
    $registerPwd =  $("#registerPwd");
    $registerPwdChk = $("#registerPwdChk");
    $registerEmail = $("#email");
    $idValidationMessage = $("#userIdValidation");
    $pwdValidationMessage = $("#pwdValidation");
    $pwdChkValidationMessage = $("#pwdChkValidation");
    $emailValidationMessage = $("#emailValidation");
    $registerBtn = $("#register")
}

function bindEvent() {
    $("#login").on('click', requestLogin);
    $("#registerLink").on('click', setRegisterPage);
    $("#loginLink").on('click', setLoginPage)
    $registerUserId.on('keyup', validateId);
    $registerPwd.on('keyup', validatePwd);
    $registerPwdChk.on('keyup', validatePwdChk);
    $registerEmail.on('keyup', validateEmail)
    $registerBtn.on('click', registerFn)
}

function requestLogin() {
    if(!loginValidationCheck()) {
        return
    }
    const param = {
        id: $("#userId").val(),
        password: $("#pwd").val()
    }
    common_ajax.call('/authenticate', 'POST', true, param, function (res) {
        if (res.code !== "00000") {
            toast.error(res.error);
            return;
        }
        toast.success("로그인에 성공하였습니다", function () {
            location.href = "/custom/mng";
        });
    });
}

function loginValidationCheck() {
    if ($("#userId").val().trim() === "") {
        toast.warning("아이디를 입력해주세요");
        return false;
    } else if ($("#pwd").val().trim() === "") {
        toast.warning("비밀번호를 입력해주세요");
        return false;
    }
    return true;
}

function setRegisterPage() {
    $("#loginForm").hide();
    $("#registerForm").show();
    $registerUserId.val('');
    $registerPwd.val('')
    $registerPwdChk.val('')
    $registerEmail.val('')
    $idValidationMessage.html('');
    $pwdValidationMessage.html('');
    $pwdChkValidationMessage.html('')
    $emailValidationMessage.html('')
}

function setLoginPage() {
    $("#loginForm").show();
    $("#registerForm").hide();
    $("#userId").val('')
    $("#pwd").val()
}

function validateId() {
    let id = $registerUserId.val().trim().toLowerCase();
    $registerUserId.val(id);
    const test = /[a-z0-9A-Z]{5,12}/.exec(id);
    if(test == null || test [0] !== id) {
        $idValidationMessage.html('아이디는 숫자와 영어로 5~12자리로 만들어주세요.');
        return false;
    }
    let result = true;
    common_ajax.call(`/user/checkId/${id}`, 'GET', false, {}, function(res) {
       if (res.code === API_RESULT.SUCCESS) {
           let data = res.data;
           if ($registerUserId.val() === data.id) {
               $idValidationMessage.html(data.result ? '' : '중복된 아이디입니다.');
               result = data.result;
           }
       }
    });
    return result;
}

function validatePwd() {
    let pwd = $registerPwd.val().trim();
    const test = /^.*(?=^.{8,12}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/.exec(pwd);
    $pwdValidationMessage.html(test === null ?'문자, 숫자, 특수문자를 포함하여 8~12자리로 만들어주세요': '');
    if($registerPwdChk.val().trim()) {
        validatePwdChk()
    }
    return test !== null;
}

function validatePwdChk() {
    $pwdChkValidationMessage.html($registerPwd.val().trim() !== $registerPwdChk.val().trim() ? '비밀번호가 일치하지 않습니다.' : '');
    return $registerPwd.val().trim() === $registerPwdChk.val().trim();
}

function validateEmail() {
    let email = $registerEmail.val().trim();
    if (email.indexOf("@") < 0) {
        $emailValidationMessage.html("email 형태가 아닙니다.");
        return false;
    }
    let result = true;
    common_ajax.call(`/user/checkEmail/${email}`, 'GET', false, {}, function(res) {
        if (res.code === API_RESULT.SUCCESS) {
            let data = res.data;
            if ($registerEmail.val() === data.email) {
                $emailValidationMessage.html(data.result ? '' : '중복된 이메일입니다.');
                result = data.result;
            }
        }
    });
    return result;
}

function registerFn() {
    if (!validateId()) {
        toast.warning("잘못된 아이디입니다.");
        return false;
    }
    if (!validatePwd()) {
        toast.warning("암호를 확인해주세요.");
        return false;
    }
    if (!validatePwdChk()) {
        toast.warning("암호를 확인해주세요.");
        return false;
    }
    if (!validateEmail()) {
        toast.warning("이메일을 확인해주세요.");
        return false;
    }
    const param = {
        id: $registerUserId.val().trim(),
        password: $registerPwd.val().trim(),
        email: $registerEmail.val().trim()
    }
    common_ajax.call('/user/register', "POST", true, param, function(res) {
        if (res.code === API_RESULT.SUCCESS) {
            toast.success("회원가입이 완료되었습니다.");
            setLoginPage();
        } else {
            console.log(res);
            toast.error()
        }
    })
}