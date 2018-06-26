
window.com_xdev_charts_line_XdevLineChart = function() {
	var component = new google_line_chart.XdevLineChart(this.getElement());
	
	var state = this.getState();
	var connector = this;
	
	var chart_div = this.getElement().getElementsByClassName("line");
	
	connector.divId(chart_div[0].id);

	var chart;
	var data;
	var view;
	var options;
	var columns;
	
	this.onStateChange = function() 
	{
		if(typeof state.dataTable != 'undefined')
		{
			createAndDrawChart(this.getState());
		}	
    };
	
	google.charts.load('current', {packages: ['corechart', 'line']});
	google.charts.setOnLoadCallback(function(div, state, connector) {
		
		return function()
		{
			chart = new google.visualization.LineChart(document.getElementById(div));
			
			if(typeof state.dataTable != 'undefined')
			{
				createAndDrawChart(state);
			}
	       
	    	window.addEventListener('resize', function() {
	    		chart.draw(view, options);
	    	});
	    	
	    	var element = document.getElementById(div);
	    	
	    	element.printImage = function() {
	    		connector.print_success(chart.getImageURI());
	    	};
	        
	        google.visualization.events.addListener(chart, 'select', selectHandler);
		}
		
	}(chart_div[0].id, state, connector));
	
	function createAndDrawChart(state)
	{	
		if(typeof state.dataTable.columns != 'undefined')
		{
			columns = state.dataTable.columns;
		}
		
		if(typeof chart != 'undefined')
		{
			options = 
			{
					title: state.config.title,
	    			titleTextStyle: state.config.titleTextStyle,
	    			backgroundColor: state.config.backgroundColor,
	    			fontName: state.config.fontName,
	    			fontSize: state.config.fontSize,
	    			legend: state.config.legend,
	    			pointSize: state.config.pointSize,
	    			pointShape: state.config.pointShape,
	    			lineWidth: state.config.lineWidth,
	    			lineDashStyle: state.config.lineDashStyle,
	    			colors: state.config.colors,
	    			orientation: state.config.orientation,
	    			//chartArea: {top:'15%', bottom:'15%', left:'15%', right:'15%'},
	    			curveType: state.config.curveType,
	    			hAxis: state.config.hAxis,
	    			vAxis: state.config.vAxis
			};
			
			data = new google.visualization.DataTable(
					{
						cols: columns,
						rows: state.dataTable.rows
					}
				)
				
			view = new google.visualization.DataView(data);
			var index = columns.map(function (icol) { return icol.id; }).indexOf('id');
			
			if(index >= 0)
			{
				view.hideColumns([index]);
			}
			
			chart.draw(view, options);
		}	
	}

	function selectHandler() {
		var selection = chart.getSelection();

		for (var i = 0; i < selection.length; i++)
		{
			var item = selection[i];
		
			if (item.row != null && item.column != null)
			{
				var json = translateToJSON(state, item, data);
				
				connector.select(json);
			}
		}
	}
}

