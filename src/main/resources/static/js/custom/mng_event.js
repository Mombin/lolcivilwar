const positionList = ['top', 'jg', 'mid', 'bot', 'sup']
    , listOfPosition = {}
    , listOfSummoner = {};
let worker
    , teamModal
    , leaderModal
    , $randomTeamBtn
    , $randomLeaderBtn
    , $modalTeam
    , $modalLeader
    , $filterText
    , $pickCount
    , flag = false
    , newTeamFlagA = true
    , newTeamFlagB = true
    , newLeaderFlag = false;
$(document).ready(function () {
    setItems();
    setEvents();
    setMatchEvents();
});

function setItems() {
    $randomTeamBtn = $("#modalRandomTeam .modal-footer [name='randomBtn']");
    $randomLeaderBtn = $("#modalRandomLeader .modal-footer [name='randomBtn']");
    $modalTeam = $("#modalRandomTeam .modal-body.team");
    $modalLeader = $("#modalRandomLeader .modal-body.team");
    teamModal =  new bootstrap.Modal(document.getElementById('modalRandomTeam'), {
        keyboard: false
    });
    leaderModal = new bootstrap.Modal(document.getElementById('modalRandomLeader'), {
        keyboard: false
    });
    $groupSelector = $("#groupSelector");
    $filterText = $('[name="filter_text"]');
    $pickCount = $('[name="pick-count"]');
}

function setEvents() {
    $("[name='random_team']").on('click', function () {
        const team = $(this).data('team');
        if (!isValidationName(team)) {
            alert("해당 팀에 소환사 명을 적어주세요.");
            return false;
        }
        $modalTeam.data('team', team);
        $.each($("[name='team_" + team + "']"), function () {
            const position = $(this).parent().data('position');
            $modalTeam.find("[data-position='" + position + "'] input").val($(this).val());
            listOfPosition[position] = $(this).val();
        });
        teamModal.show();
    })

    $randomTeamBtn.on('click',  function () {
        flag = !flag;
        let param = {
            list: [],
            newTeamFlag: ($modalTeam.data('team') === 'a'? newTeamFlagA : newTeamFlagB)
        }
        $.each(listOfPosition, function (key, value) {
            param.list.push({position: key, name: value})
        });
        if (!flag) {
            let result = dice(param)
            $randomTeamBtn.html('랜덤');
            worker.terminate();
            $.each(result, function (index, item) {
                $modalTeam.find("[data-position='" + item.position.toLowerCase() + "'] input").val(item.name);
            })
            return;
        } else {
            $randomTeamBtn.html('스탑');
        }
        worker = new Worker('/static/js/custom/randomWorker.js');
        worker.postMessage({ flag : flag, list: listOfPosition, speed: 10
            , newTeamFlag: ($modalTeam.data('team') === 'a'? newTeamFlagA : newTeamFlagB)});
        worker.onmessage = function (evt) {
            const tempList = evt.data;
            $.each(tempList, function(index, item) {
                $modalTeam.find("[data-position='" + positionList[index] + "'] input").val(item.obj);
            })
        }
    })

    $("#modalRandomTeam .modal-footer [name='applyBtn']").on('click', function () {
        const team = $modalTeam.data('team')
        if (team === 'a') {
            newTeamFlagA = false
        } else {
            newTeamFlagB = false
        }
        $.each($modalTeam.find(".input-group"), function (index, item) {
            const position = $(item).data('position');
            const name = $(item).find('input').val();
            $("#team [data-position='" + position + "'] [name='team_" + team + "']").val(name);
        });
        $("#modalRandomTeam .btn-close").trigger('click');
    });

    $("#modalRandomTeam .btn-close").on('click', function () {
        if (flag) {
            $randomTeamBtn.trigger('click');
        }
    })

    $("[name='new_team']").on('click', function () {
        newTeamFlagA = true;
        newTeamFlagB = true;
        $("[name='select_leader']").show();
        $(".summoner-list").remove();
        $("[name='team_a']").val('');
        $("[name='team_b']").val('');
        const $summonerList = $("#summonerList");
        for (let i = 0; i < 10; i++) {
            const input = $("<input>").attr('type', 'text').attr('placeholder', '이름')
                .attr('name', 'summonerName')
                .data('index', i).addClass('form-control');
            const btnA = $("<button>").addClass('btn btn-outline-primary')
                .attr('type', 'button').attr('style', 'width: 15%')
                .on('click', moveTeam).data('target', 'a').html('A팀');
            const btnB = $("<button>").addClass('btn btn-outline-success')
                .attr('type', 'button').attr('style', 'width: 15%')
                .on('click', moveTeam).data('target', 'b')
                .html('B팀');
            const $summoner = $('<div>').addClass('input-group summoner-list').attr('style', 'margin-top: 5px;')
            $summoner.append(input);
            if (Object.keys(matchRate).length > 0) {
                input.on('change', function() {
                    let targetRate = makeRate(matchRate[$(this).val()]);
                    $(this).next().val(targetRate);
                });
                $summoner.append($("<input>").attr('type', 'text').attr('readonly', true)
                    .css('text-align', 'center')
                    .attr('name', 'summonerRate').addClass('form-control')).val();
            }

            $summoner.append(btnA).append(btnB);
            $summonerList.append($summoner);
        }
    });

    $("[name='select_leader']").on('click', function () {
        if (!isValidationList()) {
            alert('이름을 전부 입력해주세요.');
            return;
        }
        $.each(summonerNames(), function(index, item) {
            listOfSummoner[$(item).data('index')] = {
                name: $(item).val(),
                index: index
            };
        });
        $modalLeader.find("[data-team='A'] input").val('');
        $modalLeader.find("[data-team='B'] input").val('');
        leaderModal.show();
    });

    $randomLeaderBtn.on('click',  function () {
        if(!newLeaderFlag) {
            newLeaderFlag = true;
        }
        flag = !flag;
        if (!flag) {
            $randomLeaderBtn.html('랜덤');
            worker.terminate();
            return;
        } else {
            $randomLeaderBtn.html('스탑');
        }
        worker = new Worker('/static/js/custom/randomWorker.js');
        worker.postMessage({ flag : flag, list: listOfSummoner, speed: 50});
        worker.onmessage = function (evt) {
            const tempList = evt.data;
            $modalLeader.find("[data-team='A'] input").val(tempList[0].obj.name).data('index', tempList[0].obj.index);
            $modalLeader.find("[data-team='B'] input").val(tempList[1].obj.name).data('index', tempList[1].obj.index);
        }
    })

    $("#modalRandomLeader .btn-close").on('click', function () {
        if (flag) {
            $randomLeaderBtn.trigger('click');
        }
    })

    $("#modalRandomLeader .modal-footer [name='applyBtn']").on('click', function () {
        if (!newLeaderFlag) {
            alert("팀장을 정해주세요");
            return;
        }
        const teamAleader = $modalLeader.find("[data-team='A'] input");
        const teamBleader = $modalLeader.find("[data-team='B'] input");
        let gap = 0;
        if (teamBleader.data().index > teamAleader.data().index) {
            gap--;
        }

        summonerNames().eq(teamAleader.data().index).parent().remove();
        summonerNames().eq(teamBleader.data().index + gap).parent().remove();
        $("[data-position='top']").find("[name='team_a']").val(teamAleader.val())
        $("[data-position='top']").find("[name='team_b']").val(teamBleader.val())

        $("#modalRandomLeader .btn-close").trigger('click');
        $("[name='select_leader']").hide();
        newLeaderFlag = false;
    });

    $("[name='group_list_check']").on('click', function () {
        matchRate = {};
        callMyGroup('/api/group/my-match', groupChangeFunction)
    });

    $("[name='group_list_apply']").on('click', function () {
        $("[name='new_team']").trigger('click');
        var customGroupList = [];
        $.each($(".form-check-input.me-1:checked"), function(index, item) {
            customGroupList.push($(item).data('name'));
        });
        $.each($(".input-group.summoner-list"), function (index, item) {
            $(item).find("[name='summonerName']").val(customGroupList[index]);
            $(item).find("[name='summonerName']").trigger('change');
        })
        $("[name='group_list_check']").show();
        $filterText.hide().val('');
        $pickCount.hide();
        changePickCount(0);
        $("#groupUserList .list-group").hide();
        $("[name='group_list_apply']").hide();
        $groupSelector.hide();
    });

    // 그룹 선택시 이벤트
    $groupSelector.on('change', function () {
        let item = $(this).find('option:selected').data('group')
        setMyGroup(item)
        $("[name='group_list_check']").hide();
        $filterText.show().find('.form-control').val('');
        $pickCount.show();
        changePickCount(0);
        $("#groupUserList .list-group").show();
        $("[name='group_list_apply']").show();
        $(".input-group.summoner-list").remove();
        currentGroupSeq = item.groupSeq
    });
    $filterText.find('.form-control').on('keyup', filteringList);
}

function filteringList() {
    let filteringText = $(this).val()
        , groupSummoners = $('[name="group-summoner"]')
    if (filteringText === "") {
        groupSummoners.show();
        return;
    }
    $.each(groupSummoners, function(index, item) {
        let text = $(item).clone().children().remove().end().text();
        if (text.toUpperCase().indexOf(filteringText.toUpperCase()) === -1) {
            $(item).hide();
        } else {
            $(item).show()
        }
    })
}