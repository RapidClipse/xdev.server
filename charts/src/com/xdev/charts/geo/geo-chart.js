var google_geo_chart = google_geo_chart || {};

function makeid() {
  var text = "";
  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  for (var i = 0; i < 10; i++)
    text += possible.charAt(Math.floor(Math.random() * possible.length));

  return text;
}

google_geo_chart.XdevGeoChart = function(element) {
	var id = makeid();
    element.innerHTML = '<div style="width: 100%; height: 100%;" id="chart_geo_'+id+'" class="geo"></div>';
};