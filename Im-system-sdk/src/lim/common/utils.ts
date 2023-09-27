
export default class Beans {
    public static to(target:any):any{
        return JSON.parse(JSON.stringify(target));
    }

    public static bean(json:string):any{
        return JSON.parse(json);
    }
    public static copy(target:any):any{
        return Beans.to(target);
    }
    public static replace(target, replaceJSON){
        const r = Beans.to(target);
        for(const v in replaceJSON){
            r[v] = replaceJSON[v];
        }
        return r;
    }

    public static toMapByKey(arrs:Array<any>, key:string){
        const result = {};
        arrs.forEach((v)=>{
            if(v.hasOwnProperty(key)){
                result[key] = v;
            }
        });
        return result;
    }

    public static json(target:any):any{
        return JSON.stringify(target)
    }

    public static  strEmpty(s:string):boolean{
        return !!s;
    }

    public static  strNotEmpty(s:string):boolean{
        return !s;
    }


    public static isEmpty = (str: any): boolean => {
        if (
          str === null ||
              str === '' ||
              str === undefined ||
              str.length === 0
        ) {
          return true
        } else {
          return false
        }
      };

    public static uuid = ():string => {
        return (Math.random()*36).toString(36).slice(2)+new Date().getTime().toString();
    }

}
