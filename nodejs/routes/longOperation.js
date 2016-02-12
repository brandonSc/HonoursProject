var LineReader = require('line-by-line');

module.exports = function(app) {
	
	var array = new Array();
	lr = new LineReader('../values.txt');

	lr.on('error', function (err) {
		console.log('error reading values.txt: '+err);
	});

	lr.on('line', function (line) {
		array.push(Number(line));
	});

	lr.on('end', function () {
		console.log('array initialized');
	});
    
    app.get('/long-operation', function(req, res) { 
	    //console.log(factor(largeNumber));
        bubbleSort(array);
        res.header('Content-Type', 'application/json');
        res.send('{"message":"done"}');
    });
    
    function bubbleSort(a) {
        for ( i=0; i<a.length; i++ ) {
            for ( j=1; j<a.length-i; j++ ) { 
                if ( a[j-1] < a[j] ) { 
                    var temp = a[j-1];
                    a[j-1] = a[j];
                    a[j] = temp;
                }
            }
        }
        return a;
    }
}
