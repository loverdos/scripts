package main

import "os"
import "net/http"
import "fmt"

func main() {
    var port string

    switch len(os.Args) {
    case 2:
        port = os.Args[1]
    default:
        fmt.Printf("Usage: %s PORT\n", os.Args[0])
        os.Exit(2)
    }

    panic(http.ListenAndServe(fmt.Sprintf(":%s", port), http.FileServer(http.Dir("."))))
}