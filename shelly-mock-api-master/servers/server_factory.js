const isInt = require("../utils").isInt
const createBlindServer = require('./blind_server')
const createLightServer = require("./light_server")
const createPlugServer = require("./plug_server")
//process.argv

const arguments = process.argv.slice(2)
const aLength = arguments.length 
var port = 3000
var plugs=0,lights=0,blinds=0
console.log(aLength);
if ([2,4,6].includes(aLength)){
    for (i=0; i<aLength; i=i+2){
        const option = arguments[i] 
        const total = arguments[i+1]
        if (!isInt(total)){
            throw 'Command is not correct!';
        }
        switch(option){
            case "plug":{
                plugs=total
                break
            }
            case "blind":{
                blinds= total
                break
            }
            case "light":{
                lights=total
                break
            }
            default :{
                throw 'Command is not correct!';
            }
        }
    }
}

for(i=0;i<blinds;i++){
    createBlindServer(port);
    port++
}

for(i=0;i<plugs;i++){
    console.log(port);
    createPlugServer(port);
    port++
}

for(i=0;i<lights;i++){
    createLightServer(port);
    port++
}