package routes

import "net/http"

type Route struct {
	Name        string
	Method      string
	Pattern     string
	HandlerFunc http.HandlerFunc
}

type Routes []Route

var routes = Routes{
	Route{
		"App Page",
		"GET",
		"/",
		Index,
	},
	Route{
		"Create Record",
		"PUT",
		"/record",
		PutRecord,
	},
	Route{
		"Read Record",
		"GET",
		"/record",
		GetRecord,
	},
	Route{
		"Update Record",
		"POST",
		"/record",
		PostRecord,
	},
	Route{
		"Delete Record",
		"DELETE",
		"/record",
		DeleteRecord,
	},
}
