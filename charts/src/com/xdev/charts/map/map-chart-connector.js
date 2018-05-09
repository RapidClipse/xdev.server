
window.com_xdev_charts_map_XdevMapChart = function() {
	var component = new google_map_chart.XdevMapChart(this.getElement());
	
	var state = this.getState();
	var connector = this;
	
	var chart_div = this.getElement().getElementsByClassName("map");
	
	connector.divId(chart_div[0].id);

	var chart;
	var data;
	var view;
	var options;
	
	google.charts.load('current', {packages: ['map'], 'mapsApiKey' : state.mapsApiKey});
	google.charts.setOnLoadCallback(function(div, state, connector) {
		
		return function()
		{
			options = 
	    	{
					title: state.config.title,
					titleTextStyle: state.config.titleTextStyle,
					mapType: state.config.mapType
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
	    	
	    	chart = new google.visualization.Map(document.getElementById(div));		
	    	chart.draw(view, options);
	       
	        $('#' + div).resize(function(e) {
	        	chart.draw(view, options);
	        });
	        
	        google.visualization.events.addListener(chart, 'select', selectHandler);
		}
		
	}(chart_div[0].id, state, connector));

	function selectHandler() {
		var selection = chart.getSelection();
		
		console.log(selection);
		
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

	$('#' + chart_div[0].id).bind('refresh', function() {
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
	});

	$('#' + chart_div[0].id).bind('config', function() {
		options = 
		{
				title: state.config.title,
				titleTextStyle: state.config.titleTextStyle,
				mapType: state.config.mapType
		};

		chart.draw(view, options);
	});
	
	$('#' + chart_div[0].id).bind('printImage', function() {		
		connector.print_success(chart.getImageURI());
	});
}