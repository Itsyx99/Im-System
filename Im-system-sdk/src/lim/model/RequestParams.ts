export class RequestParams {
    appId:number;
    identifier?: string;
    userSign:string

    constructor(appId:number,identifier:string,userSign:string){
        this.appId = appId
        this.identifier = identifier
        this.userSign = userSign
    }

}