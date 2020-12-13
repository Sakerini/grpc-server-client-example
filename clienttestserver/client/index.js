const express = require("express");
const fs = require("fs");
const clientStub = require("./clientStub");
const bodyParser = require('body-parser');

const app = express();
var _ = require('lodash');
var async = require('async');


app.get("/", function(request, response) {
    if (fs.existsSync("view/input_form.html")) {
        const contentString = fs.readFileSync("view/input_form.html", "utf8");
        response.end(contentString);
    } else {
        console.log("BAD html")
        response.end("PROBLEMS");
    }
});

app.get("/login", function(request, response) {
    let AccountInformation = {
        username: request.query.username,
        password: request.query.password
    };

    clientStub.login(AccountInformation, (err, data) => {
        if (err) throw err;

        if (data.status == "WRONG") {
            console.log("login not successful");
            response.end("fail")
        } else {
            console.log("Succes login");
            response.end("success")
        }
    });
});

//Client side stream -- sends 10 numbers to server
app.get("/sendNumbersStream", function(request, response) { 

    // get call
    var call = clientStub.sendNumbers(function(error, status) {
        if (error) {
            // returned error
            console.log("error");
            response.end("fail to send numbers")
        } else {
            // everything recieved
            console.log("numbers sent")
            response.end("numbers sent")
        }
    });

    // sendNumber callback
    function sendNumber(number) {
        return function(callback) {
            console.log("sendingNumber" + number);
            call.write({
                number: number
            });

            //a delay before sending next callback
            _.delay(callback, _.random(500, 1500));
        }
    }

    var sendCallBackArray = []
    for (var i = 0; i < 10; i++) {
        sendCallBackArray[i] = sendNumber(i);
    }

    // streaming
    async.series(sendCallBackArray, function() {
        call.end();
    });
});

//Server side stream -- server sends numbers to client
app.get("/getNumbers", function(request, response) { 
    let numbers = {
        number: 5
    }

    var call = clientStub.getNumbers(numbers);
    call.on('data', function(number) {
        console.log(number.number)
    });

    call.on('end', function(){
        console.log('The server has finished sending')
    })

    call.on('error', function(e){
        console.log(e)
        //error happened
    })

    call.on('stats', function(status) {
        console.log(status)
        //process status
    })
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log("Server running at port %d", PORT);
});