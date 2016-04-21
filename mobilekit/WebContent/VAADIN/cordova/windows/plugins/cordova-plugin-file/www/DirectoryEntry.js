cordova.define("cordova-plugin-file.DirectoryEntry",function(e,t,r){var n=e("cordova/argscheck"),o=e("cordova/utils"),i=e("cordova/exec"),c=e("./Entry"),a=e("./FileError"),s=e("./DirectoryReader"),l=function(e,t,r,n){t&&!/\/$/.test(t)&&(t+="/"),n&&!/\/$/.test(n)&&(n+="/"),l.__super__.constructor.call(this,!1,!0,e,t,r,n)};o.extend(l,c),l.prototype.createReader=function(){return new s(this.toInternalURL())},l.prototype.getDirectory=function(e,t,r,o){n.checkArgs("sOFF","DirectoryEntry.getDirectory",arguments);var c=this.filesystem,s=r&&function(e){var t=new l(e.name,e.fullPath,c,e.nativeURL);r(t)},u=o&&function(e){o(new a(e))};i(s,u,"File","getDirectory",[this.toInternalURL(),e,t])},l.prototype.removeRecursively=function(e,t){n.checkArgs("FF","DirectoryEntry.removeRecursively",arguments);var r=t&&function(e){t(new a(e))};i(e,r,"File","removeRecursively",[this.toInternalURL()])},l.prototype.getFile=function(t,r,o,c){n.checkArgs("sOFF","DirectoryEntry.getFile",arguments);var s=this.filesystem,l=o&&function(t){var r=e("./FileEntry"),n=new r(t.name,t.fullPath,s,t.nativeURL);o(n)},u=c&&function(e){c(new a(e))};i(l,u,"File","getFile",[this.toInternalURL(),t,r])},r.exports=l})