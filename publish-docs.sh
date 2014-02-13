#!/usr/bin/env bash

echo "Publishing docs from origin/master at HEAD"

t="$(mktemp -d)"

git clone "git@github.com:fedora-infra/fedmsg-java.git" "$t/fedmsg-java"
pushd "$t/fedmsg-java"
sbt doc
mv target/scala*/api "$t"
git reset --hard HEAD
git checkout gh-pages
git rm -rf ./*
mv "$t"/api/* .
git add .
git commit -m 'Docs deploy'
git push origin gh-pages
popd

rm -rf "$t"

echo "Done."
