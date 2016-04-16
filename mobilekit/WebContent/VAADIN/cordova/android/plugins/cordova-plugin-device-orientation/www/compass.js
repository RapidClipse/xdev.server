cordova.define("cordova-plugin-device-orientation.compass",function(e,n,a){var r=e("cordova/argscheck"),t=e("cordova/exec"),i=e("cordova/utils"),o=e("./CompassHeading"),c=e("./CompassError"),d={},s=null,g={getCurrentHeading:function(e,n,a){r.checkArgs("fFO","compass.getCurrentHeading",arguments);var i=function(n){var a=new o(n.magneticHeading,n.trueHeading,n.headingAccuracy,n.timestamp);e(a)},d=n&&function(e){var a=new c(e);n(a)};t(i,d,"Compass","getHeading",[a])},watchHeading:function(e,n,a){r.checkArgs("fFO","compass.watchHeading",arguments);var t=a!==void 0&&a.frequency!==void 0?a.frequency:100,o=a!==void 0&&a.filter!==void 0?a.filter:0,c=i.createUUID();if(o>0?(d[c]="iOS",g.getCurrentHeading(e,n,a)):d[c]=window.setInterval(function(){g.getCurrentHeading(e,n)},t),cordova.platformId==="browser"&&!s){var u=new Event("deviceorientation");s=window.setInterval(function(){window.dispatchEvent(u)},200)}return c},clearWatch:function(e){e&&d[e]&&(d[e]!="iOS"?clearInterval(d[e]):t(null,null,"Compass","stopHeading",[]),delete d[e],s&&Object.keys(d).length===0&&(window.clearInterval(s),s=null))}};a.exports=g})