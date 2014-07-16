# fedmsg-java

[![Build Status](https://travis-ci.org/fedora-infra/fedmsg-java.png)](https://travis-ci.org/fedora-infra/fedmsg-java)

A small library for emitting messages to the fedmsg bus from Java.

[javadoc](http://fedora-infra.github.io/fedmsg-java/).

**NOTE:**
If using this with a Jenkins master hosted on JDK 1.6, you must also compile
this dependency on a 1.6 JVM.

# Developing

```bash
$ sudo yum install sbt

$ git clone git://github.com/fedora-infra/fedmsg-java # for anonymous clone
# - OR -
$ git clone git@github.com:fedora-infra/fedmsg-java # for fedora-infra members

$ cd fedmsg-java && sbt compile
```

# License

Apache 2.
