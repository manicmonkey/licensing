Licensing
=========

This is a project started in 2010 to play around with Scala and Vaadin. It's purpose is to act as a software licensing system.

I revived the project in mid 2014 and plan to find some time in between regular work, android development and game development to revitalize the project and put it into production.

Mockups can be seen using pencil.

[![Build Status](https://travis-ci.org/manicmonkey/licensing.svg?branch=master)](https://travis-ci.org/manicmonkey/licensing)

AngularJS frontend uses Grunt for build tasks and bower for dependency management. To get up and running install node/npm and run 'npm install' followed by 'grunt'. 

Roadmap
-------

- Get main UI elements / pages working
- Internationalization

- Investigate reports (maybe integrate High Charts)
- Would be cool to have a map with the locations of the installations (using something like http://maplacejs.com/)

Admin access for...

- Customers
- Products
- Users
- Auditing

todo...

- create users screen
- security is working
- need to expose user information from userendpoint - roles enabled
- list of users?
- access to customers (doesn't appear in domain model at moment?)

Create product documentation: http://www.thinkful.com/learn/a-guide-to-using-github-pages/start/existing-project/project-page/existing-repo/

Consider migrating DAOs to use Spring-data JPA Repository

Extract mock data insertion - maybe use the REST API