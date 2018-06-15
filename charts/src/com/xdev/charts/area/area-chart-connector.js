
window.com_xdev_charts_area_XdevAreaChart = function() {
	var component = new google_area_chart.XdevAreaChart(this.getElement());

	var state = this.getState();
	var connector = this;
	var chart_div = this.getElement().getElementsByClassName("area");
		
	connector.divId(chart_div[0].id);

	var chart;
	var data;
	var view;
	var options;
		
	google.charts.load('current', {packages: ['corechart']});
	google.charts.setOnLoadCallback(function(div, state, connector) {
		
		return function()
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
	    			chartArea: state.config.chartArea,
	    			isStacked: state.config.isStacked,
	    			hAxis: state.config.hAxis,
	    			vAxis: state.config.vAxis
	    	};
			
	    	 data = new google.visualization.DataTable(
	    		{
	    			cols: state.dataTable.columns,
	    			rows: state.dataTable.rows
	    		}
	    	)
	    	
	    	view = new google.visualization.DataView(data);
	    	var values = state.dataTable.columns.map(function (icol) { return icol.label; });
	    	var indices = getAllIndexes(values, 'hidden');

	    	if(indices.length > 0)
	    	{
	    		view.hideColumns(indices);
	    	}
	    	
			chart = new google.visualization.AreaChart(document.getElementById(div));		
	    	chart.draw(view, options);
			
	    	window.addEventListener('resize', function() {
	    		chart.draw(view, options);
	    	});
	    	
	    	var element = document.getElementById(div);
	    	element.config = function() {
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
		    			chartArea: state.config.chartArea,
		    			isStacked: state.config.isStacked,
		    			hAxis: state.config.hAxis,
		    			vAxis: state.config.vAxis
	    		};

	    		chart.draw(view, options);
	    	};
	    	
	    	element.refresh = function() {
	    		data = new google.visualization.DataTable(
	    				{
	    					cols: state.dataTable.columns,
	    					rows: state.dataTable.rows
	    				}
	    			)
	    			
	    		view = new google.visualization.DataView(data);
	    		var index = state.dataTable.columns.map(function (icol) { return icol.id; }).indexOf('id');
	    		
	    		if(index >= 0)
	    		{
	    			view.hideColumns([index]);
	    		}

	    		chart.draw(view, options);
	    	};
	    	
	    	element.printImage = function() {
	    		connector.print_success(chart.getImageURI());
	    	};

	        google.visualization.events.addListener(chart, 'select', selectHandler);
		}
		
	}(chart_div[0].id, state, connector));

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