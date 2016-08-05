#!/bin/bash

java -classpath dashboard/target/war/WEB-INF/classes/ org.consumersunion.stories.server.security.PasswordUtil $1
