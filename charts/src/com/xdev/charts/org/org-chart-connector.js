
window.com_xdev_charts_org_XdevOrgChart = function() {
	var component = new google_org_chart.XdevOrgChart(this.getElement());
	
	var state = this.getState();
	var connector = this;
	
	var chart_div = this.getElement().getElementsByClassName("org");
	
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
	
	google.charts.load('current', {packages: ['orgchart']});
	google.charts.setOnLoadCallback(function(div, state, connector) {
		
		return function()
		{
			chart = new google.visualization.OrgChart(document.getElementById(div));
			
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
					allowHtml: state.config.allowHtml,
					allowCollapse: state.config.allowCollapse,
					size: state.config.size
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
			
			if (item.row != null)
			{
				var json = translateToJSON(state, item, data);
				connector.select(json);
			}
		}
	}
}