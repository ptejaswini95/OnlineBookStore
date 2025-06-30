#!/bin/bash

# Create database
psql -U postgres -c "CREATE DATABASE bookstore;"

# Execute initialization script
psql -U postgres -d bookstore -f init.sql 