#!/bin/bash

MAX_GROUPS=13

# Create empty git repository
git init

# Add all the files in the template directory to Git
git add .
git commit -m "Creation structure dossiers"

# For every group, add the corresponding remote and push
for i in $(seq -f "%02g" 1 1 3)
#for i in $(seq -f "%02g" 1 1 $MAX_GROUPS)
do
   git remote add Groupe$i https://gitlab.com/infof307-1920/groupe$i.git
   git push Groupe$i master
done