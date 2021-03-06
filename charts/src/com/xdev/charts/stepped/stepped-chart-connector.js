
window.com_xdev_charts_stepped_XdevSteppedAreaChart = function() {
	var component = new google_stepped_chart.XdevSteppedAreaChart(this.getElement());
	
	var state = this.getState();
	var connector = this;
	
	var chart_div = this.getElement().getElementsByClassName("stepped");

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
		
	google.charts.load('current', {packages: ['corechart']});
	google.charts.setOnLoadCallback(function(div, state, connector) {
		
		return function()
		{
			chart = new google.visualization.SteppedAreaChart(document.getElementById(div));
			
			if(typeof state.dataTable != 'undefined')
			{
				createAndDrawChart(state);
			}
	       
			window.addEventListener('resize', function()
	    	{
	    		
	    		if(typeof state.dataTable != 'undefined')
				{
	    			chart.draw(view, options);
				}
	    		
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
	    			//chartArea: {top:'15%', bottom:'15%', left:'15%', right:'15%'},
	    			isStacked: true,
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

	function selectHandler() 
	{
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
	
	function translateToJSON(state, item, data) 
	{
		if(typeof columns != 'undefined')
		{
			var columnLength = columns.length;
			
			var resultMap = new Map();
			
			var colIndex = item.column;
			var colCategory = columns[colIndex].id;
			var colType = columns[colIndex].type;
			var colValue = data.getValue(item.row, colIndex);
			
			resultMap.set("category", colCategory);
			resultMap.set("xvalue", data.getValue(item.row, 0));
			resultMap.set("yvalue", colValue);
			
			return strMapToObj(resultMap);
		}
	}
}