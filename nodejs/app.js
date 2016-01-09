var app = require('express')();
var bodyParser = require('body-parser');
var http = require('http').Server(app);

// setup express to use request body parameters in json
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
 
// server the landing page 
app.get('/', function(req, res) { 
    res.send('hello world');
});

var port = 3000;
if (process.env.PORT) {
    port = process.env.PORT;
}
http.listen(port, function () {
    console.log('listening on port ' + port);
});
