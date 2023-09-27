import { imClient } from "./lim/core/ImClient";

// if(typeof window !== 'undefined'){
//     console.log("window");

// }
// if(typeof global !== 'undefined'){
//     console.log("global");

// }
// @ts-ignore
if (typeof uni !== 'undefined') {
    // console.log("uni");
    // @ts-ignore
    uni['im'] = imClient;
    // @ts-ignore
    // uni['im_webtoolkit'] = WebToolkit;
}

export {
    imClient as im
    // Logger as log,
    // WebToolkit as webtoolkit,
};

