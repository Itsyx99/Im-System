import log from "../log/Logger";
import { imClient } from './ImClient';
import ApiResponse from "../model/ApiResponse";
import Beans from "../common/utils";
import Fetch from "../common/Fetch";

export default class HttpApi {

    url:string = "";//http://127.0.0.1:8000/v1

    constructor(url :string){
        this.url = url;
    }

    call(url:string,params?:any,body?: any): Promise<ApiResponse> {
        
        let userId = imClient.userId;
        log.info(userId);

        let reqUrl = this.url + url;
        if(params != null){
            let paramStr = this.setQueryConfig(params)
            console.log(paramStr);
            reqUrl += paramStr;
        }

        let requestMethod = "POST";

        const headers = {
            'Content-Type':'application/json',
        };

        const request: any = {method: requestMethod,headers:headers, mode: 'cors', body: Beans.json(body)};
        return this.httpFetch(reqUrl, request);

    }

    setQueryConfig(params:any){ 
	　　var _str = "?"; 
	　　for(const o in params){ 
        　　_str += o + "=" + params[o] + "&"; 
	　　} 
	　　var _str = _str.substring(0, _str.length-1); //末尾是&
	　　return _str; 
	}


    private httpFetch(url:string, request:any):Promise<ApiResponse>{
        console.log("httpFetch")
        /*IFTRUE_WXAPP*/
        // @ts-ignore
        if(wx === undefined){
            throw new Error('wx handle not exist');
        }
        return new Promise<ApiResponse>(function (resolve, reject) {
            // @ts-ignore
            wx.request({
                method: request.method,
                url: url, 
                data: Beans.bean(request),
                header: request.headers,
                success (res) {
                    console.log(res)
                    resolve(res.data);
                },
                fail(res){
                    console.log(res)
                    reject(res.data);
                }
            });
        });
        /*FITRUE_WXAPP*/

        /*IFTRUE_WEBAPP*/
        let webfetch = Fetch.getFetchToolkit();
        console.log("webfetch")
        return webfetch(url as string, request).then(response =>{
            return response.json();
        }).then(res =>{
            log.info(`==> [${request.method}] ${url} back:` + Beans.json(res));
            const resp = this.response2ApiResponse(res);
            console.log(res)
            if(resp.isFailed()){
                return Promise.reject(resp);
            }
            return Promise.resolve(this.response2ApiResponse(res));
        });
        /*FITRUE_WEBAPP*/

        /*IFTRUE_RNAPP*/
        let rnfetch = Fetch.getFetchToolkit();
        console.log("rnfetch")
        return rnfetch(url as string, request).then(response =>{
            return response.json();
        }).then(res =>{
            log.info(`==> [${request.method}] ${url} back:` + Beans.json(res));
            const resp = this.response2ApiResponse(res);
            if(resp.isFailed()){
                return Promise.reject(resp);
            }
            return Promise.resolve(this.response2ApiResponse(res));
        });
        /*FITRUE_RNAPP*/

        /*IFTRUE_UNIAPP*/
        let rnfetch1 = Fetch.getFetchToolkit();
        return rnfetch1(url as string, request).then(response =>{
            console.log(response)
            return response.json();
        }).then(res =>{
            log.info(`==> [${request.method}] ${url} back:` + Beans.json(res));
            const resp = this.response2ApiResponse(res);
            if(resp.isFailed()){
                return Promise.reject(resp);
            }
            // return Promise.resolve(this.response2ApiResponse(res));
            return Promise.resolve(resp);
        });
        /*FITRUE_UNIAPP*/
    }

    public response2ApiResponse( response: any): ApiResponse {
        const apiResponse: ApiResponse = new ApiResponse(true);
        apiResponse.data = response.data;
        apiResponse.msg = response.msg;
        apiResponse.code = response.code;
        return apiResponse;
    }

}
// export const httpApi = new HttpApi();