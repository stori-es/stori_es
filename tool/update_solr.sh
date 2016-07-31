#!/bin/bash

# 1) Shutdown Solr.
/home/$USER/solr_tomcat/apache-tomcat-7.0.32/bin/shutdown.sh
# 2) Move the existing data and conf into a sub-dir
mkdir /home/$USER/solr_tomcat/apache-solr-config/stories
mv /home/$USER/solr_tomcat/apache-solr-config/conf /home/$USER/solr_tomcat/apache-solr-config/stories
mv /home/$USER/solr_tomcat/apache-solr-config/data /home/$USER/solr_tomcat/apache-solr-config/stories
# 3) Create new directory for collections core
mkdir -p /home/$USER/solr_tomcat/apache-solr-config/collections/data
mkdir -p /home/$USER/solr_tomcat/apache-solr-config/collections/conf
# 4) Pre-populate configuration with existing config
cp -r /home/$USER/solr_tomcat/apache-solr-config/stories/conf/* /home/$USER/solr_tomcat/apache-solr-config/collections/conf
# 5) Update the base solr.xml file
cp /home/user/solr_tomcat/apache-solr-config/solr.xml /home/user/solr_tomcat/apache-solr-config/solr.xml.orig-single-core
cat << 'EOF' > /home/user/solr_tomcat/apache-solr-config/solr.xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<!--
   This is an example of a simple "solr.xml" file for configuring one or 
   more Solr Cores, as well as allowing Cores to be added, removed, and 
   reloaded via HTTP requests.

   More information about options available in this configuration file, 
   and Solr Core administration can be found online:
   http://wiki.apache.org/solr/CoreAdmin
-->

<!--
 All (relative) paths are relative to the installation path
  
  persistent: Save changes made via the API to this file
  sharedLib: path to a lib directory that will be shared across all cores
-->
<solr persistent="false">

  <!--
  adminPath: RequestHandler path to manage cores.  
    If 'null' (or absent), cores will not be manageable via request handler
  -->
  <cores adminPath="/admin/cores" defaultCoreName="collection1">
    <core name="stories" instanceDir="./stories" />
    <core name="collections" instanceDir="./collections" />
  </cores>
</solr>
EOF
#6) Run the reindex to get the new core initialized
/home/$USER/main/tool/start_solr.sh
/home/$USER/main/tool/reindex.sh