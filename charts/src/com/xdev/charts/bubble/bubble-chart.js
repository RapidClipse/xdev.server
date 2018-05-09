var google_bubble_chart = google_bubble_chart || {};

function makeid() {
  var text = "";
  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  for (var i = 0; i < 10; i++)
    text += possible.charAt(Math.floor(Math.random() * possible.length));

  return text;
}

google_bubble_chart.XdevBubbleChart = function(element) {
	var id = makeid();
    element.innerHTML = '<div style="width: 100%; height: 100%;" id="chart_bubble_'+id+'" class="bubble"></div>';
};