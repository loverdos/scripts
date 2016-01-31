package main

import "fmt"
import "os"
import "strings"
import "regexp"

func Exists(name string) bool {
	_, err := os.Stat(name)
	return err == nil
}

func findIn(folderName string, cmdre string) (files []string) {
	if !Exists(folderName) {
		return []string{}
	}

	f, err := os.Open(folderName)
	if err != nil {
		return []string{}
	}
	defer f.Close()

	children, err := f.Readdir(-1) // read all children
	for _, child := range children {
		childName := child.Name()
		if matched, _ := regexp.MatchString(cmdre, childName); matched {
			files = append(files, child.Name())
		}
	}

	return files
}

func main() {
	// for _, arg := range os.Args {
	//     fmt.Printf("%s\n", arg)
	// }

	if len(os.Args) != 2 {
		fmt.Printf("Usage: %s REGEXP\n", os.Args[0])
		os.Exit(2)
	}

	cmdre := os.Args[1]

	path := os.Getenv("PATH")
	folders := strings.Split(path, ":")

	for _, folder := range folders {
		files := findIn(folder, cmdre)

		if len(files) > 0 {
			fmt.Printf("==> %s\n", folder)
			for _, file := range files {
				fmt.Printf(" %s", file)
			}
			fmt.Println()
		}
	}
}
