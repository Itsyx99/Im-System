export class LoginPack {
    appId:number;
    userId?: string;
    clientType?: number;
    constructor(appId:number,userId: string, clientType?: number) {
        this.userId = userId;
        this.clientType = clientType;
        this.appId = appId;
    }
}