#!/bin/bash
# update-version.sh
# Created by Steve Hannah.  March 24, 2021
#
# Synopsis:
# ---------
#
# Updates the version of this project and commits the version changes in Git.  If the version
# is a release version (i.e. not ending in '-SNAPSHOT', then it will also add a git tag).
#
# Typically you would run this before and after running the deploy-to-sonatype.sh script.  Before
# to set the release version, and after to change to a new SNAPSHOT version.
#
# **IMPORTANT**
# Before running this, you must update the version in the Main Codename One repo.  If it can't
# find a version of Codename One in the local maven repo to match the version that you're updating
# to, it will fail.
#
# Usage:
#  bash update-version.sh VERSION
#
# Arguments:
#  VERSION    The version number to update to.
#
# Examples:
#  bash update-version.sh 8.0.1
#     Updates to version 8.0.1
#
#  bash update-version.sh 8.0.2-SNAPSHOT
#     Updates to version 8.0.2-SNAPSHOT
#
# Typical Workflow is:
# bash update-version.sh 8.0.1 && bash deploy-to-sonatype.sh && bash update-version.sh 8.0.2-SNAPSHOT
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

echo "Committing version change in git"
git commit -a -m "Updated version to $version"
if [[ "$version" == *-SNAPSHOT ]]; then
  echo "This is a snapshot version so not adding a tag"
else
  echo "Adding git tag for 'v${version}'"
  git tag -a "v${version}" -m "Version ${version}"
fi



