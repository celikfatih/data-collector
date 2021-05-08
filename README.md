## data-collector

This application has been created to obtain users `Twitter` username information from the 
[Letterboxd](https://letterboxd.com) site by a web crawling method. It collects profiles 
with twitter user names by taking their usernames from the monthly popular members page.

With the new changes, the last `N` tweets of any Twitter user can be pulled using the 
`Twitter API` and this data can be inserted into `Cassandra`.