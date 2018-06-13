
window.com_xdev_charts_combo_XdevComboChart = function() {
	var component = new google_combo_chart.XdevComboChart(this.getElement());
	
	var state = this.getState();
	var connector = this;
		
	var chart_div = this.getElement().getElementsByClassName("combo");
	
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
				vAxis: state.config.vAxis,
				hAxis: state.config.hAxis,
				legend: state.config.legend,
				seriesType: state.config.seriesType,
				series: state.series
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
	    	
	    	chart = new google.visualization.ComboChart(document.getElementById(div));		
	    	chart.draw(view, options);
	       
	        $('#' + div).resize(function(e) {
	        	chart.draw(view, options);
	        });
	        
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

	$('#' + chart_div[0].id).bind('refresh', function() {		
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

		chart.draw(view, options);
	});

	$('#' + chart_div[0].id).bind('config', function() {
		options = 
		{
				
		};

		chart.draw(view, options);
	});
	
	$('#' + chart_div[0].id).bind('printImage', function() {		
		connector.print_success(chart.getImageURI());
	});
}
