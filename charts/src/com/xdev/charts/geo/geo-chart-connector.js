
window.com_xdev_charts_geo_XdevGeoChart = function() {
	var component = new google_geo_chart.XdevGeoChart(this.getElement());
	
	var state = this.getState();
	var connector = this;
	
	var chart_div = this.getElement().getElementsByClassName("geo");
	
	connector.divId(chart_div[0].id);

	var chart;
	var data;
	var view;
	var options;
		
	google.charts.load('current', {packages: ['geochart'], 'mapsApiKey' : state.mapsApiKey});
	google.charts.setOnLoadCallback(function(div, state, connector) {
		
		return function()
		{
			options = 
	    	{
	    		region: state.config.region,
	    		displayMode: state.config.displayMode,
	    		defaultColor: state.config.defaultColor,
	    		datalessRegionColor: state.config.datalessRegionColor,
	    		colorAxis: state.config.colorAxis,
	    		backgroundColor: state.config.backgroundColor
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
	    	
	    	chart = new google.visualization.GeoChart(document.getElementById(div));		
	    	chart.draw(view, options);
	       
	    	window.addEventListener('resize', function() {
	    		chart.draw(view, options);
	    	});
	    	
	    	var element = document.getElementById(div);
	    	element.config = function() {
	    		options = 
	    		{
	    				region: state.config.region,
	    	    		displayMode: state.config.displayMode,
	    	    		defaultColor: state.config.defaultColor,
	    	    		datalessRegionColor: state.config.datalessRegionColor,
	    	    		colorAxis: state.config.colorAxis,
	    	    		backgroundColor: state.config.backgroundColor
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

