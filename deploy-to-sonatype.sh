#!/bin/bash
# deploy-to-sonatype.sh
# Created by Steve Hannah, March 24, 2021
#
# Synopsis:
# --------
#
# Deploys project to Maven central.  You should run update-version.sh before and after
# running this to set the release version, and then create a new SNAPSHOT version.
#
# **IMPORTANT**
# Before running this, you should deploy the main Codename One project to maven central.
# After running this, you still need to log into the Sonatype console to close, and release
# the staging repository.
#
# Typical Workflow is:
# bash update-version 8.0.1 && bash deploy-to-sonatype.sh && bash update-version 8.0.2-SNAPSHOT
set -e
version=$(bash print-version.sh)
if [[ "$version" == *-SNAPSHOT ]]; then
  echo "This is a snapshot version so not deploying"
else

  # We need to install this version of Codename One so that it will build
  # correctly.
  if [ ! -f $HOME/.m2/repository/com/codenameone/codenameone-maven-plugin/${version}/codenameone-maven-plugin-${version}.jar ]; then
    echo "codenameone-maven-plugin v${version} not installed.  Trying to install now.."
    SCRIPTPATH=$(pwd)
    if [ ! -d target ]; then
      mkdir target
    fi
    cd target
    curl -L https://github.com/codenameone/CodenameOne/archive/refs/tags/v${version}.zip > master.zip
    unzip master.zip
    rm master.zip
    cd CodenameOne-${version}/maven
    mvn install
    cd $SCRIPTPATH
    echo "codeameone-maven-plugin v${version} successfully installed."
  fi

  echo "Deploying version ${version} to sonatype staging"
  MAVEN_ARGS="-X"
  if [ ! -z $MAVEN_GPG_PASSPHRASE ]; then
    MAVEN_ARGS="-Dgpg.passphrase=$MAVEN_GPG_PASSPHRASE -X"
  fi
  export GPG_TTY=$(tty)
  mvn deploy -Psign-artifacts $MAVEN_ARGS
fi
