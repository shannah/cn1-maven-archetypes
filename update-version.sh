#!/bin/bash
set -e
if [ -z $1 ]; then
  echo "Usage bash update-version.sh VERSION"
  echo "  Where VERSION is the version number to update to."
  echo "  E.g. bash update-version.sh 7.0.15-SNAPSHOT"
  exit 1
fi
version=$1
oldVersion=$(bash print-version.sh)
if [ $version == $oldVersion ]; then
  echo "Version is same as old version. Not updating version"
  exit 0
fi
echo "Version: $version"
mvn versions:set -DnewVersion=$version
mvn versions:commit

f=./cn1lib-archetype/src/main/resources/META-INF/maven/archetype-metadata.xml
perl -pi -e "s/<defaultValue>$oldVersion<\/defaultValue>/<defaultValue>$version<\/defaultValue>/g" "$f"
f=./cn1app-archetype/src/main/resources/META-INF/maven/archetype-metadata.xml
perl -pi -e "s/<defaultValue>$oldVersion<\/defaultValue>/<defaultValue>$version<\/defaultValue>/g" "$f"

f=./cn1app-archetype/src/test/resources/projects/basic/archetype.properties
perl -pi -e "s/^cn1Version=$oldVersion\$/cn1Version=$version/g" "$f"
f=./cn1lib-archetype/src/test/resources/projects/basic/archetype.properties
perl -pi -e "s/^cn1Version=$oldVersion\$/cn1Version=$version/g" "$f"



