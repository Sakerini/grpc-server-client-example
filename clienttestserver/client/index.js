const express = require("express");
const fs = require("fs");
const clientStub = require("./clientStub");
const bodyParser = require('body-parser');

const app = express();


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

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log("Server running at port %d", PORT);
});