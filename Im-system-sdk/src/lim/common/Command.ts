enum MessageCommand {

    MSG_P2P = 0x44F,

    MSG_P2P_SYNC = 0x454,

    //发送消息已读   1106
    MSG_READED = 0x452,

    //消息接收ack
    MSG_RECIVE_ACK = 1107,

    //单聊消息ACK 1046
    MSG_ACK = 0x416,

    // 消息撤回 1050
    MSG_RECALL = 0x41A,

    // //消息撤回通知 1052
    MSG_RECALL_NOTIFY = 0x41C,

    // 消息撤回回包 1051
    MSG_RECALL_ACK = 0x41B,

    // //消息已读通知 1053
    MSG_READED_NOTIFY = 0x41D,

}

enum FriendShipCommand{
//添加好友 3000
    FRIEND_ADD = 0xbb8,

    //更新好友 3001
    FRIEND_UPDATE = 0xbb9,

    //删除好友 3002
    FRIEND_DELETE = 0xbba,

    //好友申请 3003
    FRIEND_REQUEST = 0xbbb,

    //好友申请已读 3004
    FRIEND_REQUEST_READ = 0xbbc,

    //好友申请审批 3005
    FRIEND_REQUEST_APPROVER = 0xbbd,

    //添加黑名单 3010
    FRIEND_BLACK_ADD = 0xbc2,

    //移除黑名单 3011
    FRIEND_BLACK_DELETE = 0xbc3,

    //新建好友分组 3012
    FRIEND_GROUP_ADD = 0xbc4,

    //删除好友分组 3013
    FRIEND_GROUP_DELETE = 0xbc5,

    //好友分组添加成员 3014
    FRIEND_GROUP_MEMBER_ADD = 0xbc6,

    //好友分组移除成员 3015
    FRIEND_GROUP_MEMBER_DELETE = 0xbc7,

}

enum GroupCommand{
/**
     * 推送申请入群通知 2000
     */
    JOIN_GROUP = 0x7d0,

    /**
     * 推送添加群成员 2001，通知给所有管理员和本人
     */
    ADDED_MEMBER = 0x7d1,

    /**
     * 推送创建群组通知 2002，通知给所有人
     */
    CREATED_GROUP = 0x7d2,

    /**
     * 推送更新群组通知 2003，通知给所有人
     */
    UPDATED_GROUP = 0x7d3,

    /**
     * 推送退出群组通知 2004，通知给管理员和操作人
     */
    EXIT_GROUP = 0x7d4,

    /**
     * 推送修改群成员通知 2005，通知给管理员和被操作人
     */
    UPDATED_MEMBER = 0x7d5,

    /**
     * 推送删除群成员通知 2006，通知给所有群成员和被踢人
     */
    DELETED_MEMBER = 0x7d6,

    /**
     * 推送解散群通知 2007，通知所有人
     */
    DESTROY_GROUP = 0x7d7,

    /**
     * 推送转让群主 2008，通知所有人
     */
    TRANSFER_GROUP = 0x7d8,

    /**
     * 禁言群 2009，通知所有人
     */
    MUTE_GROUP = 0x7d9,

    /**
     * 禁言/解禁 群成员 2010，通知管理员和被操作人
     */
    SPEAK_GOUP_MEMBER = 0x7da,

    //群聊消息收发   2104
    MSG_GROUP = 0x838,

    //群聊消息收发同步消息   2105
    MSG_GROUP_SYNC = 0x839,

    //群聊消息ACK 2047
    GROUP_MSG_ACK = 0x7ff,
}

enum SystemCommand{

    //心跳 9999
    PING = 0x270f,

    //登陸  9000
    LOGIN = 0x2328,

    //登录ack  9001
    LOGINACK = 0x2329,

    //下线通知 用于多端互斥  9002
    MUTUALLOGIN = 0x232a,

    //登出  9003
    LOGOUT = 0x232b,
}

enum UserEventCommand{
    //4000
    USER_MODIFY = 0xfa0,

    //4001
    USER_ONLINE_STATUS_CHANGE = 0xfa1,

    //4002 在线状态订阅
    USER_ONLINE_STATUS_SUBSCRIBE = 0xfa2,

    //4003 拉取订阅的在线状态好友,只发送给请求端
    PULL_USER_ONLINE_STATUS = 0xfa3,

    //4004 用户在线状态通知报文
    USER_ONLINE_STATUS_CHANGE_NOTIFY = 0xfa4,
}

enum ConversationEventCommand{
    //5000 会话删除
    CONVERSATION_DELETE = 0x1388,
    //5001 会话修改
    CONVERSATION_UPDATE = 0x1389,
}

export {
    MessageCommand,
    FriendShipCommand,
    GroupCommand,
    SystemCommand,
    UserEventCommand,
    ConversationEventCommand
};