var google_column_chart = google_column_chart || {};

function makeid() {
  var text = "";
  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  for (var i = 0; i < 10; i++)
    text += possible.charAt(Math.floor(Math.random() * possible.length));

  return text;
}

google_column_chart.XdevColumnChart = function(element) {
	var id = makeid();
    element.innerHTML = '<div style="width: 100%; height: 100%;" id="chart_column_'+id+'" class="column"></div>';
};