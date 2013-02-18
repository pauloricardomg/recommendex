# Recommendex: A Very Simple Recommender System

This project simulates a web store where a user can browse items, add them to the shopping cart and also buy them. Based on these user actions, the system computes which items are correlated and suggest recommendations of related items when a particular item is being browsed.

## Web Application Install Instructions

1. Install Jetty >= 7.0, MySQL >= 5.0.
2. Execute the script {project.dir}/recommendex/db/create_db.sh and it will prompt you for the MySql root password. This is necessary to create the recommendex user and database.
3. Execute the script {project.dir}/recommendex/db/create_tables.sh in order to create the project DB schema.
3. Modify the "server.webapp.dir" property on {project.dir}/build.xml to point to the "webapps" directory of the Jetty installation.
4. Configure Jetty to enable JNDI support (instructions here: http://wiki.eclipse.org/Jetty/Feature/JNDI).
5. Run "ant deploy" in {project.dir}/recommendex.
6. Enter the jetty directory and run java -jar start.jar.
7. Open your browser and enter the following address to open the webapp: http://127.0.0.1:8080/recommendex
