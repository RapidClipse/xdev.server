

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
