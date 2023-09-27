import Fetch from "../common/Fetch";
import Logger from "../log/Logger";



const Base64 = {
  _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
  encode: function(e) {
    var t = "";
    var n, r, i, s, o, u, a;
    var f = 0;
    e = Base64._utf8_encode(e);
    while (f < e.length) {
      n = e.charCodeAt(f++);
      r = e.charCodeAt(f++);
      i = e.charCodeAt(f++);
      s = n >> 2;
      o = (n & 3) << 4 | r >> 4;
      u = (r & 15) << 2 | i >> 6;
      a = i & 63;
      if (isNaN(r)) {
        u = a = 64
      } else if (isNaN(i)) {
        a = 64
      }
      t = t + this._keyStr.charAt(s) + this._keyStr.charAt(o) + this._keyStr.charAt(u) + this._keyStr.charAt(a)
    }
    return t
  },
  decode: function(e) {
    var t = "";
    var n, r, i;
    var s, o, u, a;
    var f = 0;
    e = e.replace(/[^A-Za-z0-9+/=]/g, "");
    while (f < e.length) {
      s = this._keyStr.indexOf(e.charAt(f++));
      o = this._keyStr.indexOf(e.charAt(f++));
      u = this._keyStr.indexOf(e.charAt(f++));
      a = this._keyStr.indexOf(e.charAt(f++));
      n = s << 2 | o >> 4;
      r = (o & 15) << 4 | u >> 2;
      i = (u & 3) << 6 | a;
      t = t + String.fromCharCode(n);
      if (u != 64) {
        t = t + String.fromCharCode(r)
      }
      if (a != 64) {
        t = t + String.fromCharCode(i)
      }
    }
    t = Base64._utf8_decode(t);
    return t
  },
  _utf8_encode: function(e) {
    e = e.replace(/rn/g, "n");
    var t = "";
    for (var n = 0; n < e.length; n++) {
      var r = e.charCodeAt(n);
      if (r < 128) {
        t += String.fromCharCode(r)
      } else if (r > 127 && r < 2048) {
        t += String.fromCharCode(r >> 6 | 192);
        t += String.fromCharCode(r & 63 | 128)
      } else {
        t += String.fromCharCode(r >> 12 | 224);
        t += String.fromCharCode(r >> 6 & 63 | 128);
        t += String.fromCharCode(r & 63 | 128)
      }
    }
    return t
  },
  _utf8_decode: function(e) {
    var t = "";
    var n = 0;
    var r = 0;
    var c1 = 0;
    var c2 = 0;
    while (n < e.length) {
      r = e.charCodeAt(n);
      if (r < 128) {
        t += String.fromCharCode(r);
        n++
      } else if (r > 191 && r < 224) {
        c2 = e.charCodeAt(n + 1);
        t += String.fromCharCode((r & 31) << 6 | c2 & 63);
        n += 2
      } else {
        c2 = e.charCodeAt(n + 1);
        let c3 = e.charCodeAt(n + 2);
        t += String.fromCharCode((r & 15) << 12 | (c2 & 63) << 6 | c3 & 63);
        n += 3
      }
    }
    return t
  }
};



export default class WebToolkit {
  // 获取浏览器信息
  private static getBrowserInfo(): any {
    let agent: any = navigator.userAgent.toLowerCase();
    let system = agent.split(" ")[1].split(" ")[0].split("(")[1];
    let REGSTR_EDGE = /edge\/[\d.]+/gi;
    let REGSTR_IE = /trident\/[\d.]+/gi;
    let OLD_IE = /msie\s[\d.]+/gi;
    let REGSTR_FF = /firefox\/[\d.]+/gi;
    let REGSTR_CHROME = /chrome\/[\d.]+/gi;
    let REGSTR_SAF = /safari\/[\d.]+/gi;
    let REGSTR_OPERA = /opr\/[\d.]+/gi;

    let info = {
      code: 0,
      system: system,
      browser: "",
      browserVersion: ""
    };
    // IE
    if (agent.indexOf("trident") > 0) {
      info.browser = agent.match(REGSTR_IE)[0].split("/")[0];
      info.browserVersion = agent.match(REGSTR_IE)[0].split("/")[1];
      return info;
    }
    // OLD_IE
    if (agent.indexOf("msie") > 0) {
      info.browser = agent.match(OLD_IE)[0].split(" ")[0];
      info.browserVersion = agent.match(OLD_IE)[0].split(" ")[1];
      return info;
    }
    // Edge
    if (agent.indexOf("edge") > 0) {
      info.browser = agent.match(REGSTR_EDGE)[0].split("/")[0];
      info.browserVersion = agent.match(REGSTR_EDGE)[0].split("/")[1];
      return info;
    }
    // firefox
    if (agent.indexOf("firefox") > 0) {
      info.browser = agent.match(REGSTR_FF)[0].split("/")[0];
      info.browserVersion = agent.match(REGSTR_FF)[0].split("/")[1];
      return info;
    }
    // Opera
    if (agent.indexOf("opr") > 0) {
      info.browser = agent.match(REGSTR_OPERA)[0].split("/")[0];
      info.browserVersion = agent.match(REGSTR_OPERA)[0].split("/")[1];
      return info;
    }
    // Safari
    if (agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0) {
      info.browser = agent.match(REGSTR_SAF)[0].split("/")[0];
      info.browserVersion = agent.match(REGSTR_SAF)[0].split("/")[1];
      return info;
    }
    // Chrome
    if (agent.indexOf("chrome") > 0) {
      info.browser = agent.match(REGSTR_CHROME)[0].split("/")[0];
      info.browserVersion = agent.match(REGSTR_CHROME)[0].split("/")[1];
      return info;
    } else {
      info.code = -1;
      return info;
    }
  }

  // TODO: 获取小程序设备信息
  private static getWxappInfo(): any {
    return {
      system: 'WXAPP',
      browser: 'WXAPP',
      browserVersion: '1.0'
    }
  }

  // TODO: 获取ReactNative设备信息
  private static getReactNativeInfo(): any {
    return {
      system: 'RNNative',
      browser: 'RNNative',
      browserVersion: '1.0'
    }
  }

  // TODO: 获取UniApp设备信息
  private static getUniAppInfo(): any {
    return {
      system: 'UNIAPP',
      browser: 'UNIAPP',
      browserVersion: '1.0'
    }
  }

  // 动态加入script 到head 标签处
  private static loadJS(url, callback ){
    var script:any = document.createElement('script'), fn = callback || function(){};
    script.type = 'text/javascript';
    // document.getElementsByTagName('head')[0].children[16].outerHTML.indexOf('http://pv.sohu.com/cityjson?ie=utf-8')
    let exist = false;
    for(const v in document.getElementsByTagName('head')[0].children){
      const dom = document.getElementsByTagName('head')[0].children[v];
      if(dom.outerHTML !== undefined && dom.outerHTML.indexOf(url) >= 0){
         exist = true;
      }
    }
    if(exist){
      fn();
      return;
    }

    //IE
    if(script.readyState){
        script.onreadystatechange = function(){
        if( script.readyState == 'loaded' || script.readyState == 'complete' ){
          script.onreadystatechange = null;
          fn();
        }
      };
    }else{
      //其他浏览器
      script.onload = function(){
        fn();
      };
    }
    script.src = url;
    document.getElementsByTagName('head')[0].appendChild(script);
  }

  // 获取当前ip信息(fetch方式)
  private static getIpInfoByFetch(callback) {
    const url = 'http://pv.sohu.com/cityjson?ie=utf-8';
    let fetch:any = Fetch.getFetchToolkit();
    const request: any = {method: "GET", mode: "cors", headers:{"Content-Type":"application/json"}};
    return fetch(url as string, request).then(response =>{
      return response.text();
    }).then(res =>{
      if(typeof callback === 'function'){
         let currentCity = eval('_current_city=' + res.replace('var returnCitySN = ', ''));
         callback(currentCity);
      }
    }).catch((e) =>{
       Logger.trace(e);
    });
  }

  // 获取当前ip信息(动态插入script脚本方式)
  private static getIpInfoByInsertScript(callback) {
    const url = 'http://pv.sohu.com/cityjson?ie=utf-8';
    WebToolkit.loadJS(url, function() {
      callback(window['returnCitySN']);
    })
  }


  public static getIpInfo(callback) {
    /*IFTRUE_WXAPP*/
    // 小程序的情况需要把pv.sohu.com域名加入白名单中
    WebToolkit.getIpInfoByFetch(callback);
    /*FITRUE_WXAPP*/

    /*IFTRUE_WEBAPP*/
    WebToolkit.getIpInfoByInsertScript(callback);
    /*FITRUE_WEBAPP*/

    /*IFTRUE_RNAPP*/
    //
    WebToolkit.getIpInfoByInsertScript(callback);
    /*FITRUE_RNAPP*/

    /*IFTRUE_UNIAPP*/
    // 小程序的情况需要把pv.sohu.com域名加入白名单中
    WebToolkit.getIpInfoByFetch(callback);
    /*FITRUE_UNIAPP*/
  }

  // 获取客户端设备信息
  public static getDeviceInfo():any {
    
    /*IFTRUE_WXAPP*/
    // 小程序的情况需要把pv.sohu.com域名加入白名单中
    const deviceInfo = WebToolkit.getWxappInfo();
    /*FITRUE_WXAPP*/

    /*IFTRUE_WEBAPP*/
    const deviceInfo = WebToolkit.getBrowserInfo();
    /*FITRUE_WEBAPP*/

    /*IFTRUE_RNAPP*/
    const deviceInfo = WebToolkit.getReactNativeInfo();
    /*FITRUE_RNAPP*/

    /*IFTRUE_UNIAPP*/
    const deviceInfo = WebToolkit.getUniAppInfo();
    /*FITRUE_UNIAPP*/

    return deviceInfo;
  }


  public static base64Encode(str){
     return Base64.encode(str);
  }

  public static base64Decode(str){
    return Base64.decode(str);
  }

}
