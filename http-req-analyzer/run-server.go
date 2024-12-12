package main

import (
    "crypto/tls"
    "fmt"
    "log"
    "net/http"
    "strings"
)

// Map TLS versions to human readable strings
var tlsVersions = map[uint16]string{
    0x0304: "TLS 1.3",
    0x0303: "TLS 1.2",
    0x0302: "TLS 1.1",
    0x0301: "TLS 1.0",
    // GREASE values
    0x0a0a: "GREASE (0x0a0a)",
    0x1a1a: "GREASE (0x1a1a)",
    0x2a2a: "GREASE (0x2a2a)",
    0x3a3a: "GREASE (0x3a3a)",
    0x4a4a: "GREASE (0x4a4a)",
    0x5a5a: "GREASE (0x5a5a)",
    0x6a6a: "GREASE (0x6a6a)",
    0x7a7a: "GREASE (0x7a7a)",
    0x8a8a: "GREASE (0x8a8a)",
    0x9a9a: "GREASE (0x9a9a)",
    0xaaaa: "GREASE (0xaaaa)",
    0xbaba: "GREASE (0xbaba)",
    0xcaca: "GREASE (0xcaca)",
    0xdada: "GREASE (0xdada)",
    0xeaea: "GREASE (0xeaea)",
    0xfafa: "GREASE (0xfafa)",
}

// Map cipher suite IDs to their names
var cipherSuites = map[uint16]string{
    // TLS 1.3 cipher suites
    0x1301: "TLS_AES_128_GCM_SHA256",
    0x1302: "TLS_AES_256_GCM_SHA384",
    0x1303: "TLS_CHACHA20_POLY1305_SHA256",

    // TLS 1.2 and below cipher suites
    0x0005: "TLS_RSA_WITH_RC4_128_SHA",
    0x000a: "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
    0x002f: "TLS_RSA_WITH_AES_128_CBC_SHA",
    0x0035: "TLS_RSA_WITH_AES_256_CBC_SHA",
    0x003c: "TLS_RSA_WITH_AES_128_CBC_SHA256",
    0x003d: "TLS_RSA_WITH_AES_256_CBC_SHA256",
    0x009c: "TLS_RSA_WITH_AES_128_GCM_SHA256",
    0x009d: "TLS_RSA_WITH_AES_256_GCM_SHA384",
    0x0033: "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
    0x0039: "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
    0x0067: "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256",
    0x006b: "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256",
    0x009e: "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
    0x009f: "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",
    0xc009: "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
    0xc00a: "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
    0xc013: "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
    0xc014: "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
    0xc023: "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
    0xc024: "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
    0xc027: "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
    0xc028: "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
    0xc02b: "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
    0xc02c: "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
    0xc02f: "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
    0xc030: "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
    0xcca8: "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
    0xcca9: "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256",
    0xccaa: "TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
    0xff00: "TLS_FALLBACK_SCSV",
    0x00ff: "TLS_EMPTY_RENEGOTIATION_INFO_SCSV",

    // GREASE values
    0x0a0a: "GREASE (0x0a0a)",
    0x1a1a: "GREASE (0x1a1a)",
    0x2a2a: "GREASE (0x2a2a)",
    0x3a3a: "GREASE (0x3a3a)",
    0x4a4a: "GREASE (0x4a4a)",
    0x5a5a: "GREASE (0x5a5a)",
    0x6a6a: "GREASE (0x6a6a)",
    0x7a7a: "GREASE (0x7a7a)",
    0x8a8a: "GREASE (0x8a8a)",
    0x9a9a: "GREASE (0x9a9a)",
    0xaaaa: "GREASE (0xaaaa)",
    0xbaba: "GREASE (0xbaba)",
    0xcaca: "GREASE (0xcaca)",
    0xdada: "GREASE (0xdada)",
    0xeaea: "GREASE (0xeaea)",
    0xfafa: "GREASE (0xfafa)",
}

func handleRequest(w http.ResponseWriter, r *http.Request) {
    fmt.Println("\n=== HTTP Request Details ===")
    fmt.Printf("Method: %s\n", r.Method)
    fmt.Printf("URL: %s\n", r.URL)
    fmt.Printf("Protocol: %s\n", r.Proto)
    fmt.Printf("Remote Address: %s\n", r.RemoteAddr)

    fmt.Println("\nHeaders:")
    for name, values := range r.Header {
        fmt.Printf("  %s: %s\n", name, strings.Join(values, ", "))
    }

    if r.TLS != nil {
        fmt.Println("\nTLS Connection Details:")
        fmt.Printf("  TLS Version: %X\n", r.TLS.Version)
        fmt.Printf("  Cipher Suite: %X\n", r.TLS.CipherSuite)
        fmt.Printf("  Server Name (SNI): %s\n", r.TLS.ServerName)
        if len(r.TLS.PeerCertificates) > 0 {
            fmt.Printf("  Client Certificate: %v\n", r.TLS.PeerCertificates[0].Subject)
        }
    }

    fmt.Println("=== End HTTP Request Details ===\n")
    fmt.Fprintf(w, "Request received and logged!")
}

func main() {
    cert, err := tls.LoadX509KeyPair("server.crt", "server.key")
    if err != nil {
        log.Fatal(err)
    }

    tlsConfig := &tls.Config{
        Certificates: []tls.Certificate{cert},
        GetConfigForClient: func(hello *tls.ClientHelloInfo) (*tls.Config, error) {
            fmt.Println("\n=== Client Hello Details ===")
            fmt.Printf("Server Name (SNI): %s\n", hello.ServerName)

            fmt.Println("\nSupported TLS Versions:")
            for _, version := range hello.SupportedVersions {
                if name, ok := tlsVersions[version]; ok {
                    fmt.Printf("  - %s (0x%04X)\n", name, version)
                } else {
                    fmt.Printf("  - Unknown Version 0x%04X\n", version)
                }
            }

            fmt.Println("\nAdvertised Cipher Suites:")
            for _, cipher := range hello.CipherSuites {
                if name, ok := cipherSuites[cipher]; ok {
                    fmt.Printf("  - %s (0x%04X)\n", name, cipher)
                } else {
                    fmt.Printf("  - Unknown Cipher 0x%04X\n", cipher)
                }
            }

            fmt.Printf("\nSupports ALPN: %v\n", len(hello.SupportedProtos) > 0)
            if len(hello.SupportedProtos) > 0 {
                fmt.Printf("ALPN Protocols: %v\n", hello.SupportedProtos)
            }

            fmt.Println("=== End Client Hello Details ===\n")
            return nil, nil
        },
    }

    server := &http.Server{
        Addr:      ":8443",
        TLSConfig: tlsConfig,
        Handler:   http.HandlerFunc(handleRequest),
    }

    log.Printf("Starting server on :8443")
    log.Fatal(server.ListenAndServeTLS("", ""))
}
