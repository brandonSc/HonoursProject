var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var http = require('http').Server(app);
var request = require('request');

// use jade as the view engine 
app.engine('jade', require('jade').__express);
app.set('view engine','jade');
app.set('views', './views/');

// setup express to use request body parameters in json
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

// route the public/static files
app.use(express.static('./public'));

// set cloudant database URL and credentials 
var db = 'https://0a6f8059-22b3-4136-9e7c-9fbcb7b4579d-bluemix.cloudant.com';
var username = '0a6f8059-22b3-4136-9e7c-9fbcb7b4579d-bluemix';
var password = '***REMOVED***';

request.get(db+'/test', function(err, res, body) {
    console.log(body);
}).auth(username, password);

// serve the landing page 
app.get('/', function(req, res) { 
    res.render('index');
});

app.get('/records', function(req, res) { 
});

app.post('/records', function(req, res) { 
});

var port = 3000;
if (process.env.PORT) {
    port = process.env.PORT;
}

http.listen(port, function () {
    console.log('listening on port ' + port);
});
