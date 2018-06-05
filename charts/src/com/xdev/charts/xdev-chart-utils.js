function translateToJSON(state, item, data) {
	var columnLength = state.dataTable.columns.length;
	
	var resultMap = new Map();
	
	for(cIndex = 0; cIndex < columnLength; cIndex++) {
		var colCaption = state.dataTable.columns[cIndex].id;
		var colIndex = state.dataTable.columns.map(function (icol) { return icol.id; }).indexOf(colCaption);
		var colType = state.dataTable.columns[colIndex].type;
		var colValue = data.getValue(item.row, colIndex);
		
		if(colCaption != "role")
		{
			resultMap.set(colCaption, colValue);
		}
	}
	
	return strMapToObj(resultMap);
}

function strMapToObj(strMap) {
    let obj = Object.create(null);
    for (let [k,v] of strMap) {
        obj[k] = v;
    }
    return obj;
}

function getAllIndexes(array, value) {
	var indexes = [], i;
    for(i = 0; i < array.length; i++)
        if (array[i] === value)
            indexes.push(i);
    return indexes;
}
