
window.com_xdev_charts_gantt_XdevGanttChart = function() {
	var component = new google_gantt_chart.XdevGanttChart(this.getElement());

	var state = this.getState();
	var connector = this;
	var chart_div = this.getElement().getElementsByClassName("gantt");
	
	connector.divId(chart_div[0].id);
	
	var chart;
	var data;
	var view;
	var options;
		
	google.charts.load('current', {packages: ['gantt']});
	google.charts.setOnLoadCallback(function(div, state, connector) {
		
		return function()
		{
			options = 
	    	{
					backgroundColor: state.config.backgroundColor,
					gantt: state.config.gantt
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
	    	
			chart = new google.visualization.Gantt(document.getElementById(div));		
	    	chart.draw(view, options);
			
	    	window.addEventListener('resize', function() {
	    		chart.draw(view, options);
	    	});
	    	
	    	var element = document.getElementById(div);
	    	element.config = function() {
	    		options = 
	    		{
	    				backgroundColor: state.config.backgroundColor,
						gantt: state.config.gantt
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
	    			
	    		var viewRefresh = new google.visualization.DataView(data);
	    		var index = state.dataTable.columns.map(function (icol) { return icol.id; }).indexOf('id');
	    		
	    		if(index >= 0)
	    		{
	    			view.hideColumns([index]);
	    		}

	    		chart.draw(viewRefresh, options);
	    	};
	    	
	    	element.printImage = function() {
	    		connector.print_success(chart.getImageURI());
	    	};

	        google.visualization.events.addListener(chart, 'select', selectHandler);
		}
		
	}(chart_div[0].id, state, connector));

	function selectHandler() {
		var selection = chart.getSelection();
		console.log(selection);
		for (var i = 0; i < selection.length; i++)
		{
			var item = selection[i];
			console.log(item);
			if (item.row != null)
			{
				var json = translateToJSON(state, item, data);
				connector.select(json);
			}
		}
	}
}