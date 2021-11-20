$(document).ready(function () {
    bindObject();
    bindEvent();
    init();
});

// 객체 바인딩
function bindObject() {
    $groupSelector = $("#groupSelector");
}

// 이벤트 바인딩
function bindEvent() {
    $("#addBtn").on('click', fnAddUser);
    $("#deleteBtn").on('click', fnDeleteBtnClick);
    $("#deleteUser").on('click', fnDeleteUserClick);

    $("#newBtn").on('click', function() {
        selectedUser = {}
        $("#nickname").val("");
        $("#summonerId").val("");
        $(".modify-group").hide();
        $(".new-group").show();
    });

    $("#modifyBtn").on('click', fnModifyBtnClick);
    $groupSelector.on('change', changeGroupSelect);
    $("#saveTierPoint").on('click', saveTierPoint)
}

/* 체크박스 선택후 삭제 기능 */
function fnDeleteUserClick() {
    if(!currentGroup) {
        alert("생성한 그룹이 없습니다.");
        return false;
    }

    const checkedList = $("#myGroupList").find(".form-check-input:checked");
    if (checkedList.length === 0) {
        alert("선택한 유저가없습니다.");
        return false;
    }

    const userSeqArray = [];
    $.each(checkedList, function(index, item) {
        userSeqArray.push($(item).data('seq'));
    });

    const param = {
        groupSeq: currentGroup.groupSeq,
        userSeqArray: userSeqArray
    }
    common_ajax.call("/api/group/user", 'DELETE', true, param, function (res) {
        if (res.code === API_RESULT.FAIL) {
            alert(res.message);
            return;
        }
        toast.success("삭제되었습니다.")
        callMyGroup('/api/group/my', groupChangeFunction, currentGroup.groupSeq);
        $("#newBtn").trigger('click');
    });
}

/* 단일건 삭제기능 */
function fnDeleteBtnClick() {
    if(!currentGroup) {
        alert("생성한 그룹이 없습니다.");
        return false;
    }

    if (!selectedUser) {
        alert("선택한 유저가없습니다.");
        return false;
    }

    const param = {
        groupSeq: selectedUser.groupSeq,
        userSeqArray: [selectedUser.seq]
    }
    common_ajax.call("/api/group/user", 'DELETE', true, param, function (res) {
        if (res.code === API_RESULT.FAIL) {
            alert(res.message);
            return;
        }
        toast.success("삭제되었습니다")
        callMyGroup('/api/group/my', groupChangeFunction, currentGroup.groupSeq);
        $("#newBtn").trigger('click');
    });
}

/* 닉네임, 롤 아이디 수정 */
function fnModifyBtnClick() {
    if(!isValidationInput()) {
        return;
    }

    const param = {
        groupSeq: selectedUser.groupSeq,
        customUserSeq: selectedUser.seq,
        nickname: $("#nickname").val().trim(),
        summonerId: $("#summonerId").val().trim()
    }

    common_ajax.call("/api/group/user", "PUT", true, param, function(res) {
        if (res.code === API_RESULT.FAIL) {
            alert(res.message);
            return;
        }
        toast.success("수정되었습니다")
        callMyGroup('/api/group/my', groupChangeFunction, currentGroup.groupSeq);
        $("#newBtn").trigger('click');
    });
}

function saveTierPoint() {
    let validation = true;
    let param = [];
    $.each($('[name="tierPoint"]'), function (idx, obj) {
        const value = $(obj).val();
        const data = $(obj).data();
        if (!validation) {
            return;
        }

        if (isNaN(Number(value)) || Number(value) < 0 || Number.isInteger(value)) {
            toast.warning("티어점수는 0이상의 정수만 가능합니다.");
            $(obj).focus();
            validation = false;
        }
        param.push({
            groupSeq: data.groupSeq,
            userSeq: data.seq,
            tierPoint: Number(value)
        })

    });
    if (!validation) {
        return;
    }

    common_ajax.call('/api/group/tier-point', 'POST', true, param, function (res) {
        if (res.code === API_RESULT.FAIL) {
            toast.error(res.message);
            return;
        }
        toast.success("수정되었습니다")
        callMyGroup('/api/group/my', groupChangeFunction, currentGroup.groupSeq);
    })

}