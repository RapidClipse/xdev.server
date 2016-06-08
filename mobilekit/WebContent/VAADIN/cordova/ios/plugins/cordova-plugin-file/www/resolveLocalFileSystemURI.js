cordova.define("cordova-plugin-file.resolveLocalFileSystemURI",function(e,l,o){(function(){function l(){return cordova.platformId==="browser"&&e("./isChrome")()?(o.exports.resolveLocalFileSystemURL=window.resolveLocalFileSystemURL||window.webkitResolveLocalFileSystemURL,!0):!1}if(!l()){var s=e("cordova/argscheck"),t=e("./DirectoryEntry"),i=e("./FileEntry"),r=e("./FileError"),n=e("cordova/exec"),a=e("./fileSystems");o.exports.resolveLocalFileSystemURL=o.exports.resolveLocalFileSystemURL||function(e,l,o){s.checkArgs("sFF","resolveLocalFileSystemURI",arguments);var c=function(e){o&&o(new r(e))};if(!e||e.split(":").length>2)return setTimeout(function(){c(r.ENCODING_ERR)},0),void 0;var m=function(e){if(e){if(l){var o=e.filesystemName||e.filesystem&&e.filesystem.name||(e.filesystem==window.PERSISTENT?"persistent":"temporary");a.getFs(o,function(s){s||(s=new FileSystem(o,{name:"",fullPath:"/"}));var r=e.isDirectory?new t(e.name,e.fullPath,s,e.nativeURL):new i(e.name,e.fullPath,s,e.nativeURL);l(r)})}}else c(r.NOT_FOUND_ERR)};n(m,c,"File","resolveLocalFileSystemURI",[e])},o.exports.resolveLocalFileSystemURI=function(){console.log("resolveLocalFileSystemURI is deprecated. Please call resolveLocalFileSystemURL instead."),o.exports.resolveLocalFileSystemURL.apply(this,arguments)}}})()})