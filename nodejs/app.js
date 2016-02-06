var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var http = require('http').Server(app);

// use jade as the view engine 
app.engine('jade', require('jade').__express);
app.set('view engine','jade');
app.set('views', './views/');

// setup express to use request body parameters in json
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

// route the public/static files
app.use(express.static('./public'));

// serve the landing page 
app.get('/', function(req, res) { 
    res.render('index');
});

// load the REST API for 'records'
require('./routes/record')(app); 

// load the long operation route
require('./routes/longOperation')(app);

// start the server
var port = 3000;
if (process.env.PORT) {
    port = process.env.PORT;
}

http.listen(port, function () {
    console.log('listening on port ' + port);
});
