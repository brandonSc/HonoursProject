<html>

<head>
    <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="/comp3601/materialize/css/materialize.min.css">
    <link rel="stylesheet" href="/comp3601/style.css">
    <script type="text/javascript" src="/comp3601/jquery-1.12.0.min.js"></script>
    <script type="text/javascript" src="/comp3601/materialize/js/materialize.min.js"></script>
    <title>Performance Test (Java)</title>
</head>
<body>
	<nav class="top-nav amber hide-on-med-and-down">
      <div class="nav-wrapper"><a href="/" style="padding-left:15px" class="brand-logo">Performance Test (Java)</a><a href="#" data-activates="nav-mobile-collapsed" class="button-collapse"><i class="material-icons">menu</i></a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="http://localhost:3000">Node.Js</a></li>
                <li><a href="http://localhost:4000">Golang</a></li>
                <li><a href="/about">About</a></li>
        </ul>
      </div>
    </nav>
    <nav style="height:100px" class="top-nav amber hide-on-large-only">
      <div style="padding-top:20px" class="nav-wrapper"><a href="/" style="font-size:250%" class="brand-logo">Performance Test (Java)</a><a href="#" data-activates="nav-mobile-collapsed" class="button-collapse"><i style="font-size:350%" class="material-icons">menu</i></a>
        <ul id="nav-mobile-collapsed" class="side-nav">
                <li><a href="http://localhost:3000">Node.Js</a></li>
                <li><a href="http://localhost:4000/">Golang</a></li>
                <li><a href="/about">About</a></li>
        </ul>
      </div>
    </nav>
    <div class="container">
        <br>
        <h4>Overview</h4>
        <p>This page runs a response-time performance test on a Node.Js server. The test involves running some basic operations as well as a computationally heavy operation. The basic operations include <i>creating</i>, <i>reading</i>, <i>updating</i> and <i>deleting</i> Record objects. A Record is a simple <i>name-value</i> pair, such as <i>FavouriteColor-Red</i> or <i>CourseCode-COMP4905</i>. The complex operation is intended to be long running (about a 10 seconds). It invokes an arbitrary mathemetical operation on the server. All response times are logged in a console at the end of the page. Compare your results against the same application running on a Java server, or running on a Go server!</p>
        <br>
        <div class="row">
            <div class="col s12 m6">
                <div style="padding:15px" class="card hoverable"><span class="card-title">Record </span>
                    <div id="record_name_row" class="row">
                        <div class="input-field col s12">
                            <input id="record_name" type="text" class="validate">
                            <label for="record_name" class="active">Name</label>
                        </div>
                    </div>
                    <div id="record_value_row" class="row">
                        <div class="input-field col s12">
                            <input id="record_value" type="text" class="validate">
                            <label for="record_value" class="active">Value</label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col s12 m6">
                <div class="row">
                    <div style="padding-left:15px;padding-top:15px" class="col">
                        <div id="create_record_row" class="row center"><a id="create_button" style="width:120px" class="waves-effect waves-light btn-large amber">Create</a></div>
                        <div class="row center"><a id="update_button" style="width:120px" class="waves-effect waves-light btn-large amber">Update</a></div>
                    </div>
                    <div style="padding-left:15px;padding-top:15px" class="col">
                        <div id="create_record_row" class="row center"><a id="read_button" style="width:120px" class="waves-effect waves-light btn-large amber">Read</a></div>
                        <div class="row center"><a id="delete_button" style="width:120px" class="waves-effect waves-light btn-large amber">Delete</a></div>
                    </div>
                </div>
                <div style="padding-left:5px" class="row"><a id="long_operation" style="width:240px" class="waves-effect waves-light btn-large red">Run Long Operation</a></div>
            </div>
        </div>
        <div id="console" class="scrollbox"></div>
        <br>
    </div>
</body>

</html>
<script>
    $(document).ready(function() {
        $(".button-collapse").sideNav();

        $("#create_button").click(function() {
            var startTime = new Date();
            var name = document.getElementById('record_name').value;
            var value = document.getElementById('record_value').value;

            function callback(response) {
                if ( response.error ) 
                    return error(response)
                var text = printObj(response);
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printOut("CREATE", time + " ms - " + text);
            }

            function error(response) {
                console.log(response);
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printErr("CREATE", time + " ms - " + printObj(response));
            }
            $.ajax({
                url: '/comp3601/webapi/record?name='+name+'&value='+value,
                type: 'PUT',
                success: callback,
                error: error
            });
        });

        $("#read_button").click(function() {
            var startTime = new Date();

            function callback(response) {
                if ( response.error ) 
                    return error(response)
                var text = printObj(response);
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printOut("READ", time + " ms - " + text);
                document.getElementById('record_value').value = response.value;
                $('#record_value').focus();
            }

            function error(response) {
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printErr("READ", time + " ms - " + printObj(response));
            }
            var name = document.getElementById('record_name').value;
            $.ajax({
                url: '/comp3601/webapi/record?name=' + name,
                type: 'GET',
                success: callback,
                error: error
            });
        });

        $("#update_button").click(function() {
            var startTime = new Date();

            function callback(response) {
                if ( response.error ) 
                    return error(response)
                var text = printObj(response);
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printOut("UPDATE", time + " ms - " + text);
            }

            function error(response) {
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printErr("UPDATE", time + " ms - " + printObj(response));
            }
            var name = document.getElementById('record_name').value;
            var value = document.getElementById('record_value').value;
            $.ajax({
                url: '/comp3601/webapi/record',
                type: 'POST',
                success: callback,
                data: JSON.stringify({
                    name: name,
                    value: value
                }),
                error: error
            });
        });

        $("#delete_button").click(function() {
            var startTime = new Date();

            function callback(response) {
                if ( response.error ) 
                    return error(response);
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printOut("DELETE", time + " ms - " + response);
                document.getElementById("record_name").value = "";
                document.getElementById("record_value").value = "";
                $('#record_name').focus();
                $('#record_value').focus();
            }

            function error(response) {
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printErr("DELETE", time + " ms - " + printObj(response));
            }
            var name = document.getElementById('record_name').value;
            $.ajax({
                url: '/comp3601/webapi/record?name=' + name,
                type: 'DELETE',
                success: callback,
                error: error
            });
        });
        $("#long_operation").click(function() {
            var startTime = new Date();

            function callback(response) {
                if ( response.error) 
                    return error(response);
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printOut("LONG_OP", time + " ms - " + response);
            }

            function error(response) {
                var endTime = new Date();
                var time = endTime.getTime() - startTime.getTime();
                printErr("LONG_OP", time + " ms - " + printObj(response));
            }
            var name = document.getElementById('record_name').value;
            var value = document.getElementById('record_value').value;
            $.ajax({
                url: '/comp3601/webapi/long-operation',
                type: 'GET',
                success: callback,
                data: JSON.stringify({
                    name: name,
                    value: value
                }),
                error: error
            });
        });
    });

    function printOut(tag, text) {
        var field = document.getElementById("console");
        var str = field.innerHTML;
        var time = new Date();
        str = "<nobr>[" + tag + " " + time + "] " + text + "<nobr><br>" + str;
        field.innerHTML = str;
    }

    function printErr(tag, text) {
        var field = document.getElementById("console");
        var str = field.innerHTML;
        var time = new Date();
        str = "<nobr><font color='#ff4d4d'>[" + tag + " " + time + "] " + text + "</font></nobr><br>" + str;
        field.innerHTML = str;
    }

    function printObj(obj) {
        var str = JSON.stringify(obj);
        str.replace("\\", "");
        return str;
    }
</script>
