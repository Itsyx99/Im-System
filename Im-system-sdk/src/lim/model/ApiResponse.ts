export default class ApiResponse {
    public code;
    public msg = '';
    public data: any = null;

    constructor(succeed = false) {
        if(succeed){
            this.code = 200;
        }
    }

    public isSucceed(): boolean {
        return this.code === 200;
    }

    public isFailed(): boolean{
        return !this.isSucceed();
    }

}
