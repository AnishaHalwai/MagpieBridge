
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-7-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->
<p align="center">
<img src="https://github.com/MagpieBridge/MagpieBridge/blob/develop/doc/logshort.png" width="400">
</p> 

## What is MagpieBridge? [![Build Status](https://travis-ci.com/MagpieBridge/MagpieBridge.svg?branch=develop)](https://travis-ci.com/MagpieBridge/MagpieBridge)[![Gitter](https://badges.gitter.im/MagpieBridgeHelp/community.svg)](https://gitter.im/MagpieBridgeHelp/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

MapgieBridge is a framework for integrating Static Analyses into IDEs and Editors with the [Language Server Protocol](https://microsoft.github.io/language-server-protocol/specification). It is a part of the [FutureSoot](http://sable.github.io/soot/future-soot/) project. MagpieBridge offers a default implementation of a language server---`MagpieServer` which runs analysis; a default TextDocumentService--`MagpieTextDocumentService` which handles requests like didOpen, didChange, didSave; a default WorkspaceService--`MagpieWorkspaceService` which handles different kinds of code actions and many other useful APIs for static analysis. For more information, please read our paper:
[MagpieBridge: A General Approach to Integrating Static Analyses into IDEs and Editors](https://drops.dagstuhl.de/opus/volltexte/2019/10813/pdf/LIPIcs-ECOOP-2019-21.pdf) (ECOOP 2019).
A [Poster](https://linghuiluo.github.io/ECOOP19MagpieBridgePoster.pdf) is also available. You can find the IRConverter under  [https://github.com/MagpieBridge/IRConverter](https://github.com/MagpieBridge/IRConverter) for converting WALA IR to Soot IR, if you want to analyze Java source code with Soot. 

<img src="https://github.com/MagpieBridge/MagpieBridge/blob/master/doc/goal.PNG"  width="800">

## Cite the research paper
For scientific usage, please **cite the paper** [[BibTex](https://drops.dagstuhl.de/opus/volltexte/2019/10813/)].

## Why is it called MagpieBridge?
In a Chinese legend, a human and a fairy fall in love, but this love angers the gods, who separate them
on opposite sides of the Milky Way. However, on the seventh day of the seventh lunar month each year,
thousands of magpies form a bridge, called 鹊桥 in Chinese and Queqiao in pinyin, allowing the lovers
to meet. We use MagpieBridge as a metaphor for a system that connects arbitrary static analysis to arbitrary IDE.

## How to use MapgieBridge?
- [Check the tutorials to create your first project with MagpieBridge](https://github.com/MagpieBridge/MagpieBridge/wiki) :star2:**UPDATED**:star2: 

## Integrated Static Analyses using MagpieBridge
- [MagpieBridge-based CogniCrypt](https://github.com/MagpieBridge/CryptoLSPDemo)
- [MagpieBridge-based FlowDroid](https://github.com/MagpieBridge/FlowDroidLSPDemo)

## Use MagpieBridge in your Maven project
You can either 
1. MagpieBridge is published on the GitHub Package Registry. You can use the release by adding the following lines to your `pom.xml` (See all [github package](https://github.com/MagpieBridge/MagpieBridge/packages/62902?version=0.0.7)). You can follow [these instructions](https://github.com/MagpieBridge/MagpieBridge/wiki/Tutorial-3.-How-To-Install-a-GitHub-Maven-Package). 
````
<dependencies>
  <dependency>
    <groupId>magpiebridge</groupId>
    <artifactId>magpiebridge</artifactId>
    <version>0.0.7</version>
  </dependency>
</dependencies>

<repositories>
  <repository>
    <id>github</id>
    <name>GitHub MagpieBridge Apache Maven Packages</name>
    <url>https://maven.pkg.github.com/MagpieBridge/MagpieBridge</url>
  </repository>
</repositories>
````

2. or build MagpieBridge by yourself 
    -  check out the develop branch with `git clone -b develop https://github.com/MagpieBridge/MagpieBridge.git`
    -  run `mvn install` in the project root directory to build the tool and run all tests. To skip tests, run `mvn install -DskipTests`.

## Build MagpieBridge in IDE
1. Simply import the project as maven project in your IDE, Maven should take care of all required dependences. For Eclipse: 
```
Eclipse> File> Import > Maven > Existing Maven Projects > Enter the path to your local repository > Finish
```
## Get Involved
- Pull requests are welcome!
- Submit github issues for any feature enhancements, bugs or documentation problems
- Please format the code with `mvn com.coveo:fmt-maven-plugin:format` before `git push`

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://linghuiluo.github.io/"><img src="https://avatars3.githubusercontent.com/u/18470909?v=4" width="100px;" alt=""/><br /><sub><b>Linghui Luo</b></sub></a><br /><a href="https://github.com/MagpieBridge/MagpieBridge/commits?author=linghuiluo" title="Code">💻</a> <a href="#design-linghuiluo" title="Design">🎨</a><a href="https://github.com/MagpieBridge/MagpieBridge/commits?author=linghuiluo" title="Documentation">📖</a></td>
    <td align="center"><a href="http://researcher.watson.ibm.com/researcher/view.php?person=us-dolby"><img src="https://avatars0.githubusercontent.com/u/1652606?v=4" width="100px;" alt=""/><br /><sub><b>Julian Dolby</b></sub></a><br /><a href="https://github.com/MagpieBridge/MagpieBridge/commits?author=juliandolby" title="Code">💻</a> <a href="#design-juliandolby" title="Design">🎨</a></td>
    <td align="center"><a href="https://cbruegg.com"><img src="https://avatars0.githubusercontent.com/u/175421?v=4" width="100px;" alt=""/><br /><sub><b>Christian Brüggemann</b></sub></a><br /><a href="https://github.com/MagpieBridge/MagpieBridge/commits?author=cbruegg" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/jonasmanuel"><img src="https://avatars1.githubusercontent.com/u/8150255?v=4" width="100px;" alt=""/><br /><sub><b>Jonas Manuel</b></sub></a><br /><a href="https://github.com/MagpieBridge/MagpieBridge/commits?author=jonasmanuel" title="Code">💻</a><a href="https://github.com/MagpieBridge/MagpieBridge/commits?author=jonasmanuel" title="Documentation">📖</a></td>
    <td align="center"><a href="https://github.com/swissiety"><img src="https://avatars0.githubusercontent.com/u/5645864?v=4" width="100px;" alt=""/><br /><sub><b>Markus Schmidt</b></sub></a><br /><a href="https://github.com/MagpieBridge/MagpieBridge/commits?author=swissiety" title="Tests">⚠️</a></td>
    <td align="center"><a href="http://joaocpereira.me"><img src="https://avatars0.githubusercontent.com/u/6281876?v=4" width="100px;" alt=""/><br /><sub><b>João Pereira</b></sub></a><br /><a href="https://github.com/MagpieBridge/MagpieBridge/issues?q=author%3Ajcp19" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/SvenEV"><img src="https://avatars3.githubusercontent.com/u/5737127?v=4" width="100px;" alt=""/><br /><sub><b>Sven Erik Vinkemeier</b></sub></a><br /><a href="https://github.com/MagpieBridge/MagpieBridge/issues?q=author%3ASvenEV" title="Bug reports">🐛</a></td>
  </tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!

## Contact 
&#x2709; linghui[at]outlook.de
