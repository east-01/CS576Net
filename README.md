CS-576Net
==============
## Table of Contents
- [Introduction](#introduction)
- [Installation](#installation)
- [Usage](#usage)

## Introduction
This project is a Java-based server/client socket program that enables communication between a server and multiple clients. It demonstrates the basics of socket programming in Java, including setting up a server, handling client connections, and exchanging messages between the server and clients. The server will accept a message you're sending and either encrypt it or decrypt it based on the passed flag.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/east-01/CS576Net.git
   ```
2. Open the project in your preferred Java IDE.

## Usage
Run src/main/java/net/emullen/Main.java with the following command line arguments<br>
<br>
*Required:*
```bash
   -server or -client
```
*Required (Clients):*
```bash
   -encrypt or -decrypt
```
*Optional:*
```bash
   -addr/address <address>
```
```bash
   -port <port>
```
```bash
   -e/enc <true/false>
```
Any additional arguments will be used as the client's message to send to the server.