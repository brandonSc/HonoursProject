var request = require('request');

var numTests = 1000;
var interval = 1000; 
var responseTimes = new Array(numTests);
var numDone = 0;

for ( var i=0; i<numTests; i++ ) {
    testGet(i, "test");
}

function testGet(i, name) {
    var url = 'http://localhost:4000/record?name='+name
    var startTime = new Date();
    request(url, function(err, req, res) {
        if ( err ) {
            console.log('error '+err);
            return;
        }
        responseTimes[i] = new Date() - startTime;
        numDone++;
        //console.log(responseTimes[i]+" - "+res);

        if ( numDone == numTests ) 
            printStats();
    });
}

function printStats() { 
    var total = 0;
    for ( var i=0; i<numTests; i++ ) {
        total += responseTimes[i];
    }
    console.log('\nAvg Resp. Time = '+(total/numTests));
}
