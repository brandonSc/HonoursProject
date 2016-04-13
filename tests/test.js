var request = require('request');
var sleep = require('sleep');

var numTests = 50; // number of requests to send
var interval = 100; // how often in millis to send requesti
var url = "http://localhost:8080/comp3601/webapi/long-operation";


var responseTimes = new Array(numTests);
var numDone = 0;

for ( var i=0; i<numTests; i++ ) {
    runTrial(i);
}

function runTrial(i) { 
    setTimeout(function() { 
        testGet(i, "test");  
    }, (i * interval));
}

function testGet(i, name) {
    var startTime = new Date();
    request(url, function(err, req, res) {
        if ( err ) {
            console.log('error '+err);
            return;
        }
        responseTimes[i] = new Date() - startTime;
        numDone++;
        console.log(i+": "+responseTimes[i]+" - "+res);

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
