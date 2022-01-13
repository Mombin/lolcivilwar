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
    },
    _orders: {
        OWNER: 4,
        MANAGER: 3,
        MATCHER: 2,
        USER: 1,
        NONE: 0
    },
    getOrder: function (auth) {
        return this._orders[auth] || 0;
    }
}

const POSITION = {
    TOP: '탑',
    JG: '정글',
    MID: '미드',
    BOT: '원딜',
    SUP: '서폿'
}

const blackImage = "data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAA9CAIAAAB+wp2AAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABeSURBVGhD7c4BCQAxDACxeZp/CdX0z0wcFBIFOd9CZxZ66buKdEW6Il2RrkhXpCvSFemKdEW6Il2RrkhXpCvSFemKdEW6Il2RrkhXpCvSFemKdEW6Il2RrmxOLzPzA3ZtxVt56UJ2AAAAAElFTkSuQmCC";