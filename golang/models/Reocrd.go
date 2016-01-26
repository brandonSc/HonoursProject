package models

type Record struct {
	Name  string `json:"name"`
	Value string `json:"value"`
	Id    string `json:"_id"`
	Rev   string `json:"_rev"`
}
