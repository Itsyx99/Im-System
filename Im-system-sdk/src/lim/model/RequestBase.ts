export class RequestBase {
    appId:number;
    clientType: number;
    imei:string
    constructor(appId:number,clientType: number, imei: string) {
        this.appId = appId;
        this.clientType = clientType;
        this.imei = imei;
    }
}