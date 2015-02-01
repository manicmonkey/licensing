Licensing
=========

This project is designed to act as a software licensing system.

It's written in various technologies like Scala, Vaadin, AngularJS, JAX-RS / RESTEasy, Spring-Web, Spring-AOP.

UI mockups made in Pencil 2.0.5.

UML diagrams made in ArgoUML 0.34.

[![Build Status](https://travis-ci.org/manicmonkey/licensing.svg?branch=master)](https://travis-ci.org/manicmonkey/licensing)

AngularJS frontend uses Grunt for build tasks and bower for dependency management. To get up and running install node/npm and run
- npm install
- npm install -g grunt-cli
- grunt

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

- detail technical architecture - high level components and patterns.

Create product documentation: http://www.thinkful.com/learn/a-guide-to-using-github-pages/start/existing-project/project-page/existing-repo/