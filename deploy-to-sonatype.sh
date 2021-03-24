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
export GPG_TTY=$(tty)
mvn deploy -Psign-artifacts