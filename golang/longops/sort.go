package longops

import (
	"bufio"
	"os"
	"strconv"
)

var array []float64

func InitArray() {
	if array == nil {
		array = read_values("../values.txt")
	}
}

func LongSort() {
	BubbleSort(array)
}

func BubbleSort(a []float64) []float64 {
	for i := range a {
		for j := 1; j < len(a)-i; j++ {
			if a[j-1] < a[j] {
				temp := a[j-1]
				a[j-1] = a[j]
				a[j] = temp
			}
		}
	}
	return a
}

func read_values(path string) []float64 {
	inFile, _ := os.Open(path)
	defer inFile.Close()
	scanner := bufio.NewScanner(inFile)
	scanner.Split(bufio.ScanLines)

	var a []float64
	for scanner.Scan() {
		fl, err := strconv.ParseFloat(scanner.Text(), 32)
		if err == nil {
			a = append(a, fl)
		}
	}
	return a
}
