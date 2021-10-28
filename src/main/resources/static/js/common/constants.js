const API_RESULT = {
    SUCCESS: "00000",
    FAIL: "99999"
}

const BROWSER_TYPE = {
    EXPLORER: 'explorer',
    CHROME: 'chrome',
    SAFARI: 'safari',
    FIREFOX: 'firefox'
}

const GROUP_AUTH = {
    OWNER: 'OWNER',
    MANAGER: 'MANAGER',
    MATCHER: 'MATCHER',
    USER: 'USER',
    NONE: 'NONE',
    isManageable: function(auth) {
        return auth === GROUP_AUTH.OWNER || auth === this.MANAGER
    },
    authName: function (auth) {
        let name = {
            OWNER: '소유자',
            MANAGER: '관리자',
            MATCHER: '주최자',
            USER: '사용자',
            NONE: '권한부족'
        }

        return name[auth] || ''
    },
    getAuthList: function() {
        return {
            OWNER: '소유자',
            MANAGER: '관리자',
            MATCHER: '주최자',
            USER: '사용자'
        }
    }
}

const POSITION = {
    TOP: '탑',
    JG: '정글',
    MID: '미드',
    BOT: '원딜',
    SUP: '서폿'
}