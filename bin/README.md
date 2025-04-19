# P2P Chat Application

A peer-to-peer chat application designed and developed by group **TKK** from _Ho Chi Minh City University of Technology (HCMUT)_

![Demo-intro](https://github.com/ngoductuanlhp/p2p_appchat/blob/master/demo_images/login_screen.png)

This is a product for _Assignment 1_ of _Computer Network - CO3003 (semester 192)_

## Table of contents
* [Prerequisites](Prerequisites)
* [Installing](Installing)
* [Known issues](Known-issues)

## Prerequisites

This repo is the source-code, not the complied once, so you need to install some requirements first in order to run correctly.

_Recommend: You should use IntelliJ IDEA to compile and run this application for the best experiment._

1. OpenJDK 14 (including JavaSwing)
2. Maven: [Maven 4.0.0](https://maven.apache.org/)
3. Libraries:
    - [Apache commons lang 3](https://commons.apache.org/lang/download_lang.cgi)


## Installing

Follow these steps to compile and run the application correctly:
1. Run Server:

    * Compile and run file [./src/main/java/examples/TestServer.java](https://github.com/ngoductuanlhp/p2p_appchat/blob/master/src/main/java/examples/TestServer.java) first.
    * Find the correct IP address on the server computer.

2. Run Client:
    * Adjust the _serverIPAddress_ in [./src/main/java/examples/TestClient.java] to the corresponding IP address in the above step.
    * Compile and run file [./src/main/java/examples/TestClient.java](https://github.com/ngoductuanlhp/p2p_appchat/blob/master/src/main/java/examples/TestClient.java) and enjoy!

## Known issues

Any known issues will be placed here with solution. You should also help us detecting bugs and errors to fix them as soon as possible!
