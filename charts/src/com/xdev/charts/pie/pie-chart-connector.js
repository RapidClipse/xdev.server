
window.com_xdev_charts_pie_XdevPieChart = function() {
	var component = new google_pie_chart.XdevPieChart(this.getElement());
	
	var state = this.getState();
	var connector = this;
	
	var chart_div = this.getElement().getElementsByClassName("pie");
	
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
					is3D: state.config.is3D,
					pieHole: state.config.pieHole,
					backgroundColor: state.config.backgroundColor,
					fontName: state.config.fontName,
					fontSize: state.config.fontSize,
					pieSliceText: state.config.pieSliceText,
					pieSliceTextStyle: state.config.pieSliceTextStyle,
					legend: state.config.legend,
					chartArea: state.config.chartArea,
					pieSliceBorderColor: state.config.pieSliceBorderColor,
					sliceVisibilityThreshold: state.config.sliceVisibilityThreshold,
					pieResidueSliceColor: state.config.pieResidueSliceColor,
					pieResidueSliceLabel: state.config.pieResidueSliceLabel
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
	    	
	    	chart = new google.visualization.PieChart(document.getElementById(div));		
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
		
			if (item.row != null)
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
				is3D: state.config.is3D,
				pieHole: state.config.pieHole,
				backgroundColor: state.config.backgroundColor,
				fontName: state.config.fontName,
				fontSize: state.config.fontSize,
				pieSliceText: state.config.pieSliceText,
				pieSliceTextStyle: state.config.pieSliceTextStyle,
				legend: state.config.legend,
				chartArea: state.config.chartArea,
				pieSliceBorderColor: state.config.pieSliceBorderColor,
				sliceVisibilityThreshold: state.config.sliceVisibilityThreshold,
				pieResidueSliceColor: state.config.pieResidueSliceColor,
				pieResidueSliceLabel: state.config.pieResidueSliceLabel
		};

		chart.draw(view, options);
	});
	
	$('#' + chart_div[0].id).bind('printImage', function() {		
		connector.print_success(chart.getImageURI());
	});
}