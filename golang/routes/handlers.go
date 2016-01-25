package routes

import (
	"bytes"
	"encoding/json"
	"fmt"
	"html/template"
	"net/http"
	"net/url"
	"strings"

	"github.com/brandonSc/HonoursProject/golang/models"
	"hub.jazz.net/git/schurman93/Git-Monitor/cadb"
)

const viewDir = "../views/"

//
// for capturing the JSON response from cloudant
//
type Response struct {
	Docs []models.Record `json:"docs"`
}

//
// serve the default landling page for the route `/`
//
func Index(w http.ResponseWriter, r *http.Request) {
	var index = template.Must(template.ParseFiles(
		"views/index.html",
	))
	index.Execute(w, nil)
}

//
// GET /record
// look up a record by name
// @param name
//
func GetRecord(w http.ResponseWriter, r *http.Request) {
	// get name from request
	name, err := url.QueryUnescape(r.URL.Query().Get("name"))
	if err != nil {
		//fmt.Printf("error parsing url paramater: routes.GetRecord: %s", err)
		return
	}
	name = strings.Trim(name, " ")
	// send cloudant requet for record
	res, err := find_record(name)
	// parse from json
	if err != nil {
		//fmt.Printf("error reading cloudant json, routes.GetRecord: %s\n", err)
		w.WriteHeader(500)
		fmt.Fprint(w, `{"error":"unable to read cloudant json response"}`)
		return
	}
	buf := new(bytes.Buffer)
	buf.ReadFrom(res.Body)
	js := buf.String()
	var response Response
	err = json.Unmarshal([]byte(js), &response)
	// check if a document was found in db
	if len(response.Docs) > 0 {
		str, err := json.Marshal(response.Docs[0])
		if err != nil {
			//fmt.Printf("error converting documents to json, routes.GetRecord: %s\n", err)
			w.WriteHeader(500)
			fmt.Fprint(w, `{"error":"unable to form json response"}`)
		} else {
			// send the doc
			fmt.Fprint(w, string(str))
		}
	} else {
		// no docs found, send 404
		w.WriteHeader(404)
		s := fmt.Sprintf(`{"error":"no Record found with name: ` + name + `"}`)
		fmt.Fprint(w, s)
	}
}

//
// POST /record
// update an existing record with a new value
// @param name
// @param value
//
func PostRecord(w http.ResponseWriter, r *http.Request) {
	// get name and value from request
	decoder := json.NewDecoder(r.Body)
	var f map[string]interface{}
	decoder.Decode(&f)
	name := f["name"].(string)
	value := f["value"].(string)
	name = strings.Trim(name, " ")
	value = strings.Trim(value, " ")
	// request docs with the same name from cloudant
	res, err := find_record(name)
	if err != nil {
		w.WriteHeader(500)
		//fmt.Printf("error reading from cloudant, routes.GetRecord: %s\n", err)
		return
	}
	// parse response from json
	buf := new(bytes.Buffer)
	buf.ReadFrom(res.Body)
	js := buf.String()
	var response Response
	err = json.Unmarshal([]byte(js), &response)
	if err != nil {
		w.WriteHeader(500)
		estr := fmt.Sprintf("error converting documents to json, routes.GetRecord: %s\n", err)
		fmt.Fprint(w, estr)
		//fmt.Println(estr)
		return
	}
	// check if docs matching name are found
	if len(response.Docs) > 0 {
		// doc found, now update it
		var f map[string]interface{}
		json.Unmarshal([]byte(js), &f)
		f["value"] = value
		rec, err := json.Marshal(f)
		if err != nil {
			w.WriteHeader(500)
			fmt.Fprint(w, `{"error":"unable to convert data to json"}`)
			//fmt.Printf("error reading from cloudant, routes.GetRecord: %s\n", err)
			return
		}
		//fmt.Println(rec)
		res, err := cadb.Post("records-nodejs", string(rec), "")
		if err != nil {
			w.WriteHeader(500)
			fmt.Fprint(w, `{"error":"unable to update Record on cloudant"}`)
			//fmt.Printf("error reading from cloudant, routes.GetRecord: %s\n", err)
			return
		}
		fmt.Fprint(w, res)
	} else {
		w.WriteHeader(404)
		//fmt.Println("no records found that match name=" + name)
		fmt.Fprint(w, `{"error":"no Record found with name: `+name+`"}`)
	}
}

//
// PUT /record
// add a new record to the database under a new unique name
// @param name
// @parma value
//
func PutRecord(w http.ResponseWriter, r *http.Request) {
	// get name and value from request
	decoder := json.NewDecoder(r.Body)
	var f map[string]interface{}
	decoder.Decode(&f)
	name := f["name"].(string)
	value := f["value"].(string)
	name = strings.Trim(name, " ")
	value = strings.Trim(value, " ")
	// request docs with the same name from cloudant
	res, err := find_record(name)
	if err != nil {
		w.WriteHeader(500)
		//fmt.Printf("error reading from cloudant, routes.GetRecord: %s\n", err)
		return
	}
	// parse response from json
	buf := new(bytes.Buffer)
	buf.ReadFrom(res.Body)
	js := buf.String()
	var response Response
	err = json.Unmarshal([]byte(js), &response)
	if err != nil {
		w.WriteHeader(500)
		estr := fmt.Sprintf("error converting documents to json, routes.GetRecord: %s\n", err)
		fmt.Fprint(w, estr)
		//fmt.Println(estr)
		return
	}
	// check if docs matching name are found
	if len(response.Docs) == 0 {
		record := `{"name":"` + name + `","value":"` + value + `"}`
		// create new record in cloudant
		res, err := cadb.Post("records-nodejs", record, "")
		if err != nil {
			w.WriteHeader(500)
			//fmt.Println("error creating record which seemed to be unique")
			fmt.Fprint(w, `{"error":"error creating record which seemed to be unique"}`)
		}
		//fmt.Println(res)
		fmt.Fprint(w, res)
	} else {
		w.WriteHeader(400)
		//fmt.Println("a record already exists with name=" + name)
		fmt.Fprint(w, `{"error":"a Record already exists with name: '`+name+`'"}`)
	}
}

//
// DELETE /record
// update an existing record with a new value
// @param name
// @param value
//
func DeleteRecord(w http.ResponseWriter, r *http.Request) {
	// get name from request
	name, err := url.QueryUnescape(r.URL.Query().Get("name"))
	if err != nil {
		//fmt.Printf("error parsing url paramater: routes.GetRecord: %s", err)
		return
	}
	name = strings.Trim(name, " ")
	// request docs with the same name from cloudant
	res, err := find_record(name)
	if err != nil {
		w.WriteHeader(500)
		//fmt.Printf("error reading from cloudant, routes.GetRecord: %s\n", err)
		return
	}
	// parse response from json
	buf := new(bytes.Buffer)
	buf.ReadFrom(res.Body)
	js := buf.String()
	var response Response
	err = json.Unmarshal([]byte(js), &response)
	if err != nil {
		w.WriteHeader(500)
		estr := fmt.Sprintf("routes.DeleteRecord, error: converting documents to json  %s\n", err)
		fmt.Fprint(w, estr)
		//fmt.Println(estr)
		return
	}
	// check if docs matching name are found
	if len(response.Docs) > 0 {
		record := response.Docs[0]
		res, err := cadb.Delete("records-nodejs/"+record.Id+"?rev="+record.Rev, "", "")
		if err != nil {
			w.WriteHeader(500)
			fmt.Fprint(w, `{"error":"unable to update Record on cloudant"}`)
			//fmt.Printf("error reading from cloudant, routes.GetRecord: %s\n", err)
			return
		}
		fmt.Fprint(w, res)
	} else {
		w.WriteHeader(404)
		//fmt.Println("no records found that match name=" + name)
		fmt.Fprint(w, `{"error":"no Record found with name: `+name+`"}`)
	}
}

//
// look up a record in cloudant with the provided name
//
func find_record(name string) (*http.Response, error) {
	selector := `{
		"selector": {
			"_id": {
				"$gt": 0
			}, 
			"name": "` + name + `" 
		}
	}`
	return cadb.Post("records-nodejs", selector, "_find")
}
