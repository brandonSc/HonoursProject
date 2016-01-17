var request = require('request');
var validator = require('validator');

module.exports = function(app) {

    // set cloudant database URL and credentials 
    var db = 'https://0a6f8059-22b3-4136-9e7c-9fbcb7b4579d-bluemix.cloudant.com';
    var dbUser = '0a6f8059-22b3-4136-9e7c-9fbcb7b4579d-bluemix';
    var dbPass = '***REMOVED***';

    /**
     * retrieve an existing record by name
     * @param name 
     */
    app.get('/record', function(req, res) {
        res.header('Content-Type', 'application/json');
        var name = req.query.name;
        if ( validator.trim(name) == '' ) {
            res.status(400).send('{"error": "name is required"}');
            return;
        }

        findRecord(name, function(error, response, body) {
            if ( error ) { 
                res.stauts(500).send(error);
                console.log(error);
            } else { 
                var docs = JSON.parse(body).docs;
                res.send(docs[0]);
            }
        });
    });

    /**
     * update an existing record's value
     * @param name
     * @param value 
     */
    app.post('/record', function(req, res) { 
        res.header('Content-Type', 'application/json');
        var name = req.body.name;
        var value = req.body.value;
        if ( validator.trim(name) == '' ) {
            res.status(400).send('{"error": "name is required"}');
            return;
        }

        findRecord(name, function(error, response, body) {
            if ( error ) {
                res.status(500).send(error);
                console.log(error);
            } else { 
                var docs = JSON.parse(body).docs;
                if ( docs.length == 0 ) {
                    res.status(404).send('{"error": "No Record with the name \''+name+'\' was found"}');
                } else {
                    var record = docs[0];
                    record.name = name;
                    record.value = value;
                    updateRecord(record, function(error, response, body) {
                        if ( error ) {
                            res.status(500).send(error); 
                            console.log(error);
                        } else {
                            res.send(body);
                        }
                    });
                }
            }
        });
    });

    /** 
     * create a new record
     * @param name
     * @param value 
     */
    app.put('/record', function(req, res) {
        res.header('Content-Type', 'application/json');
        var name = req.body.name;
        var value = req.body.value;
        if ( validator.trim(name) == '' ) {
            res.status(400).send('{"error": "name is required"}');
            return;
        }

        findRecord(name, function(error, response, body) {
            if ( error ) {
                res.status(500).send(error);
                console.log(error);
            } else { 
                var docs = JSON.parse(body).docs;
                if ( docs && docs.length > 0 ) {
                    res.status(404).send('{"error": "A Record with the name \''+name+'\' already exists"}');
                } else {
                    createRecord(name, value, function(error, response, body) {
                        if ( error ) {
                            res.status(500).send(error); 
                            console.log(error);
                        } else {
                            res.send(body);
                        }
                    });
                }
            }
        });
    });

    /**
     * delete an existing record by name
     * @param name 
     */
    app.delete('/record', function(req, res) {
        res.header('Content-Type', 'application/json');
        var name = req.query.name;
        if ( validator.trim(name) == '' ) {
            res.status(400).send('{"error": "name is required"}');
            return;
        }
        
        findRecord(name, function(error, response, body) {
            if ( error ) {
                res.status(500).send(error);
                console.log(error);
            } else { 
                var docs = JSON.parse(body).docs;
                if ( docs.length == 0 ) {
                    res.status(404).send('{"error": "No Record with the name \''+name+'\' was found"}');
                } else {
                    var record = docs[0];
                    deleteRecord(record, function(error, response, body) {
                        if ( error ) {
                            res.status(500).send(error); 
                            console.log(error);
                        } else {
                            res.send(body);
                        }
                    });
                }
            }
        });
    });

    
    
    /**
     * make a call to cloudant for all documents 
     * matching the given name
     * callback takes a request error, response, and body
     */
    function findRecord(name, callback) {
        var options = {
            url: db+'/records-nodejs/_find',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify({ 
                selector: {
                    _id: { $gt: 0 },
                    name: name,
                }
            })
        };
        request.post(options, callback).auth(dbUser, dbPass);
    };

    /**
     * delete a record from cloudant 
     * callback(error, response, body)
     */ 
    function deleteRecord(record, callback) { 
        var options = {
            url: db+'/records-nodejs/'+record._id+'?rev='+record._rev,
            headers: { 'content-type': 'application/json' },
        };
        request.del(options, callback).auth(dbUser, dbPass);
    };

    /**
     * update a cloudant record
     * callback(error, response, body)
     */
    function updateRecord(record, callback) {
        var options = {
            url: db+'/records-nodejs',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify(record)
        };
        request.post(options, callback).auth(dbUser, dbPass);        
    };

    /**
     * create a new cloudant record with the name and value
     * callback(error, response, body)
     */
    function createRecord(name, value, callback) { 
        var options = {
            url: db+'/records-nodejs',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify({ name: name, value: value })
        };
        request.post(options, callback).auth(dbUser, dbPass);
    };
}
