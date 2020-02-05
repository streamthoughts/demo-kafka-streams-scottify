#!/bin/bash

docker rm -v -f $(docker ps -a -q)
