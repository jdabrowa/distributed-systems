#!/bin/bash

sudo apt-key adv --keyserver keyserver.ubuntu.com --recv 5E6DA83306132997
sudo apt-add-repository "deb http://zeroc.com/download/apt/ubuntu`lsb_release -rs` stable main"
sudo apt-get update
sudo apt-get install zeroc-ice-all-runtime zeroc-ice-all-dev
