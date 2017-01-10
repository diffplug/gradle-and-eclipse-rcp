# Gradle and Eclipse RCP

### Version 1.1.0-SNAPSHOT TBD

- `ide` now uses `thirdParty` to cleanly add a terminal and gradle syntax highlighting (e2ebf6c8cdb28b15cda881174b827201c11627e6)
- improved `target.maven` configuration so that subprojects can be configured lazily (f8963f3095c3c506ddecb5dce28a59fe9a13b65b)
- spotless is now applied per-project (50d4153017b65d488003907e224f764258a4f7d1)

### Version 1.0.0 - July 14th 2016

This version tests that you can clone, then run

- `gradlew ide`
- `gradlew assemble.all`

and get a nice IDE on win/mac/linux, and that it will generate launcher artifacts for win/mac/linux.

It has a known bug ([#4](https://github.com/diffplug/gradle_and_eclipse_rcp/issues/4)) regarding the mac launcher when building on mac.
