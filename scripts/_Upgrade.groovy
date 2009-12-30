//
// This script is executed by Grails during application upgrade ('grails upgrade' command).
// This script is a Gant script so you can use all special variables
// provided by Gant (such as 'baseDir' which points on project base dir).
// You can use 'Ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
// Ant.mkdir(dir:"C:\Users\Paul\Documents\NetBeansProjects/exchange-rates/grails-app/jobs")
//

Ant.property(environment:"env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
Ant.copy(file:"${pluginBasedir}/grails-app/i18n/exchange-rates.properties", todir:"${basedir}/grails-app/i18n")
Ant.copy(file:"${pluginBasedir}/web-app/images/false.png", todir:"${basedir}/web-app/images")
Ant.copy(file:"${pluginBasedir}/web-app/images/true.png", todir:"${basedir}/web-app/images")
