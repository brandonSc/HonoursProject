package main

import (
	"log"
	"net/http"
	"os"
	//for extracting service credentials from VCAP_SERVICES
	"github.com/brandonSc/HonoursProject/golang/routes"
)

const (
	DEFAULT_PORT = "4000"
	DEFAULT_HOST = ""
)

func main() {
	// setup host and port for server
	var port string
	if port = os.Getenv("VCAP_APP_PORT"); len(port) == 0 {
		port = DEFAULT_PORT
	}

	var host string
	if host = os.Getenv("VCAP_APP_HOST"); len(host) == 0 {
		host = DEFAULT_HOST
	}

	// setup the database
	//	cadb.Init()

	// grab the router and request handlers
	router := route.NewRouter()

	// launch the server
	log.Printf("Starting app on %+v:%+v\n", host, port)
	log.Fatal(http.ListenAndServe(host+":"+port, router))
}
